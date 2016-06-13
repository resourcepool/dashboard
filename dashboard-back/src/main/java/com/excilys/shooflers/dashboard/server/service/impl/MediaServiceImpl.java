package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private DashboardProperties props;

    @Override
    public MediaMetadata get(String uuid) {
        return mediaDao.get(uuid);
    }


    @Override
    public List<MediaMetadata> getAll() {
        return mediaDao.getAll();
    }

    @Override
    public List<MediaMetadata> getByBundleTag(String bundleTag) {
        return mediaDao.getByBundle(bundleTag);
    }

    @Override
    public void save(Media media) {
        // Save is ALWAYS a new UUID as media is immutable
        media.getMetadata().setUuid(UUID.randomUUID().toString());
        // Compute content Url
        media.getMetadata().setUrl(computeUrl(media));
        // This is a new media.
        mediaDao.save(media);
        // Tell revision service we added a media
        revisionService.add(Revision.Type.MEDIA, Revision.Action.ADD, media.getMetadata().getUuid());

    }
    
    @Override
    public void update(Media media) {
        String originalUuid = media.getMetadata().getUuid();
        // Save is ALWAYS a new UUID as media is immutable
        media.getMetadata().setUuid(UUID.randomUUID().toString());
        
        // Did we upload a new content?
        if (media.getContent() != null) {
            // Yes: Compute content Url
            media.getMetadata().setUrl(computeUrl(media));    
        } else {
            // No: Keep previous Url
            media.getMetadata().setUrl(get(originalUuid).getUrl());
        }
        
        // This is an update of a media. Delete previous and add new update
        mediaDao.delete(originalUuid);
        mediaDao.save(media);
        // Tell revision service we updated a media
        revisionService.add(Revision.Type.MEDIA, Revision.Action.UPDATE, originalUuid, media.getMetadata().getUuid());

    }

    @Override
    public void deleteByBundleTag(String bundleTag) {
        // Retrieve medias of bundle
        List<MediaMetadata> medias = mediaDao.getByBundle(bundleTag);
        // Remove all medias of bundle
        mediaDao.deleteByBundle(bundleTag);

        // Create revisions
        for (MediaMetadata media : medias) {
            revisionService.add(Revision.Type.MEDIA, Revision.Action.DELETE, media.getUuid());
        }

    }

    @Override
    public void delete(String uuid) {
        mediaDao.delete(uuid);
        // Create a new revision
        revisionService.add(Revision.Type.MEDIA, Revision.Action.DELETE, uuid);
    }

    /**
     * Compute file download url for media.
     *
     * @param media the media
     * @return the download url string
     */
    private String computeUrl(Media media) {
        if (media.getContent() == null) {
            return media.getMetadata().getUrl();
        }
        return new StringBuilder(props.getBaseUrl())
                .append("/file/")
                .append(UUID.randomUUID().toString())
                .append(FilenameUtils.getExtension(media.getMetadata().getMediaType().getExtension(media.getContent().getContentType())))
                .toString();
    }
}
