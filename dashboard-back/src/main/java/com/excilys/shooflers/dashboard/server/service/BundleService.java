package com.excilys.shooflers.dashboard.server.service;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;

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
    BundleMetadataDto get(String uuid);

    /**
     * Retrieve all bundles from DB.
     *
     * @return the retrieved bundles or empty
     */
    List<BundleMetadataDto> getAll();

    /**
     * Create a bundle into DB.
     *
     * @param bundle the bundle meta data
     */
    BundleMetadataDto create(BundleMetadataDto bundle);

    /**
     * Update a bundle into DB.
     *
     * @param bundle the bundle metadata to update
     * @return the new BundleMetadata or null, if failed
     */
    BundleMetadataDto update(BundleMetadataDto bundle);

    /**
     * Delete bundle from DB.
     *
     * @param uuid The bundle uuid
     */
    boolean delete(String uuid);
}
