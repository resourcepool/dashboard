package com.excilys.shoofleurs.dashboard.managers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.excilys.shoofleurs.dashboard.activities.MainActivity;
import com.excilys.shoofleurs.dashboard.model.entities.Diaporama;
import com.excilys.shoofleurs.dashboard.requests.JsonRequest;
import com.excilys.shoofleurs.dashboard.singletons.VolleySingleton;
import com.excilys.shoofleurs.dashboard.utils.Data;

import java.util.Collections;
import java.util.List;

/**
 * Created by tommy on 19/04/16.
 */
public class DiaporamaManager {
    private static DiaporamaManager S_INSTANCE;

    private MainActivity mMainActivity;

    private List<Diaporama> mDiaporamas;

    public static DiaporamaManager getInstance(MainActivity mainActivity){
        if (S_INSTANCE == null) S_INSTANCE = new DiaporamaManager(mainActivity);
        return S_INSTANCE;
    }

    private DiaporamaManager (MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    /**
     * Cette fonction permet de checker auprès du serveur si de nouveaux diaporamas
     * sont disponibles. Si c'est le cas, ils sont ajoutés à la liste de diaporamas.
     */
    public void checkUpdates() {
        JsonRequest<Diaporama[]> request = new JsonRequest<>(Data.GET_DIAPORAMAS_URL, Diaporama[].class, null, new Response.Listener<Diaporama[]>() {
            @Override
            public void onResponse(Diaporama[] response) {
                if (response.length != 0) {
                    Collections.addAll(mDiaporamas, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        executeRequest(request);
    }

    private void executeRequest(Request request) {
        VolleySingleton.getInstance(mMainActivity).addToRequestQueue(request);
    }
}
