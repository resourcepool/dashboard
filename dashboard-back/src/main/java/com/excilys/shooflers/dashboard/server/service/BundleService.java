package com.excilys.shooflers.dashboard.server.service;

import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;

import java.util.List;

/**
 * @author Mickael VIEGAS
 */
public interface BundleService {

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
     * Create a bundle into DB.
     *
     * @param bundle the bundle meta data
     */
    BundleMetadata create(BundleMetadata bundle);

    /**
     * Update a bundle into DB.
     *
     * @param bundle the bundle metadata to update
     * @return the new BundleMetadata or null, if failed
     */
    BundleMetadata update(BundleMetadata bundle);

    /**
     * Delete bundle from DB.
     *
     * @param uuid The bundle uuid
     */
    boolean delete(String uuid);
}
