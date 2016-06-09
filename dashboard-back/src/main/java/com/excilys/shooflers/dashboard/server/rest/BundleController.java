package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import com.excilys.shooflers.dashboard.server.validator.BundleMedataDtoValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private RevisionService revisionService;

    @Autowired
    private BundleMedataDtoValidatorImpl bundleMedataDtoValidator;

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
    @RequestMapping(method = RequestMethod.POST)
    public BundleMetadataDto save(@RequestBody BundleMetadataDto bundleMetadataDto) {
        bundleMetadataDto.setUuid(null);
        bundleMedataDtoValidator.validate(bundleMetadataDto);
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
    public ResponseEntity<?> get(@PathVariable("uuid") String uuid) {
        BundleMetadataDto result = bundleService.get(uuid);
        if (result == null) {
            return new ResponseEntity<>("Bound not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * Update a bundle
     *
     * @param bundle Bundle to update
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public BundleMetadataDto update(@RequestBody BundleMetadataDto bundle) {
        if (bundleService.get(bundle.getUuid()) == null) {
            throw new IllegalArgumentException("An UUID is need to edit an bundle.");
        }
        return bundleService.update(bundle);
    }

    /**
     * Delete a bundle by its uuid.
     *
     * @param uuid uuid to delete
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("uuid") String uuid) {
        if (bundleService.delete(uuid)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
