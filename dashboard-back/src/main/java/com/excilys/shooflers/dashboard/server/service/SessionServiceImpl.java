package com.excilys.shooflers.dashboard.server.service;

import com.excilys.shooflers.dashboard.server.model.User;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.security.exception.AuthRequiredException;
import com.excilys.shooflers.dashboard.server.security.exception.IllegalApiKeyException;
import com.excilys.shooflers.dashboard.server.security.exception.NoHeaderProvidedException;
import com.excilys.shooflers.dashboard.server.security.util.BasicAuthUtils;
import com.excilys.shooflers.dashboard.server.security.util.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static com.excilys.shooflers.dashboard.server.security.interceptor.CorsInterceptor.HEADER_API_KEY;
import static com.excilys.shooflers.dashboard.server.security.interceptor.CorsInterceptor.PARAM_API_KEY;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Service
public class SessionServiceImpl implements SessionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

  @Autowired
  private DashboardProperties props;

  private String apiKey;
  private ThreadLocal<User> requestUser = new ThreadLocal<>();

  @PostConstruct
  public void init() {
    apiKey = props.getApiKey();
  }

  /**
   * Checks user's validity.
   */
  public void checkValidToken() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    
    String token = TokenUtils.parse(request);
    
    // If no token, then no Auth was provided
    if (token == null) {
      throw new AuthRequiredException();
    }
    
    // Validate token structure
    assertTokenValid(token);  
    
    // Retrieve login from decrypted token
    User user = TokenUtils.parse(token);
    
    // Update session token
    updateSessionToken(user, response);
  }

  private void assertTokenValid(String token) {
    if (StringUtils.isEmpty(token)) {
      throw new SecurityException("Illegal Token.");
    }
    String[] params = token.split(":");
    if (params.length != 2) {
      throw new SecurityException("Illegal Token.");
    }
    if (LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(params[1])), ZoneOffset.UTC).isBefore(LocalDateTime.now())) {
      throw new SecurityException("The token has expired.");
    }
  }

  @Override
  public void checkValidApiKey() {
    // Check Token
    // Retrieve Servlet Response
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    String requestApiKey = getApiKey(request);

    if (requestApiKey == null) {
      throw new NoHeaderProvidedException();
    }
    LOGGER.debug("Checking apiKey : {}", requestApiKey);

    // Throws if not valid
    assertValidApiKey(requestApiKey);

  }


  /**
   * Checks user's credentials.
   *
   * @param user the user.
   */
  public void assertCredentialsValid(User user) {
    // FIXME Password is written in clear. Maybe implement a more secure auth in the future.
    // (may not be necessary, but you never know)
    User dbUser = User.builder()
      .login(props.getAdminLogin())
      .password(props.getAdminPassword())
      .build();

    if (!user.getLogin().equals(dbUser.getLogin())) {
      throw new SecurityException("Wrong user provided");
    }
    
    if (!BasicAuthUtils.isValidPassword(user.getPassword()) || !user.getPassword().equals(dbUser.getPassword())) {
      throw new SecurityException("Wrong password provided");
    }

    user.setPassword(null);
  }

  /**
   * Logs in the user.
   */
  @Override
  public void login() {
    login(null);
  }

  /**
   * Logs in the user.
   *
   * @param user the user
   */
  @Override
  public void login(User user) {

    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

    // If user is not passed along, we should check request using Basic Auth
    if (user == null) {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

      user = BasicAuthUtils.parse(request);
    }

    // If no user, then no Auth was provided
    if (user == null) {
      throw new AuthRequiredException();
    }

    // Validate credentials
    assertCredentialsValid(user);

    // Set User in ThreadLocal and remove password from object
    setRequestUser(user);
    
    // Create Token
    updateSessionToken(user, response);
  }

  @Override
  public void clearThreadLocal() {
    requestUser.remove();
  }


  /**
   * Updates the TTL of the current Session-Token passed in the request header if any.
   * Sets it in the RequestContextService.
   *
   * @param user     the user
   * @param response the http response
   * @return the updated encrypted token of the user session
   */
  public String updateSessionToken(User user, HttpServletResponse response) {
    String token = TokenUtils.buildToken(user.getLogin(), props.getSessionTimeout());
    response.setHeader("Authorization", "Token " + token);
    return token;
  }


  /**
   * Removes old threadlocal user if not cleared, and sets the new one matching the provided id.
   *
   * @param user the user to set
   */
  private void setRequestUser(User user) {
    requestUser.remove();
    user.setPassword(null);
    requestUser.set(user);
  }


  private void assertValidApiKey(String requestApiKey) {
    // Throws if the apiKey does not exist in the database
    if (requestApiKey == null || !requestApiKey.equals(apiKey)) {
      // Wrong ApiKey
      LOGGER.warn("Wrong Api-Key provided: {}", requestApiKey);
      throw new IllegalApiKeyException(requestApiKey);
    }
  }

  /**
   * Get the api application id from request.
   *
   * @param request {@link javax.servlet.http.HttpServletRequest}
   * @return the api appId value
   */
  private String getApiKey(HttpServletRequest request) {
    String header = request.getHeader(HEADER_API_KEY);
    return header == null ? request.getParameter(PARAM_API_KEY) : header;
  }

}
