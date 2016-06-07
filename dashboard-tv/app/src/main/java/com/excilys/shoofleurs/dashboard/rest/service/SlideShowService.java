package com.excilys.shoofleurs.dashboard.rest.service;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;
import com.excilys.shoofleurs.dashboard.model.json.ServerResponse;
import com.excilys.shoofleurs.dashboard.rest.json.JsonMapperUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class performs all slides requests to the server.
 */
public class SlideShowService {
    private static final String TAG = "SlideShowService";
    private SlideShowApi mSlideShowApi;

    private OnMessageServiceListener mMessageServiceListener;
    private OnDebugMessageListener mDebugMessageListener;

    public SlideShowService() {
        mSlideShowApi = ServiceGenerator.createService(SlideShowApi.class);
    }

    /**
     * This method checks if new slideshows are available to the server
     */
    public void checkUpdates() {
        if (mDebugMessageListener != null) {
            mDebugMessageListener.onDebugMessage(R.string.debug_check_updates);
        }

        Call<ServerResponse> call = mSlideShowApi.getSlideShows(SlideShowApi.TYPE_TV);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    List<SlideShow> slideShows = JsonMapperUtils.getServerResponseContent(serverResponse, new TypeReference<List<SlideShow>>() {
                    });
                    Log.i(TAG, "onResponse: " + slideShows);
                    if (slideShows.size() != 0) {
                        if (mMessageServiceListener != null) {
                            mMessageServiceListener.onCheckUpdatesResponse(slideShows);
                        }
                    } else {
                        Log.i(getClass().getSimpleName(), "checkUpdate onResponse: empty");
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(SlideShowService.class.getSimpleName(), "checkUpdate onFailure: " + t.toString());

//                if (t instanceof TimeoutError) {
//                    Log.i(TAG, "TimoutError: trying to resend the request");
//                    checkUpdates();
//                } else if (t instanceof NoConnectionError) {
//                    if (mDebugMessageListener != null) {
//                        mDebugMessageListener.onDebugMessage(R.string.debug_no_connection_error);
//                    }
//                }
            }
        });
    }

    public void setMessageServiceListener(OnMessageServiceListener mMessageServiceListener) {
        this.mMessageServiceListener = mMessageServiceListener;
    }

    public void setDebugMessageListener(OnDebugMessageListener mDebugMessageListener) {
        this.mDebugMessageListener = mDebugMessageListener;
    }

    public interface OnMessageServiceListener {
        void onCheckUpdatesResponse(List<SlideShow> slideShows);
    }

    public interface OnDebugMessageListener {
        void onDebugMessage(int messageId);
    }
}
