package excilys.dashboardadministrator.rest.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.TimeoutError;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.List;

import excilys.dashboardadministrator.model.entities.SlideShow;
import excilys.dashboardadministrator.model.json.ServerResponse;
import excilys.dashboardadministrator.rest.SlideShowApi;
import excilys.dashboardadministrator.utils.JsonMapperUtils;
import excilys.dashboardadministrator.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * This class performs all slides requests to the server.
 */
public class SlideShowService {
    private static SlideShowService S_INSTANCE;

    private Context mContext;

    private SlideShowApi mSlideShowApi;

    public static SlideShowService getInstance(Context context){
        if (S_INSTANCE == null) S_INSTANCE = new SlideShowService(context);
        return S_INSTANCE;
    }

    private SlideShowService(Context context) {
        this.mContext = context;
        mSlideShowApi = ServiceGenerator.createService(SlideShowApi.class);
    }

    /**
     * This method checks if new slideshows are available to the server
     */
    public void checkUpdates(final OnSlideShowServiceResponse listener) {

        Call<ServerResponse> call = mSlideShowApi.getSlideShows(SlideShowApi.TYPE_TV);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    List<SlideShow> slideShows = JsonMapperUtils.getServerResponseContent(serverResponse, new TypeReference<List<SlideShow>>() {});
                    Log.i(SlideShowService.class.getSimpleName(), "onResponse: "+ Arrays.asList(response));
                    if (slideShows.size() != 0) {
                        listener.onCheckUpdatesResponse(slideShows);
                    }
                    else {
                        Log.i(getClass().getSimpleName(), "checkUpdate onResponse: empty");
                    }

                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(SlideShowService.class.getSimpleName(), "checkUpdate onErrorResponse: "+ t.toString());
                if (t instanceof TimeoutError) {
                    Log.i(SlideShowService.class.getSimpleName(), "TimoutError: trying to resend the request");
                    checkUpdates(listener);
                }
            }
        });
    }

    public interface OnSlideShowServiceResponse {
        void onCheckUpdatesResponse(List<SlideShow> slideShows);
    }
}
