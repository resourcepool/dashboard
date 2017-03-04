package io.resourcepool.dashboard.rest.service;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import io.resourcepool.dashboard.database.dao.BundleDao;
import io.resourcepool.dashboard.database.dao.MediaDao;
import io.resourcepool.dashboard.model.entities.Bundle;
import io.resourcepool.dashboard.model.entities.Media;
import io.resourcepool.dashboard.rest.dtos.BundleDto;
import io.resourcepool.dashboard.rest.dtos.MediaDto;
import io.resourcepool.dashboard.rest.dtos.mappers.BundleDtoMapper;
import io.resourcepool.dashboard.rest.dtos.mappers.MediaDtoMapper;
import io.resourcepool.dashboard.rest.events.GetBundleResponseEvent;
import io.resourcepool.dashboard.rest.events.GetMediaResponseEvent;
import io.resourcepool.dashboard.rest.service.api.BundleApi;
import io.resourcepool.dashboard.ui.DashboardApplication;
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
    private boolean initialized;
    private MediaDao mMediaDao;
    private BundleDao mBundleDao;

    public BundleService(DashboardApplication application, EventBus eventBus) {
        this.mEventBus = eventBus;
        this.mMediaDao = application.getDatabaseController().getMediaDao();
        this.mBundleDao = application.getDatabaseController().getBundleDao();
    }

    public void initialize(String baseUrl) {
        mBundleApi = ServiceGenerator.createService(baseUrl, BundleApi.class);
        initialized = true;
    }

    public void fetchBundle(String tag) {
        if (!initialized) {
            throw new IllegalStateException("Service is not initialized");
        }
        Call<BundleDto> call = mBundleApi.getBundle(tag);
        call.enqueue(new Callback<BundleDto>() {
            @Override
            public void onResponse(Call<BundleDto> call, Response<BundleDto> response) {
                if (response.isSuccessful()) {
                    Bundle bundle = BundleDtoMapper.toBundle(response.body());
                    mBundleDao.save(bundle);
                    mEventBus.post(new GetBundleResponseEvent(bundle));
                } else {
                    Log.e(TAG, "fetchBundle: Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BundleDto> call, Throwable t) {
                Log.e(TAG, "fetchBundle onFailure: " + t.toString());
            }
        });
    }

    public void fetchMedia(String mediaUuid) {
        if (!initialized) {
            throw new IllegalStateException("Service is not initialized");
        }
        Call<MediaDto> call = mBundleApi.getMedia(mediaUuid);

        call.enqueue(new Callback<MediaDto>() {
            @Override
            public void onResponse(Call<MediaDto> call, Response<MediaDto> response) {
                if (response.isSuccessful()) {
                    Media media = MediaDtoMapper.toMedia(response.body());
                    mMediaDao.save(media);
                    mEventBus.post(new GetMediaResponseEvent(media));
                } else {
                    Log.e(TAG, "onResponse: Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MediaDto> call, Throwable t) {
                Log.e(TAG, "getMedias onFailure: " + t.toString());
            }
        });
    }
}
