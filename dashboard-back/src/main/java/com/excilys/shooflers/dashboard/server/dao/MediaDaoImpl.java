package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.dao.util.MediaReverseIndex;
import com.excilys.shooflers.dashboard.server.dao.util.YamlUtils;
import com.excilys.shooflers.dashboard.server.exception.ResourceIoException;
import com.excilys.shooflers.dashboard.server.exception.ResourceNotFoundException;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
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
    private Path mediaResourcesPath;
    
    private MediaReverseIndex mri = new MediaReverseIndex();

    @PostConstruct
    public void init() {
        mediaDatabasePath = Paths.get(props.getBasePath(), ENTITY_NAME);
        // Refresh reverse index
        mri.refreshDataset(getAll());
    }

    @Override
    public MediaMetadata get(String uuid) {
        String bundleUuid = mri.getBundleUuid(uuid);
        File dataFile = getMediaFile(bundleUuid + "/" + uuid);
        return readMediaFromFile(dataFile);
    }

    @Override
    public List<MediaMetadata> getAll() {
        List<MediaMetadata> mediaMetadatas = new LinkedList<>();
        // Get all media in bundle folder
        for (File bundle : mediaDatabasePath.toFile().listFiles(File::isDirectory)) {
            for (File media : bundle.listFiles(File::isFile)) {
                mediaMetadatas.add(readMediaFromFile(media));
            }
        }
        return mediaMetadatas;
    }

    @Override
    public List<MediaMetadata> getByBundle(String bundleUuid) {
        List<MediaMetadata> mediaMetadatas = new LinkedList<>();
        // Get all media in bundle folder
        Path bundle = mediaDatabasePath.resolve(bundleUuid);
        if (!Files.exists(bundle) || Files.isDirectory(bundle)) {
            return null;
        }
        for (File media : bundle.toFile().listFiles(File::isFile)) {
            mediaMetadatas.add(readMediaFromFile(media));
        }
        return mediaMetadatas;
    }

    @Override
    public MediaMetadata save(MediaMetadata mediaMetadata) {
        if (mediaMetadata.getUuid() == null) {
            mediaMetadata.setUuid(UUID.randomUUID().toString());
        }
        File dest = getMediaFile(mediaMetadata.getBundleTag() + "/" + mediaMetadata.getUuid());
        YamlUtils.store(mediaMetadata, dest);
        // Refresh Reverse index
        refreshReverseIndex();
        return mediaMetadata;
    }

    @Override
    public boolean delete(String uuid) {
        String bundleUuid = mri.getBundleUuid(uuid);
        if (bundleUuid == null) {
            throw new ResourceNotFoundException();
        }
        boolean result = YamlUtils.delete(mediaDatabasePath.resolve(bundleUuid + "/" + uuid + ".yaml").toFile());
        // Refresh Reverse index
        refreshReverseIndex();
        if (!result) {
            throw new ResourceIoException();
        }
        return result;
    }
    
    private void refreshReverseIndex() {
        mri.invalidate();
        mri.refreshDataset(getAll());
    }

    private File getMediaFile(String uuid) {
        String dataFileName = uuid + ".yaml";
        return mediaDatabasePath.resolve(dataFileName).toFile();
    }


    private MediaMetadata readMediaFromFile(File dataFile) {
        return YamlUtils.read(dataFile, MediaMetadata.class);
    }
}
