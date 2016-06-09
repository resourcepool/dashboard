package com.excilys.shoofleurs.dashboard.rest.service;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.rest.dtos.BundleDto;
import com.excilys.shoofleurs.dashboard.rest.events.BundleUpdatesEvent;
import com.excilys.shoofleurs.dashboard.rest.events.BundleUpdatesResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.dtos.mappers.BundleDtoMapper;
import com.excilys.shoofleurs.dashboard.ui.event.SetDebugMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class performs all slides requests to the server.
 */
public class BundleService {
    private static final String TAG = "BundleService";
    private BundleApi mBundleApi;
    private EventBus mEventBus;

    public BundleService(EventBus eventBus) {
        this.mEventBus = eventBus;
        mEventBus.register(this);
        mBundleApi = ServiceGenerator.createService(BundleApi.class);
    }

    /**
     * This method checks if new Bundles are available to the server
     */
    @Subscribe
    public void onBundleUpdatesEvent(BundleUpdatesEvent bundleUpdatesEvent) {
        mEventBus.post(new SetDebugMessageEvent(R.string.debug_check_updates));

        Call<List<BundleDto>> call = mBundleApi.getBundles();

        call.enqueue(new Callback<List<BundleDto>>() {
            @Override
            public void onResponse(Call<List<BundleDto>> call, Response<List<BundleDto>> response) {
                if (response.isSuccessful()) {
                    mEventBus.post(new BundleUpdatesResponseEvent(BundleDtoMapper.toBundles(response.body())));
                } else {
                    Log.e(TAG, "getBundles: Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BundleDto>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
