package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dao.RevisionDao;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Mickael on 08/06/2016.
 */
@Service
public class BundleServiceImpl implements BundleService {
    @Autowired
    private BundleDao bundleDao;

    @Autowired
    private RevisionDao revisionDao;

    @Autowired
    private BundleDtoMapperImpl mapper;

    @Override
    public BundleMetadata get(String uuid) {
        return bundleDao.get(uuid);
    }

    @Override
    public List<BundleMetadata> getAll() {
        return bundleDao.getAll();
    }

    @Override
    public BundleMetadata create(BundleMetadata bundle) {
        BundleMetadata result = bundleDao.save(bundle);
        if (bundleDao.save(bundle) != null) {
            // Create a new revision
            Revision revision = new Revision();
            revision.setRevision(revisionDao.getLatest() + 1);
            revision.setAction(Revision.Action.ADD);
            revision.setTarget(bundle.getUuid());
            revision.setType(Revision.Type.BUNDLE);
            revisionDao.save(revision);
        }

        return result;
    }

    @Override
    public BundleMetadata update(BundleMetadata bundle) {
        String oldUuid = bundle.getUuid();
        if (bundleDao.delete(bundle.getUuid())) {
            bundle.setUuid(null);
            bundle = bundleDao.save(bundle);
            // Create a new revision
            Revision revision = new Revision();
            revision.setRevision(revisionDao.getLatest() + 1);
            revision.setType(Revision.Type.BUNDLE);
            revision.setTarget(oldUuid);
            revision.setResult(bundle.getUuid());
            revision.setAction(Revision.Action.UPDATE);
            revisionDao.save(revision);
            return bundle;
        }
        return null;
    }

    @Override
    public boolean delete(String uuid) {
        boolean result = bundleDao.delete(uuid);
        if (result) {
            // Create a new revision
            Revision revision = new Revision();
            revision.setRevision(revisionDao.getLatest() + 1);
            revision.setAction(Revision.Action.DELETE);
            revision.setTarget(uuid);
            revision.setType(Revision.Type.BUNDLE);
            revisionDao.save(revision);
        }
        return result;
    }
}
