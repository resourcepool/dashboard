package com.excilys.shoofleurs.dashboard.managers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.excilys.shoofleurs.dashboard.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.model.entities.Diaporama;
import com.excilys.shoofleurs.dashboard.requests.JsonRequest;
import com.excilys.shoofleurs.dashboard.singletons.VolleySingleton;
import com.excilys.shoofleurs.dashboard.utils.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tommy on 19/04/16.
 */
public class DiaporamaService {
    private static DiaporamaService S_INSTANCE;

    private DashboardActivity mDashboardActivity;

    public static DiaporamaService getInstance(DashboardActivity mainActivity){
        if (S_INSTANCE == null) S_INSTANCE = new DiaporamaService(mainActivity);
        return S_INSTANCE;
    }

    private DiaporamaService(DashboardActivity mainActivity) {
        this.mDashboardActivity = mainActivity;
    }

    /**
     * Cette fonction permet de checker auprès du serveur si de nouveaux diaporamas
     * sont disponibles. Si c'est le cas, ils sont ajoutés à la liste de diaporamas.
     */
    public void checkUpdates() {
        @SuppressWarnings("unchecked")
        JsonRequest<Diaporama[]> request = new JsonRequest<>(Data.GET_DIAPORAMAS_URL, Diaporama[].class, null, new Response.Listener<Diaporama[]>() {
            @Override
            public void onResponse(Diaporama[] response) {
                Log.i(DiaporamaService.class.getSimpleName(), "onResponse: "+ Arrays.asList(response));
                if (response.length != 0) {
                    mDashboardActivity.getDiaporamaController().addAllDiaporamas(response);
                }
                else {
                    Log.i(getClass().getSimpleName(), "checkUpdate onResponse: empty");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(DiaporamaService.class.getSimpleName(), "checkUpdate onErrorResponse: "+error);
            }
        });
        executeRequest(request);
    }

    private void executeRequest(Request request) {
        VolleySingleton.getInstance(mDashboardActivity).addToRequestQueue(request);
    }

}
