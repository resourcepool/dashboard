package excilys.dashboardadministrator.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.Arrays;
import java.util.List;

import excilys.dashboardadministrator.model.entities.SlideShow;
import excilys.dashboardadministrator.rest.JsonRequest;
import excilys.dashboardadministrator.rest.VolleySingleton;
import excilys.dashboardadministrator.utils.Data;

/**
 * This class performs all slides requests to the server.
 */
public class SlideShowService {
    private static SlideShowService S_INSTANCE;

    private Context mContext;

    public static SlideShowService getInstance(Context context){
        if (S_INSTANCE == null) S_INSTANCE = new SlideShowService(context);
        return S_INSTANCE;
    }

    private SlideShowService(Context context) {
        this.mContext = context;
    }

    /**
     * This method checks if new slideshows are available to the server
     */
    public void checkUpdates(final OnSlideShowServiceResponse listener) {
        @SuppressWarnings("unchecked")
        JsonRequest<SlideShow[]> request = new JsonRequest<>(Data.GET_SLIDESHOWS_URL, SlideShow[].class, null, new Response.Listener<SlideShow[]>() {
            @Override
            public void onResponse(SlideShow[] response) {
                Log.i(SlideShowService.class.getSimpleName(), "onResponse: "+ Arrays.asList(response));
                if (response.length != 0) {
                    listener.onCheckUpdatesResponse(Arrays.asList(response));
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
                    checkUpdates(listener);
                }
            }
        });

        executeRequest(request);
    }

    private void executeRequest(Request request) {
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    public interface OnSlideShowServiceResponse {
        void onCheckUpdatesResponse(List<SlideShow> slideShows);
    }

}
