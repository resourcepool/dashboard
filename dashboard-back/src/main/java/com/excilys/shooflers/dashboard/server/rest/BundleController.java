package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequestMapping("/bundle")
public class BundleController {

    @Autowired
    private BundleService bundleService;

    @Autowired
    private RevisionService revisionService;

    /**
     * Get all Bundle.
     *
     * @return List of BundleDto
     */
    @RequestMapping(method = RequestMethod.GET)
    @RequireValidApiKey
    public List<BundleMetadataDto> getAll() {
        List<BundleMetadataDto> bundles = bundleService.getAll();
        bundles.forEach(b -> b.setRevision(revisionService.getLatest()));
        return bundles;
    }

    /**
     * Save a new Bundle and add a new revision.
     *
     * @param bundleMetadataDto Bundle to save
     */
    @RequireValidUser
    @RequestMapping(method = RequestMethod.POST)
    public BundleMetadataDto save(@RequestBody BundleMetadataDto bundleMetadataDto) {
        return bundleService.create(bundleMetadataDto);
    }

    /**
     * Get a particular Bundle by its uuid.
     *
     * @param uuid uuid to find
     * @return Bundle found if bundle exists
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    @RequireValidApiKey
    public BundleMetadataDto get(@PathVariable("uuid") String uuid) {
        return bundleService.get(uuid);
    }

    /**
     * Update a bundle
     *
     * @param bundle Bundle to update
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @RequireValidUser
    public BundleMetadataDto update(@RequestBody BundleMetadataDto bundle) {
        return bundleService.update(bundle);
    }

    /**
     * Delete a bundle by its uuid.
     *
     * @param uuid uuid to delete
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    @RequireValidUser
    public void delete(@PathVariable("uuid") String uuid) {
        bundleService.delete(uuid);
    }
}
