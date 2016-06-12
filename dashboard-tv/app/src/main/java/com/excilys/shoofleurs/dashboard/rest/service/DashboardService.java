package com.excilys.shoofleurs.dashboard.rest.service;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.rest.events.GetRevisionResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.service.api.DashboardApi;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class performs all slides requests to the server.
 */
public class DashboardService {
    private static final String TAG = "BundleService";
    private DashboardApi mDashboardApi;
    private EventBus mEventBus;

    public DashboardService(EventBus eventBus) {
        this.mEventBus = eventBus;
        mDashboardApi = ServiceGenerator.createService(DashboardApi.class);
    }

    public void getRevision() {
        Call<Long> call = mDashboardApi.getRevision();
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Log.i(DashboardService.class.getSimpleName(), "onResponse: " + response.body());
                    mEventBus.post(new GetRevisionResponseEvent(response.body()));
                } else {
                    Log.e(TAG, "onResponse Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e(TAG, "onFailure: getRevision", t);
            }
        });
    }

}
