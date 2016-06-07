package com.excilys.shooflers.dashboard.server.security.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with this require a valid API Key.
 *
 * @author Loïc Ortola on 07/06/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Order(value = 1)
public @interface RequireValidApiKey {
}