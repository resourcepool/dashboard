package com.excilys.shoofleurs.dashboard.database.dao;

import java.util.List;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public interface Dao<T> {

    /**
     * Retrieve t from DB.
     *
     * @param id the t id
     * @return the retrieved t or null if no match
     */
    T get(String id);

    /**
     * Retrieve all ts from DB.
     *
     * @return the retrieved ts or empty
     */
    List<T> getAll();

    /**
     * Save a t into DB.
     *
     * @param t the bundle meta data
     */
    void save(T t);

    /**
     * Save all ts into DB.
     *
     * @param ts the bundle meta data
     */
    void saveAll(List<T> ts);

    /**
     * Delete t from DB.
     *
     * @param id The t id
     */
    void delete(String id);
}
