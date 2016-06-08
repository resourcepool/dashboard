package com.excilys.shooflers.dashboard.server.security.service;

/**
 * @author Loïc Ortola on 08/06/2016.
 */
public interface ApiKeyService {

  /**
   * Assert if valid ApiKey was provided.
   * Api-Key is either provided with an X-Api-Key header or via a api-key parameter
   */
  void validateApiKey();
}
