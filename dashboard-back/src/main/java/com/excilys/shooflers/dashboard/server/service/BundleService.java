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
     * @param tag the bundle tag
     * @return the retrieved bundle or null if no match
     */
    BundleMetadata getByTag(String tag);

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
    void save(BundleMetadata bundle);

    /**
     * Update a bundle into DB.
     *
     * @param bundle the bundle metadata to update
     * @return the new BundleMetadata or null, if failed
     */
    void update(BundleMetadata bundle);

    /**
     * Delete bundle from DB.
     *
     * @param tag The bundle tag
     */
    void delete(String tag);
}
