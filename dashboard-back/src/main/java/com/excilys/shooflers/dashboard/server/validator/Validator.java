package com.excilys.shooflers.dashboard.server.validator;

/**
 * Created by Mickael on 09/06/2016.
 */
public interface Validator<T> {
    public void validate(T t);
}
