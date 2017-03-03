package com.excilys.shooflers.dashboard.server.dao.impl;

import com.excilys.shooflers.dashboard.server.dao.DeviceDao;
import com.excilys.shooflers.dashboard.server.dao.util.YamlUtils;
import com.excilys.shooflers.dashboard.server.exception.ResourceIoException;
import com.excilys.shooflers.dashboard.server.model.Device;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Component
@DependsOn("daoInitializer")
public class DeviceDaoImpl implements DeviceDao {


    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDaoImpl.class);

    @Autowired
    private DashboardProperties props;

    @Autowired
    private FileSystem fileSystem;

    private Path deviceDatabasePath;

    @PostConstruct
    public void init() {
        deviceDatabasePath = fileSystem.getPath(props.getBasePath(), ENTITY_NAME);
    }

    @Override
    public Device get(String uuid) {
        Path dataFile = getDeviceFile(uuid);
        return readFeedFromFile(dataFile);
    }

    @Override
    public List<Device> getAll() {
        List<Device> devices = new LinkedList<>();
        try {
            Files.walk(deviceDatabasePath, 1)
                    .filter(Files::isRegularFile)
                    .forEach(path -> devices.add(readFeedFromFile(path)));
        } catch (IOException e) {
            LOGGER.error("exception in DeviceDaoImpl#getAll", e);
            throw new ResourceIoException(e);
        }
        return devices;
    }

    @Override
    public Device save(Device device) {
        Path dest = getDeviceFile(device.getId());
        YamlUtils.store(device, dest);
        return device;
    }

    @Override
    public Device delete(String uuid) {
        Device result = get(uuid);
        boolean success = YamlUtils.delete(deviceDatabasePath.resolve(uuid + ".yaml"));
        if (!success) {
            throw new ResourceIoException();
        }
        return result;
    }

    private Path getDeviceFile(String uuid) {
        String dataFileName = uuid + ".yaml";
        return deviceDatabasePath.resolve(dataFileName);
    }

    private Device readFeedFromFile(Path path) {
        return YamlUtils.read(path, Device.class);
    }
}
