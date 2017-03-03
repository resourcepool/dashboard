package io.resourcepool.dashboard.validator;

/**
 * Interface Generic for a Validator
 *
 * @author Mickael
 */
public interface Validator<T> {
    public void validate(T t);
}
