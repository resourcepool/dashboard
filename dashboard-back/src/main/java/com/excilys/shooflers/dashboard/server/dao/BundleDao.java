package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface BundleDao {

    String ENTITY_NAME = "bundle";

    /**
     * Retrieve bundle from DB.
     *
     * @param uuid the bundle uuid
     * @return the retrieved bundle or null if no match
     */
    BundleMetadata get(String uuid);

    /**
     * Retrieve all bundles from DB.
     *
     * @return the retrieved bundles or empty
     */
    List<BundleMetadata> getAll();

    /**
     * Save a bundle into DB.
     *
     * @param bundle the bundle meta data
     */
    BundleMetadata save(BundleMetadata bundle);

    /**
     * Delete bundle from DB.
     *
     * @param uuid The bundle uuid
     */
    boolean delete(String uuid);
}