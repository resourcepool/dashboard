package com.excilys.shooflers.dashboard.server.service;

import com.excilys.shooflers.dashboard.server.model.Device;

import java.util.List;

public interface DeviceService {

  List<Device> getAll();

  /**
   * Create a device into DB.
   *
   * @param device the device
   */
  void save(Device device);

  /**
   * Update a device into DB.
   *
   * @param device the device to update
   * @return the new Feed or null if failed
   */
  void update(Device device);

  /**
   * Delete device from DB.
   *
   * @param uuid The device uuid
   */
  void delete(String uuid);

  /**
   * Get device from DB.
   *
   * @param uuid the device uuid
   * @return the new device or null if none
   */
  Device get(String uuid);

  /**
   * Register new device into DB.
   *
   * @param device the device to register
   */
  void register(Device device);

  /**
   * Removes all references to a specific feed.
   *
   * @param uuid the feed uuid
   */
  void removeFeed(String uuid);
}
