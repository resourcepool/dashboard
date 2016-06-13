package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.dao.util.MediaReverseIndex;
import com.excilys.shooflers.dashboard.server.dao.util.YamlUtils;
import com.excilys.shooflers.dashboard.server.exception.ResourceIoException;
import com.excilys.shooflers.dashboard.server.exception.ResourceNotFoundException;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        mediaResourcesPath = Paths.get(props.getBaseResources());
        // Refresh reverse index
        mri.refreshDataset(getAll());
    }

    @Override
    public MediaMetadata get(String uuid) {
        String bundleUuid = mri.getBundleTag(uuid);
        File dataFile = getMediaFile(bundleUuid, uuid);
        return readMediaFromFile(dataFile);
    }

    @Override
    public File getContent(String filename) {
        String uuid = filename.substring(0, filename.indexOf("."));
        String ext = filename.substring(uuid.length());
        File dataFile = getResourceFile(mri.getBundleTag(uuid), uuid, ext);
        return dataFile;
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
    public List<MediaMetadata> getByBundle(String bundleTag) {
        List<MediaMetadata> mediaMetadatas = new LinkedList<>();
        // Get all media in bundle folder
        Path bundle = mediaDatabasePath.resolve(bundleTag);
        if (!Files.exists(bundle) || !Files.isDirectory(bundle)) {
            return mediaMetadatas;
        }
        for (File media : bundle.toFile().listFiles(File::isFile)) {
            mediaMetadatas.add(readMediaFromFile(media));
        }
        return mediaMetadatas;
    }

    @Override
    public void save(Media media) {
        MediaMetadata mediaMetadata = media.getMetadata();
        MediaType mediaType = mediaMetadata.getMediaType();

        File dest = getMediaFile(mediaMetadata.getBundleTag(), mediaMetadata.getUuid());
        YamlUtils.store(mediaMetadata, dest);

        MultipartFile content = media.getContent();

        // TODO implement some kind of rollback if second content write fails
        if (content != null) {
            String ext = mediaType.getExtension(content.getContentType());
            dest = getResourceFile(mediaMetadata.getBundleTag(), mediaMetadata.getUuid(), ext);
            try {
                FileCopyUtils.copy(content.getBytes(), dest);

            } catch (IOException e) {
                LOGGER.warn("Error while writing file.", e);
                throw new IllegalStateException(e);
            }
        }

        // Refresh Reverse index
        refreshReverseIndex();
    }

    @Override
    public void deleteByBundle(String bundleTag) {

        if (bundleTag == null) {
            throw new ResourceNotFoundException();
        }
        // FIXME for now, we chose not to delete files when media is destroyed.
        try {
            // Delete each media of bundle tag
            Set<String> medias = mri.getMedias(bundleTag);
            if (medias == null) {
                return;
            }
            medias.forEach(this::delete);
            // Remove parent directory
            FileUtils.deleteDirectory(mediaDatabasePath.resolve(bundleTag).toFile());
        } catch (IOException e) {
            throw new ResourceIoException(e);
        }
        // Refresh Reverse index
        refreshReverseIndex();
    }

    @Override
    public void delete(String uuid) {
        String bundleTag = mri.getBundleTag(uuid);
        if (bundleTag == null) {
            throw new ResourceNotFoundException();
        }
        boolean result = YamlUtils.delete(mediaDatabasePath.resolve(bundleTag + "/" + uuid + ".yaml").toFile());
        if (!result) {
            throw new ResourceIoException();
        }
        // FIXME for now, we chose not to delete files when media is destroyed.
        // Refresh Reverse index
        refreshReverseIndex();
    }

    private void refreshReverseIndex() {
        mri.invalidate();
        mri.refreshDataset(getAll());
    }

    /**
     * Retrieve media file from bundle tag and media tag
     *
     * @param bundleTag
     * @param uuid
     * @return
     */
    private File getMediaFile(String bundleTag, String uuid) {
        String dataFileName = uuid + ".yaml";
        return mediaDatabasePath.resolve(bundleTag + "/" + dataFileName).toFile();
    }

    private File getResourceFile(String bundleTag, String uuid, String ext) {
        String dataFileName = uuid + (ext.startsWith(".") ? ext : "." + ext);
        return mediaResourcesPath.resolve(bundleTag + "/" + dataFileName).toFile();
    }


    private MediaMetadata readMediaFromFile(File dataFile) {
        return YamlUtils.read(dataFile, MediaMetadata.class);
    }
}
