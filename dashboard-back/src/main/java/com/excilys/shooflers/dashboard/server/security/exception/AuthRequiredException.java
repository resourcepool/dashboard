package com.excilys.shooflers.dashboard.server.security.exception;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public class AuthRequiredException extends RuntimeException {
  
  public AuthRequiredException() {
    super("Authentication is required. Please provide credentials using Basic Auth");
  }
}
