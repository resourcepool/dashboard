package com.excilys.shooflers.dashboard.server.security.aspect;

import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.service.SessionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Loïc Ortola on 07/06/2016.
 */

@Aspect
@Component
public class RequireValidApiKeyAspect {

  @Autowired
  private SessionService sessionService;

  /**
   * Picks out RequireValidApiKey annotation on Method.
   *
   * @see RequireValidApiKey
   */
  @Pointcut("@annotation(com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey)")
  private void methodAnnotatedWithRequireValidApiKey() {
  }

  /**
   * Check if the apiKey is valid and execute the intercepted method.
   *
   * @param joinPoint the joinPoint
   * @return the return value
   * @throws Throwable if an error occurs in the intercepted method
   * @see #methodAnnotatedWithRequireValidApiKey()
   */
  @Around("methodAnnotatedWithRequireValidApiKey()")
  public Object checkValidApiKey(ProceedingJoinPoint joinPoint) {
    Object returnValue = null;
    try {
      sessionService.checkValidApiKey();
      returnValue = joinPoint.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Throwable throwable) {
      throw new IllegalStateException(throwable);
    }
    return returnValue;
  }

}