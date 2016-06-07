package com.excilys.shooflers.dashboard.server.security.aspect;

import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.SessionServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Aspect
@Component
public class RequireValidUserAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequireValidUserAspect.class);

  @Autowired
  private SessionServiceImpl sessionService;

  /**
   * Picks out RequireValidUser annotation on Method.
   *
   * @see RequireValidUser
   */
  @Pointcut("@annotation(com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser)")
  private void methodAnnotatedWithRequireValidUser() {
  }

  /**
   * Check if the user is valid, execute the intercepted method and update the session token.
   *
   * @param joinPoint the join point
   * @return result of the intercepted method
   * @see #methodAnnotatedWithRequireValidUser()
   */
  @Around("methodAnnotatedWithRequireValidUser()")
  public Object checkValidUser(ProceedingJoinPoint joinPoint) {
    Object returnValue = null;
    try {
      LOGGER.debug("In checkValidToken aspect");
      sessionService.checkValidToken();
      returnValue = joinPoint.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Throwable throwable) {
      throw new IllegalStateException(throwable);
    } finally {
      sessionService.clearThreadLocal();
    }
    return returnValue;
  }

}