package io.resourcepool.dashboard.dao;


import io.resourcepool.dashboard.model.Device;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface DeviceDao {

    String ENTITY_NAME = "device";

    /**
     * Retrieve device from DB using its id.
     * @param uuid the device uuid
     * @return the retrieved device or null if no match
     */
    Device get(String uuid);

    /**
     * Retrieve all devices from DB.
     *
     * @return the retrieved devices or empty
     */
    List<Device> getAll();

    /**
     * Save a device into DB.
     *
     * @param deviceMetaData the device meta data
     */
    Device save(Device deviceMetaData);

    /**
     * Delete device from DB.
     *
     * @param uuid The device tag
     */
    Device delete(String uuid);


}