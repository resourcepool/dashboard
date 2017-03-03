package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.model.Device;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequestMapping("/discovery")
public class DiscoveryController {

  @Autowired
  private DeviceService deviceService;

  /**
   * Get all Device.
   *
   * @return List of DeviceDto
   */
  @RequestMapping(method = RequestMethod.GET)
  @RequireValidUser
  @RequireValidApiKey
  public List<Device> getAll() {
    List<Device> devices = deviceService.getAll();
    return devices;
  }

  /**
   * Register or check-in Device
   *
   * @param id the device unique id
   * @return The Device if exists, none otherwise
   */
  @RequireValidApiKey
  @RequestMapping(value = "{id}", method = RequestMethod.POST)
  public Device get(@PathVariable("id") String id, HttpServletRequest request) {
    Device result = deviceService.get(id);
    if (result == null) {
      result = Device.builder()
        .id(id)
        .lastHealthCheck(LocalDateTime.now())
        .lastKnownIp(request.getRemoteAddr())
        .build();
      deviceService.register(result);
    } else {
      result.setLastHealthCheck(LocalDateTime.now());
      result.setLastKnownIp(request.getRemoteAddr());
      deviceService.update(result);
    }
    return result;
  }
}
