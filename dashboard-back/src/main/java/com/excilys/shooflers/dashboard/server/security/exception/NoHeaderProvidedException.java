package com.excilys.shooflers.dashboard.server.security.exception;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public class NoHeaderProvidedException extends RuntimeException {

  public NoHeaderProvidedException() {
    super("No Api-Key header was provided. This endpoint requires a valid Api-Key header");
  }

}
