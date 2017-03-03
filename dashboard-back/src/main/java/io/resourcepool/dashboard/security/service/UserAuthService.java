package io.resourcepool.dashboard.security.service;

/**
 * @author Loïc Ortola on 08/06/2016.
 */
public interface UserAuthService {
  /**
   * Assert if valid credentials were provided.
   * Credentials are either provided with basic auth headers at login time, or via a session-token
   */
  void validateUser();
}
