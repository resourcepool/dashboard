package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapper;
import com.excilys.shooflers.dashboard.server.exception.ResourceNotFoundException;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.validator.BundleMedataDtoValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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
    @ApiOperation(value = "List of BundleDto")
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
    @ApiOperation(value = "Persist a Bundle", notes = "Save a new Bundle and add a new revision")
    @ApiResponse(code = 201, message = "REUSSI")
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
    @ApiOperation(value = "Get a Bundle by his Tag", notes = "Get a particular Bundle by its tag")
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
    @ApiOperation(value = "Update a Bundle", code = 204)
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
    @ApiOperation(value = "Delete a bundle by its tags.")
    public void delete(@PathVariable("tag") String tag) {
        BundleMetadata result = bundleService.getByTag(tag);
        if (result == null) {
            throw new ResourceNotFoundException("Bundle not found");
        } else {
            bundleService.delete(result);
        }

    }
}
