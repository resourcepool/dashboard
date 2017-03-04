package io.resourcepool.dashboard.rest.events;

import io.resourcepool.dashboard.model.entities.Bundle;


public class GetBundleResponseEvent {
    private Bundle mBundle;

    public GetBundleResponseEvent(Bundle bundle) {
        mBundle = bundle;
    }

    public Bundle getBundle() {
        return mBundle;
    }
}
