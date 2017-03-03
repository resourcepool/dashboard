package io.resourcepool.dashboard.rest.events;

import io.resourcepool.dashboard.model.entities.Bundle;

import java.util.List;


public class GetBundleResponseEvent {
    private List<Bundle> mBundles;

    public GetBundleResponseEvent(List<Bundle> bundles) {
        mBundles = bundles;
    }

    public List<Bundle> getBundles() {
        return mBundles;
    }
}
