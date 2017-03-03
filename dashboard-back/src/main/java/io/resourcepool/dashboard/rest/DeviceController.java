package io.resourcepool.dashboard.rest;

import io.resourcepool.dashboard.exception.ResourceNotFoundException;
import io.resourcepool.dashboard.model.Device;
import io.resourcepool.dashboard.security.annotation.RequireValidApiKey;
import io.resourcepool.dashboard.security.annotation.RequireValidUser;
import io.resourcepool.dashboard.service.DeviceService;
import io.resourcepool.dashboard.validator.DeviceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequireValidUser
@RequestMapping("/device")
public class DeviceController {

  @Autowired
  private DeviceService deviceService;

  @Autowired
  private DeviceValidator validator;

  /**
   * Get all Bundle.
   *
   * @return List of BundleDto
   */
  @RequestMapping(method = RequestMethod.GET)
  @RequireValidApiKey
  public List<Device> getAll() {
    List<Device> devices = deviceService.getAll();
    return devices;
  }

  /**
   * Get a particular Device by its id.
   *
   * @param id id to find
   * @return Device found if device exists
   */
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @RequireValidApiKey
  public Device get(@PathVariable("id") String id) {
    Device result = deviceService.get(id);
    if (result == null) {
      throw new ResourceNotFoundException("Device not found");
    } else {
      return result;
    }
  }

  /**
   * Update a device metadata
   *
   * @param device Device to update
   * @return
   */
  @RequestMapping(method = RequestMethod.PUT)
  public Device update(@RequestBody Device device) {
    validator.validate(device);
    if (device.getId() == null) {
      throw new IllegalArgumentException("A valid id is required to edit a device.");
    }
    Device deviceDb = deviceService.get(device.getId());
    deviceDb.setFeedId(device.getFeedId());
    deviceDb.setName(device.getName());
    deviceService.update(deviceDb);
    return deviceDb;
  }

  /**
   * Delete a device by its id.
   *
   * @param id id to delete
   */
  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  public void delete(@PathVariable("id") String id) {
    Device result = deviceService.get(id);
    if (result == null) {
      throw new ResourceNotFoundException("Bound not found");
    } else {
      deviceService.delete(id);
    }

  }
}
