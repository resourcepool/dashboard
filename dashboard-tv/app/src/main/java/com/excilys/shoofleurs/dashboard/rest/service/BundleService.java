package com.excilys.shoofleurs.dashboard.rest.service;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.rest.dtos.BundleDto;
import com.excilys.shoofleurs.dashboard.rest.dtos.MediaDto;
import com.excilys.shoofleurs.dashboard.rest.dtos.mappers.BundleDtoMapper;
import com.excilys.shoofleurs.dashboard.rest.dtos.mappers.MediaDtoMapper;
import com.excilys.shoofleurs.dashboard.rest.events.GetBundleResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.events.GetMediaResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.service.api.BundleApi;

import org.greenrobot.eventbus.EventBus;

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
        mBundleApi = ServiceGenerator.createService(BundleApi.class);
    }

    public void checkRevision() {

    }

    public void getBundles() {
        Call<List<BundleDto>> call = mBundleApi.getBundles();
        call.enqueue(new Callback<List<BundleDto>>() {
            @Override
            public void onResponse(Call<List<BundleDto>> call, Response<List<BundleDto>> response) {
                if (response.isSuccessful()) {
                    mEventBus.post(new GetBundleResponseEvent(BundleDtoMapper.toBundles(response.body())));
                } else {
                    Log.e(TAG, "getBundles: Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BundleDto>> call, Throwable t) {
                Log.e(TAG, "getBundles onFailure: " + t.toString());
            }
        });
    }

    public void getMedias(String bundleUuid) {
        Call<List<MediaDto>> call = mBundleApi.getMedias(bundleUuid);

        call.enqueue(new Callback<List<MediaDto>>() {
            @Override
            public void onResponse(Call<List<MediaDto>> call, Response<List<MediaDto>> response) {
                if (response.isSuccessful()) {
                    mEventBus.post(new GetMediaResponseEvent(MediaDtoMapper.toMedias(response.body())));
                } else {
                    Log.e(TAG, "onResponse: Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<MediaDto>> call, Throwable t) {
                Log.e(TAG, "getMedias onFailure: " + t.toString());
            }
        });
    }
}
