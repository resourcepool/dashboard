package com.excilys.shooflers.dashboard.server.security.exception;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public class IllegalApiKeyException extends RuntimeException {

  public IllegalApiKeyException() {
    super("Illegal Api-Key provided");
  }

  public IllegalApiKeyException(String apiKey) {
    super("Illegal Api-Key provided: " + apiKey);
  }

}
