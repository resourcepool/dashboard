package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class BundleServiceImpl implements BundleService {

    @Autowired
    private BundleDao bundleDao;

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private MediaService mediaService;

    @Override
    public BundleMetadata getByTag(String tag) {
        return bundleDao.getByTag(tag);
    }

    @Override
    public List<BundleMetadata> getAll() {
        return bundleDao.getAll();
    }

    @Override
    public void save(BundleMetadata bundle) {
        if (getByTag(bundle.getTag()) != null) {
            throw new IllegalArgumentException("This tag already exists");
        }
        // Save is ALWAYS a new UUID as bundle is immutable
        bundle.setUuid(UUID.randomUUID().toString());
        // Save new bundle
        bundleDao.save(bundle);
        revisionService.add(Revision.Type.BUNDLE, Revision.Action.ADD, bundle.getUuid());
    }

    @Override
    public void update(BundleMetadata bundle) {
        String originalUuid = bundle.getUuid();
        BundleMetadata dbBundle = bundleDao.get(originalUuid);


        // Keep original bundle tag if not valid
        if (bundle.getTag() == null || bundle.getTag().trim().isEmpty()) {
            bundle.setTag(dbBundle.getTag());
        }
        
        // This is an update of a bundle. Delete previous and add new update
        bundleDao.delete(originalUuid);
        // Save is ALWAYS a new UUID as bundle is immutable
        bundle.setUuid(UUID.randomUUID().toString());
        bundleDao.save(bundle);
        
        // Did we change the tag?
        if (!dbBundle.getTag().equals(bundle.getTag())) {
            // If yes, we are in trouble. We need to:
            // Change all references to the bundle in medias
            // Move the folder in media entity
            // TODO
            List<MediaMetadata> medias = mediaService.getByBundleTag(dbBundle.getTag());
            medias.forEach(mediaMetadata -> {
                mediaMetadata.setBundleTag(bundle.getTag());
                mediaService.update(Media.builder().metadata(mediaMetadata).build());
            });
        }

        // Tell revision service we updated a bundle
        revisionService.add(Revision.Type.BUNDLE, Revision.Action.UPDATE, originalUuid, bundle.getUuid());

    }

    @Override
    public void delete(String tag) {

        // Step 1: delete all media related to bundle:
        mediaService.deleteByBundleTag(tag);
        // Step 2: delete bundle
        BundleMetadata dbBundle = bundleDao.delete(tag);
        // Create a new revision
        revisionService.add(Revision.Type.BUNDLE, Revision.Action.DELETE, dbBundle.getUuid());
        
    }

}