package com.excilys.shooflers.dashboard.server.security.aspect;

import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.SessionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

import static com.excilys.shooflers.dashboard.server.service.SessionServiceImpl.REALM;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Aspect
@Component
public class RequireValidUserAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequireValidUserAspect.class);

  @Autowired
  private SessionService sessionService;

  /**
   * Picks out RequireValidUser annotation on Method.
   *
   * @see RequireValidUser
   */
  @Pointcut("@annotation(com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser)")
  private void methodAnnotatedWithRequireValidUser() {
  }

  /**
   * Picks out RequireValidUser annotation on Bean class.
   *
   * @see com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser
   */
  @Pointcut("within(@com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser *)")
  public void beanAnnotatedWithRequireValidUser() {
  }

  /**
   * Picks out all public methods on Bean class.
   *
   * @see com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser
   */
  @Pointcut("execution(public * *(..))")
  private void beanPublicMethod() {
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
      LOGGER.debug("In assertValidToken aspect");
      sessionService.validateUser();
      returnValue = joinPoint.proceed();
    } catch (RuntimeException e) {
      HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
      response.setHeader("WWW-Authenticate", "Basic realm=\"" + REALM + "\"");
      throw e;
    } catch (Throwable throwable) {
      throw new IllegalStateException(throwable);
    }
    return returnValue;
  }

  /**
   * Check if the user is valid, execute the intercepted method and update the session token.
   *
   * @param joinPoint the join point
   * @return result of the intercepted method
   * @see #beanAnnotatedWithRequireValidUser()
   * @see #beanPublicMethod()
   */
  @Around("beanAnnotatedWithRequireValidUser() && beanPublicMethod()")
  public Object checkValidUser2(ProceedingJoinPoint joinPoint) {
    return checkValidUser(joinPoint);
  }

}