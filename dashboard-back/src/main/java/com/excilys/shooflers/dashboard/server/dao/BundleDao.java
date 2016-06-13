package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface BundleDao {

    String ENTITY_NAME = "bundle";

    BundleMetadata get(String uuid);

    /**
     * Retrieve bundle from DB.
     *
     * @param uuid the bundle tag
     * @return the retrieved bundle or null if no match
     */
    BundleMetadata getByTag(String uuid);

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
     * @param uuid The bundle tag
     */
    BundleMetadata delete(String uuid);

    
}