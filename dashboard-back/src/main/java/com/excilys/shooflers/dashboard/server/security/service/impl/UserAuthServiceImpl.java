package com.excilys.shooflers.dashboard.server.security.service.impl;

import com.excilys.shooflers.dashboard.server.model.User;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.security.exception.AuthRequiredException;
import com.excilys.shooflers.dashboard.server.security.model.TokenMetaData;
import com.excilys.shooflers.dashboard.server.security.util.BasicAuthUtils;
import com.excilys.shooflers.dashboard.server.security.util.StringEncryptor;
import com.excilys.shooflers.dashboard.server.security.util.TokenUtils;
import com.excilys.shooflers.dashboard.server.security.service.UserAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

/**
 * This service provides user authentication capabilities.
 *
 * @author Loïc Ortola on 08/06/2016.
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private DashboardProperties props;

    private User adminUser;

    @PostConstruct
    public void init() {
        // Get AdminUser from properties
        adminUser = User.builder()
                .login(props.getAdminLogin())
                .password(props.getAdminPassword().getBytes())
                .salt(StringEncryptor.SIG.nextSalt())
                .build();
    }

    @Override
    public void validateUser() {
        // Two possibilities: either through Basic-Auth or through Token.
        if (isLoginAttempt()) {
            attemptLogin();
        } else {
            assertValidToken();
        }
    }

    /**
     * Checks user's validity.
     */
    private void assertValidToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        String decryptedToken = TokenUtils.parse(request);

        // If no token, then no Auth was provided
        if (decryptedToken == null) {
            throw new AuthRequiredException();
        }

        // Validate token structure
        assertTokenValid(decryptedToken);

        // Retrieve user meta-data from decrypted token
        TokenMetaData tokenMetaData = TokenUtils.parse(decryptedToken);

        // Update session token
        updateSessionToken(tokenMetaData, response);
    }

    /**
     * Assert the validity of a token structure. A token is valid if its structure is built under the form:
     * login:hash:instant
     * and if the login:hash part matches the admin user
     *
     * @param token
     */
    private void assertTokenValid(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new SecurityException("Illegal Token.");
        }
        String[] params = token.split(":");
        if (params.length != 3) {
            throw new SecurityException("Illegal Token.");
        }
        if (LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(params[2])), ZoneOffset.UTC).isBefore(LocalDateTime.now())) {
            throw new SecurityException("The token has expired.");
        }
        TokenMetaData tokenMetaData = TokenUtils.parse(token);
        if (!tokenMetaData.isValidAgainst(adminUser)) {
            throw new SecurityException("Illegal token.");
        }
    }

    /**
     * Checks user's credentials.
     *
     * @param user the user.
     */
    private void assertCredentialsValid(User user) {
        // FIXME Password is written in clear. Maybe implement a more secure auth in the future.
        // (may not be necessary, but you never know)

        if (!user.getLogin().equals(adminUser.getLogin())) {
            throw new SecurityException("Wrong user provided");
        }

        if (!Arrays.equals(user.getPassword(), adminUser.getPassword())) {
            throw new SecurityException("Wrong password provided");
        }

        user.setSalt(adminUser.getSalt());
    }

    /**
     * Logs in the user.
     */
    private void attemptLogin() {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        User user = BasicAuthUtils.parse(request);

        // If no user, then no Auth was provided
        if (user == null) {
            throw new AuthRequiredException();
        }

        // Validate credentials
        assertCredentialsValid(user);

        // Create Token
        updateSessionToken(TokenMetaData.fromUser(adminUser), response);
    }


    /**
     * Updates the TTL of the current Session-Token passed in the request header if any.
     * Sets it in the RequestContextService.
     *
     * @param tokenMetaData the user
     * @param response      the http response
     * @return the updated encrypted token of the user session
     */
    private String updateSessionToken(TokenMetaData tokenMetaData, HttpServletResponse response) {
        String token = TokenUtils.buildToken(tokenMetaData.login, tokenMetaData.hash, props.getSessionTimeout());
        response.setHeader("Authorization", "Token " + token);
        return token;
    }


    /**
     * Checks whether we are attempting to attemptLogin or already have done so
     *
     * @return true if attemptLogin attempt, false otherwise
     */
    private boolean isLoginAttempt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String tokenHeader = request.getHeader("Authorization");
        return (tokenHeader != null && tokenHeader.startsWith("Basic"));
    }

}
