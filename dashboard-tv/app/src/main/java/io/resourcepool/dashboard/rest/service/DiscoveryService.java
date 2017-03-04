package io.resourcepool.dashboard.rest.service;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import io.resourcepool.dashboard.rest.dtos.Device;
import io.resourcepool.dashboard.rest.events.RegisterDeviceResponseEvent;
import io.resourcepool.dashboard.rest.service.api.DiscoveryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class performs all discovery requests to the server
 */
public class DiscoveryService {

    private static final String TAG = "DiscoveryService";
    private DiscoveryApi mDiscoveryApi;
    private EventBus mEventBus;
    private Context mCtx;
    private boolean initialized;

    public DiscoveryService(Context ctx, EventBus eventBus) {
        this.mCtx = ctx;
        this.mEventBus = eventBus;

    }

    public void initialize(String baseUrl) {
        this.mDiscoveryApi = ServiceGenerator.createService(baseUrl, DiscoveryApi.class);
        this.initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void registerDevice() {
        if (!initialized) {
            throw new IllegalStateException("Service must be initialized before calling its methods");
        }
        Call<Device> call = mDiscoveryApi.registerDevice(Settings.Secure.getString(mCtx.getContentResolver(), Settings.Secure.ANDROID_ID));
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.isSuccessful()) {
                    mEventBus.post(new RegisterDeviceResponseEvent(response.body()));
                } else {
                    Log.e(TAG, "onResponse: Error " + response.code() + ": " + response.message());
                    mEventBus.post(new RegisterDeviceResponseEvent(null));
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                mEventBus.post(new RegisterDeviceResponseEvent(null));
            }
        });
    }

}
