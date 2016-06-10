package com.excilys.shooflers.dashboard.server.validator;

/**
 * Interface Generic for a Validator
 *
 * @author Mickael
 */
public interface Validator<T> {
    public void validate(T t);
}
