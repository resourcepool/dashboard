package com.excilys.shooflers.dashboard.server.security.util;

import com.excilys.shooflers.dashboard.server.model.User;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public class BasicAuthUtils {
    /**
     * Get token from request.
     * Token can be in the header or in parameter.
     *
     * @param request containing token
     * @return value of the token or {@code null} if does not exists
     */
    public static User parse(HttpServletRequest request) {

        if (request == null) {
            return null;
        }

        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader == null || !tokenHeader.startsWith("Basic")) {
            return null;
        }

        String[] base64 = tokenHeader.split(" ");
        String[] loginAndPassword = new String(Base64.decodeBase64(base64[1])).split(":");
        // Parsing basic auth header
        String login = loginAndPassword[0];
        String password = loginAndPassword[1];
        return User.builder()
                .login(login)
                .password(password.getBytes())
                .build();
    }
}
