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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tommy on 19/04/16.
 */
public class DiaporamaManager {
    private static DiaporamaManager S_INSTANCE;

    private DashboardActivity mDashboardActivity;

    private List<Diaporama> mDiaporamasList;

    public static DiaporamaManager getInstance(DashboardActivity mainActivity){
        if (S_INSTANCE == null) S_INSTANCE = new DiaporamaManager(mainActivity);
        return S_INSTANCE;
    }

    private DiaporamaManager (DashboardActivity mainActivity) {
        this.mDashboardActivity = mainActivity;
        mDiaporamasList = new ArrayList<>();
    }

    /**
     * Cette fonction permet de checker auprès du serveur si de nouveaux diaporamas
     * sont disponibles. Si c'est le cas, ils sont ajoutés à la liste de diaporamas.
     */
    public void checkUpdates() {
        @SuppressWarnings("unchecked")
        Class<List<Diaporama>> cls = (Class<List<Diaporama>>)(Object)List.class;
        JsonRequest<List> request = new JsonRequest<>(Data.GET_DIAPORAMAS_URL, List.class, null, new Response.Listener<List>() {
            @Override
            public void onResponse(List response) {
                Log.i(DiaporamaManager.class.getSimpleName(), "onResponse: "+ Arrays.asList(response));
                if (response.size() != 0) {
                    mDiaporamasList.addAll(response);
//                    mDiaporamasList.addAll(response);
                    refreshCurrentDiaporama();
                }
                else {
                    Log.i(getClass().getSimpleName(), "checkUpdate onResponse: empty");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(DiaporamaManager.class.getSimpleName(), "checkUpdate onErrorResponse: "+error);
            }
        });
        executeRequest(request);
    }

    public void refreshCurrentDiaporama() {
        if (!mDiaporamasList.isEmpty()) {
            mDashboardActivity.setCurrentDiaporama(mDiaporamasList.get(0));
        }
    }
    
    private void executeRequest(Request request) {
        VolleySingleton.getInstance(mDashboardActivity).addToRequestQueue(request);
    }

}
