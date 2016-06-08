package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;


@Service
public class BundleServiceImpl implements BundleService {

    @Autowired
    private BundleDao bundleDao;

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private BundleDtoMapperImpl mapper;

    @Autowired
    private DashboardProperties props;

    @Override
    public BundleMetadataDto get(String uuid) {
        return mapper.toDto(bundleDao.get(uuid));
    }

    @Override
    public List<BundleMetadataDto> getAll() {
        return mapper.toListDto(bundleDao.getAll());
    }

    @Override
    public BundleMetadataDto create(BundleMetadataDto bundle) {
        bundle = mapper.toDto(bundleDao.save(mapper.fromDto(bundle)));
        if (bundle != null) {
            // Create a new revision
            bundle.setRevision(revisionService.add(Revision.Action.ADD, bundle.getUuid(), Revision.Type.BUNDLE, null).getRevision());
        }

        return bundle;
    }

    @Override
    public BundleMetadataDto update(BundleMetadataDto bundle) {
        String oldUuid = bundle.getUuid();
        if (bundleDao.delete(bundle.getUuid())) {
            bundle.setUuid(null);
            bundle = mapper.toDto(bundleDao.save(mapper.fromDto(bundle)));
            // Rename media foler
            new File(props.getBasePath() + "/media/" + oldUuid).renameTo(new File(props.getBasePath() + "/media/" + bundle.getUuid()));
            new File(props.getBaseResources() + "/" + oldUuid).renameTo(new File(props.getBaseResources() + "/" + bundle.getUuid()));
            // Create a new revision
            bundle.setRevision(revisionService.add(Revision.Action.UPDATE, oldUuid, Revision.Type.BUNDLE, bundle.getUuid()).getRevision());
            return bundle;
        }
        return null;
    }

    @Override
    public boolean delete(String uuid) {
        boolean result = bundleDao.delete(uuid);
        if (result) {
            // Create a new revision
            revisionService.add(Revision.Action.DELETE, uuid, Revision.Type.BUNDLE, null);

            // Delete all medias associated with bundle
            File dir = new File(props.getBasePath() + "/media/" + uuid);
            File[] dirFiles = dir.listFiles();
            if (dirFiles != null) {
                Arrays.asList(dirFiles).forEach(File::delete);
            }
            dir.delete();

            // Delete all files associated with bundle
            dir = new File(props.getBaseResources() + "/" + uuid);
            dirFiles = dir.listFiles();
            if (dirFiles != null) {
                Arrays.asList(dirFiles).forEach(File::delete);
            }
            dir.delete();
        }
        return result;
    }
}