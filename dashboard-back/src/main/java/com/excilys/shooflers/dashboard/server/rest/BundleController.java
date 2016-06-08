package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dao.RevisionDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.BundleService;
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
    private BundleService bundleService;

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
        List<BundleMetadataDto> bundles = mapper.toListDto(bundleService.getAll());
        bundles.forEach(b -> b.setRevision(revisionDao.getLatest()));
        return bundles;
    }

    /**
     * Save a new Bundle and add a new revision.
     *
     * @param bundleMetadataDto Bundle to save
     */
    @RequestMapping(method = RequestMethod.POST)
    public BundleMetadataDto save(@RequestBody BundleMetadataDto bundleMetadataDto) {
        return mapper.toDto(bundleService.create(mapper.fromDto(bundleMetadataDto)));
    }

    /**
     * Get a particular Bundle by its uuid.
     *
     * @param uuid uuid to find
     * @return Bundle found if bundle exists
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    public BundleMetadataDto get(@PathVariable("uuid") String uuid) {
        return mapper.toDto(bundleService.get(uuid));
    }

    /**
     * Update a bundle
     *
     * @param bundle Bundle to update
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public BundleMetadataDto update(@RequestBody BundleMetadataDto bundle) {
        return mapper.toDto(bundleService.update(mapper.fromDto(bundle)));
    }

    /**
     * Delete a bundle by its uuid.
     *
     * @param uuid uuid to delete
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uuid") String uuid) {
        bundleService.delete(uuid);
    }
}
