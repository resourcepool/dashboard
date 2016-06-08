package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.dao.util.YamlUtils;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Component
public class MediaDaoImpl implements MediaDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaDaoImpl.class);

    @Autowired
    private DashboardProperties props;

    private Path mediaDatabasePath;

    @PostConstruct
    public void init() {
        mediaDatabasePath = Paths.get(props.getBasePath(), ENTITY_NAME);
    }

    @Override
    public MediaMetadata get(String uuid) {
        File dataFile = getBundleFile(uuid);
        return readBundleFromFile(dataFile);
    }

    @Override
    public List<MediaMetadata> getAll() {
        List<MediaMetadata> mediaMetadatas = new LinkedList<>();
        for (File b : mediaDatabasePath.toFile().listFiles(File::isFile)) {
            mediaMetadatas.add(readBundleFromFile(b));
        }
        return mediaMetadatas;
    }

    @Override
    public MediaMetadata save(MediaMetadata mediaMetadata) {
        if (mediaMetadata.getUuid() == null) {
            mediaMetadata.setUuid(UUID.randomUUID().toString());
        }
        File dest = getBundleFile(mediaMetadata.getUuid());
        YamlUtils.store(mediaMetadata, dest);
        return mediaMetadata;
    }

    @Override
    public boolean delete(String uuid) {
        return YamlUtils.delete(mediaDatabasePath.resolve(uuid + ".yaml").toFile());
    }

    private File getBundleFile(String uuid) {
        String dataFileName = uuid + ".yaml";
        return mediaDatabasePath.resolve(dataFileName).toFile();
    }


    private MediaMetadata readBundleFromFile(File dataFile) {
        return YamlUtils.read(dataFile, MediaMetadata.class);
    }
}
