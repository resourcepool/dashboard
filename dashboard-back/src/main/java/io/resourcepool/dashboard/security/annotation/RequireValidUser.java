package io.resourcepool.dashboard.security.annotation;


import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Methods annotated with this require a valid connected user.
 *
 * @author Loïc Ortola on 07/06/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Order(value = 0)
public @interface RequireValidUser {

}