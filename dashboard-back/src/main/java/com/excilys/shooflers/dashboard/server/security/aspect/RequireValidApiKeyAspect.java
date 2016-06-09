package com.excilys.shooflers.dashboard.server.security.aspect;

import com.excilys.shooflers.dashboard.server.service.SessionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

import static com.excilys.shooflers.dashboard.server.service.impl.SessionServiceImpl.REALM;

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
     * @see com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey
     */
    @Pointcut("@annotation(com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey)")
    private void methodAnnotatedWithRequireValidApiKey() {
    }

    /**
     * Picks out RequireValidApiKey annotation on Bean class.
     *
     * @see com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey
     */
    @Pointcut("within(@com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey *)")
    public void beanAnnotatedWithRequireValidApiKey() {
    }

    /**
     * Picks out all public methods on Bean class.
     *
     * @see com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey
     */
    @Pointcut("execution(public * *(..))")
    private void beanPublicMethod() {
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

    /**
     * Check if the apiKey is valid and execute the intercepted method.
     *
     * @param joinPoint the joinPoint
     * @return the return value
     * @throws Throwable if an error occurs in the intercepted method
     * @see #beanAnnotatedWithRequireValidApiKey()
     * @see #beanPublicMethod()
     */
    @Around("beanAnnotatedWithRequireValidApiKey() && beanPublicMethod()")
    public Object checkValidApiKey2(ProceedingJoinPoint joinPoint) {
        return checkValidApiKey(joinPoint);
    }

}