package com.excilys.shooflers.dashboard.server.service;

import com.excilys.shooflers.dashboard.server.model.User;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface SessionService {
  
  void checkValidApiKey();

  void login();

  void login(User user);

  void clearThreadLocal();
}
