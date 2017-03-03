package io.resourcepool.dashboard.rest;

import io.resourcepool.dashboard.dto.BundleMetadataDto;
import io.resourcepool.dashboard.dto.mapper.BundleDtoMapper;
import io.resourcepool.dashboard.exception.ResourceNotFoundException;
import io.resourcepool.dashboard.model.metadata.BundleMetadata;
import io.resourcepool.dashboard.security.annotation.RequireValidApiKey;
import io.resourcepool.dashboard.security.annotation.RequireValidUser;
import io.resourcepool.dashboard.service.BundleService;
import io.resourcepool.dashboard.validator.BundleMedataDtoValidator;
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
    private BundleMedataDtoValidator validator;

    @Autowired
    private BundleDtoMapper mapper;

    /**
     * Get all Bundle.
     *
     * @return List of BundleDto
     */
    @RequestMapping(method = RequestMethod.GET)
    @RequireValidApiKey
    public List<BundleMetadataDto> getAll() {
        List<BundleMetadataDto> bundles = mapper.toListDto(bundleService.getAll());
        return bundles;
    }

    /**
     * Save a new Bundle and add a new revision.
     *
     * @param bundleMetadataDto Bundle to save
     */
    @RequestMapping(method = RequestMethod.POST)
    public BundleMetadataDto save(@RequestBody BundleMetadataDto bundleMetadataDto) {
        validator.validate(bundleMetadataDto);
        BundleMetadata bundle = mapper.fromDto(bundleMetadataDto);
        bundleService.save(bundle);
        return mapper.toDto(bundle);
    }

    /**
     * Get a particular Bundle by its tag.
     *
     * @param tag tag to find
     * @return Bundle found if bundle exists
     */
    @RequestMapping(value = "{tag}", method = RequestMethod.GET)
    @RequireValidApiKey
    public BundleMetadataDto get(@PathVariable("tag") String tag) {
        BundleMetadata result = bundleService.getByTag(tag);
        if (result == null) {
            throw new ResourceNotFoundException("Bound not found");
        } else {
            return mapper.toDto(result);
        }
    }

    /**
     * Update a bundle
     *
     * @param bundleMetadataDto Bundle to update
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public BundleMetadataDto update(@RequestBody BundleMetadataDto bundleMetadataDto) {
        validator.validate(bundleMetadataDto);

        if (bundleMetadataDto.getUuid() == null) {
            throw new IllegalArgumentException("A valid uuid is required to edit a bundle.");
        }
        BundleMetadata bundle = mapper.fromDto(bundleMetadataDto);

        bundleService.update(bundle);

        return mapper.toDto(bundle);
    }

    /**
     * Delete a bundle by its tag.
     *
     * @param tag tag to delete
     */
    @RequestMapping(value = "{tag}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("tag") String tag) {
        BundleMetadata result = bundleService.getByTag(tag);
        if (result == null) {
            throw new ResourceNotFoundException("Bound not found");
        } else {
            bundleService.delete(result);
        }

    }
}
