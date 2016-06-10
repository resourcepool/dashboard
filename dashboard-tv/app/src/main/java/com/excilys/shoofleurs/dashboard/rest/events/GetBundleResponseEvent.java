package com.excilys.shoofleurs.dashboard.rest.events;

import com.excilys.shoofleurs.dashboard.model.entities.Bundle;

import java.util.List;

/**
 * Created by excilys on 07/06/16.
 */
public class GetBundleResponseEvent {
    private List<Bundle> mBundles;

    public GetBundleResponseEvent(List<Bundle> bundles) {
        mBundles = bundles;
    }

    public List<Bundle> getBundles() {
        return mBundles;
    }
}
