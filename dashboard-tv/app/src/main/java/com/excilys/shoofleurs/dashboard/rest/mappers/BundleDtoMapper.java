package com.excilys.shoofleurs.dashboard.rest.mappers;

import com.excilys.shoofleurs.dashboard.model.entities.Bundle;
import com.excilys.shoofleurs.dashboard.rest.dtos.BundleDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by excilys on 09/06/16.
 */
public class BundleDtoMapper {

    public static Bundle toBundle(BundleDto bundleDto) {
        Bundle bundle = new Bundle();
        bundle.setName(bundleDto.getName());
        bundle.setRevision(bundleDto.getRevision());
        bundle.setUuid(bundleDto.getUuid());
        bundle.setValidity(ValidityDtoMapper.toValidity(bundleDto.getValidity()));
        return bundle;
    }

    public static List<Bundle> toBundles(List<BundleDto> bundleDtos) {
        if (bundleDtos == null) {
            return null;
        }

        List<Bundle> bundles = new ArrayList<>();
        for (BundleDto bundleDto : bundleDtos) {
            bundles.add(toBundle(bundleDto));
        }
        return bundles;
    }
}
