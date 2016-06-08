package com.excilys.shooflers.dashboard.server.security.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Loïc Ortola on 07/06/2016.
 *         This filter implements a support for the CORS protocol
 */
public class CorsInterceptor implements HandlerInterceptor {

    public static final String HEADER_API_KEY = "x-api-key";
    public static final String PARAM_API_KEY = "api-key";

    private static final Logger LOGGER = LoggerFactory.getLogger(CorsInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        LOGGER.trace("In CorsFilter");

        // Add CORS Headers to every response
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, Api-Key");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Access-Control-Allow-Origin", "*");

        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            // Return immediately with 200-OK if OPTIONS for CORS
            LOGGER.trace("OPTIONS Request: immediate return");
            // Check whether to use a wildcard or the real origin
            String requestHeaders = request.getHeader("Access-Control-Request-Headers");
            if (requestHeaders != null) {
                // Parsing headers
                String[] headers = requestHeaders.toLowerCase().split(",");
                for (String header : headers) {
                    if (header.trim().equals(HEADER_API_KEY)) {
                        LOGGER.trace("Api-Key requested. Allowing origin: " + request.getHeader("Origin"));
                        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
                        response.setHeader("Access-Control-Allow-Credentials", "true");
                    }
                }
            }
            // Filter will return and bypass the rest of the chain
            return false;
        } else {
            // Carry on with next item in the request chain
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
