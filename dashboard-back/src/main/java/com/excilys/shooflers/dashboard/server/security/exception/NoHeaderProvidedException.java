package com.excilys.shooflers.dashboard.server.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="No Api-Key header was provided. This endpoint requires a valid Api-Key header")
public class NoHeaderProvidedException extends RuntimeException {

  public NoHeaderProvidedException() {
    super("No Api-Key header was provided. This endpoint requires a valid Api-Key header");
  }

}
