package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dao.RevisionDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequireValidUser
@RequestMapping("/bundle")
public class BundleController {

    @Autowired
    private BundleDao bundleDao;

    @Autowired
    private BundleDtoMapperImpl mapper;

    @Autowired
    private RevisionDao revisionDao;

    /**
     * Get all Bundle.
     *
     * @return List of BundleDto
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<BundleMetadataDto> getAll() {
        List<BundleMetadataDto> bundles = mapper.toListDto(bundleDao.getAll());
        bundles.forEach(b -> b.setRevision(revisionDao.getLatest()));
        return bundles;
    }

    /**
     * Save a new Bundle and add a new revision.
     *
     * @param bundle Bundle to save
     */
    @RequestMapping(method = RequestMethod.POST)
    public BundleMetadataDto save(@RequestBody BundleMetadataDto bundle) {
        bundle = mapper.toDto(bundleDao.save(mapper.fromDto(bundle)));
        // Create a new revision
        Revision revision = new Revision();
        revision.setRevision(revisionDao.getLatest() + 1);
        revision.setAction(Revision.Action.ADD);
        revision.setTarget(bundle.getUuid());
        revision.setType(Revision.Type.BUNDLE);
        revisionDao.save(revision);
        return bundle;
    }

    /**
     * Get a particular Bundle by its uuid.
     *
     * @param uuid uuid to find
     * @return Bundle found if bundle exists
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    public BundleMetadataDto get(@PathVariable("uuid") String uuid) {
        return mapper.toDto(bundleDao.get(uuid));
    }

    /**
     * Update a bundle
     *
     * @param bundle Bundle to update
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public BundleMetadataDto update(@RequestBody BundleMetadataDto bundle) {
        String oldUuid = bundle.getUuid();
        if (bundleDao.delete(bundle.getUuid())) {
            bundle.setUuid(null);
            bundle = mapper.toDto(bundleDao.save(mapper.fromDto(bundle)));
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

    /**
     * Delete a bundle by its uuid.
     *
     * @param uuid uuid to delete
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uuid") String uuid) {
        if (bundleDao.delete(uuid)) {
            // Create a new revision
            Revision revision = new Revision();
            revision.setRevision(revisionDao.getLatest() + 1);
            revision.setAction(Revision.Action.DELETE);
            revision.setTarget(uuid);
            revision.setType(Revision.Type.BUNDLE);
            revisionDao.save(revision);
        }
    }
}
