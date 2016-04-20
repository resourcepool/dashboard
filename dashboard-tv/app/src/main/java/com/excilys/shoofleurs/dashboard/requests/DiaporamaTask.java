package com.excilys.shoofleurs.dashboard.requests;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.excilys.shoofleurs.dashboard.model.entities.Diaporama;
import com.excilys.shoofleurs.dashboard.singletons.VolleySingleton;
import com.excilys.shoofleurs.dashboard.utils.Data;

import java.util.List;

/**
 * Created by tommy on 19/04/16.
 */
public class DiaporamaTask {
    public static void getDiaporamas(Context context) {
        JsonRequest<Diaporama[]> request = new JsonRequest<>(Data.GET_DIAPORAMAS_URL, Diaporama[].class, null, new Response.Listener<Diaporama[]>() {
            @Override
            public void onResponse(Diaporama[] response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}
