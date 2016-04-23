package com.excilys.shoofleurs.dashboard.service;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.excilys.shoofleurs.dashboard.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;
import com.excilys.shoofleurs.dashboard.rest.JsonRequest;
import com.excilys.shoofleurs.dashboard.rest.VolleySingleton;
import com.excilys.shoofleurs.dashboard.utils.Data;

import java.util.Arrays;

/**
 * This class performs all slides requests to the server.
 */
public class SlideShowService {
    private static SlideShowService S_INSTANCE;

    private DashboardActivity mDashboardActivity;

    public static SlideShowService getInstance(DashboardActivity mainActivity){
        if (S_INSTANCE == null) S_INSTANCE = new SlideShowService(mainActivity);
        return S_INSTANCE;
    }

    private SlideShowService(DashboardActivity mainActivity) {
        this.mDashboardActivity = mainActivity;
    }

    /**
     * This method checks if new slideshows are available to the server
     */
    public void checkUpdates() {
        @SuppressWarnings("unchecked")
        JsonRequest<SlideShow[]> request = new JsonRequest<>(Data.GET_SLIDESHOWS_URL, SlideShow[].class, null, new Response.Listener<SlideShow[]>() {
            @Override
            public void onResponse(SlideShow[] response) {
                Log.i(SlideShowService.class.getSimpleName(), "onResponse: "+ Arrays.asList(response));
                if (response.length != 0) {
                    mDashboardActivity.getSlideShowController().addSlideShows(response);
                }
                else {
                    Log.i(getClass().getSimpleName(), "checkUpdate onResponse: empty");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(SlideShowService.class.getSimpleName(), "checkUpdate onErrorResponse: "+error);
                if (error instanceof TimeoutError) {
                    Log.i(SlideShowService.class.getSimpleName(), "TimoutError: trying to resend the request");
                    checkUpdates();
                }
            }
        });
        executeRequest(request);
    }

    private void executeRequest(Request request) {
        VolleySingleton.getInstance(mDashboardActivity).addToRequestQueue(request);
    }

}
