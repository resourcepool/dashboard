package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.dao.DeviceDao;
import com.excilys.shooflers.dashboard.server.model.Device;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.service.DeviceService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 03/03/2017
 */
@Service
public class DeviceServiceImpl implements DeviceService {
  
  @Autowired
  private RevisionService revisionService;
  
  @Autowired
  private DeviceDao deviceDao;
  
  @Override
  public List<Device> getAll() {
    return deviceDao.getAll();
  }

  @Override
  public void save(Device device) {
    deviceDao.save(device);
    revisionService.add(Revision.Type.DEVICE, Revision.Action.ADD, device.getId());
  }

  @Override
  public void update(Device device) {
    deviceDao.delete(device.getId());
    deviceDao.save(device);
    // Tell revision service we updated a device
    revisionService.add(Revision.Type.DEVICE, Revision.Action.UPDATE, device.getId());
  }

  @Override
  public void delete(String uuid) {
    deviceDao.delete(uuid);
    // Create a new revision
    revisionService.add(Revision.Type.DEVICE, Revision.Action.DELETE, uuid);
  }

  @Override
  public Device get(String uuid) {
    return deviceDao.get(uuid);
  }

  @Override
  public void register(Device device) {
    save(device);
  }

  @Override
  public void removeFeed(String uuid) {
    List<Device> all = getAll();
    if (all == null || all.isEmpty()) {
      return;
    }
    all.forEach(device -> {
      if (uuid.equals(device.getFeedId())) {
        device.setFeedId(null);
        update(device);
      }
    });
  }
}
