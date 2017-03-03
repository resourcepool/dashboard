package io.resourcepool.dashboard.security.aspect;

import io.resourcepool.dashboard.service.SessionService;
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

import static io.resourcepool.dashboard.service.impl.SessionServiceImpl.REALM;

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
   * @see io.resourcepool.dashboard.security.annotation.RequireValidUser
   */
  @Pointcut("@annotation(io.resourcepool.dashboard.security.annotation.RequireValidUser) && " +
    "!@annotation(io.resourcepool.dashboard.security.annotation.RequireValidApiKey) && " +
    "!within(@io.resourcepool.dashboard.security.annotation.RequireValidApiKey *) ")
  private void methodAnnotatedWithRequireValidUser() {
  }

  /**
   * Picks out RequireValidUser annotation on Bean class.
   *
   * @see io.resourcepool.dashboard.security.annotation.RequireValidUser
   */
  @Pointcut("within(@io.resourcepool.dashboard.security.annotation.RequireValidUser *) && " +
    "!@annotation(io.resourcepool.dashboard.security.annotation.RequireValidApiKey) && " +
    "!within(@io.resourcepool.dashboard.security.annotation.RequireValidApiKey *) ")
  public void beanAnnotatedWithRequireValidUser() {
  }

  /**
   * Picks out all public methods on Bean class.
   *
   * @see io.resourcepool.dashboard.security.annotation.RequireValidUser
   */
  @Pointcut("execution(public * *(..))")
  private void beanPublicMethod() {
  }

  // Combination of RequireVU & RequireVAK
  @Pointcut("(@annotation(io.resourcepool.dashboard.security.annotation.RequireValidUser) && @annotation(io.resourcepool.dashboard.security.annotation.RequireValidApiKey)) || " +
    "(within(@io.resourcepool.dashboard.security.annotation.RequireValidUser *) && @annotation(io.resourcepool.dashboard.security.annotation.RequireValidApiKey)) || " +
    "(@annotation(io.resourcepool.dashboard.security.annotation.RequireValidUser) && within(@io.resourcepool.dashboard.security.annotation.RequireValidApiKey *)) || " +
    "(within(@io.resourcepool.dashboard.security.annotation.RequireValidUser *) && within(@io.resourcepool.dashboard.security.annotation.RequireValidApiKey *))"
  )
  private void annotatedWithRequireValidUserAnd() {

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

  @Around("annotatedWithRequireValidUserAnd()")
  public Object checkValidUserOrValid(ProceedingJoinPoint joinPoint) {
    Object returnValue = null;
    try {
      try {
        sessionService.validateApiKey();
      } catch (RuntimeException ignore) {
        sessionService.validateUser();
      }
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
}