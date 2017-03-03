package io.resourcepool.dashboard.rest.dtos.mappers;

import io.resourcepool.dashboard.model.entities.Bundle;
import io.resourcepool.dashboard.rest.dtos.BundleDto;

import java.util.ArrayList;
import java.util.List;


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
