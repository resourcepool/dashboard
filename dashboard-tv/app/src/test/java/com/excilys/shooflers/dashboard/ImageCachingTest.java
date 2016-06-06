package com.excilys.shooflers.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.excilys.shoofleurs.dashboard.rest.VolleySingleton;

/**
 * Created by tommy on 23/04/16.
 */
public class ImageCachingTest extends InstrumentationTestCase {
    Context context;

    public void setUp() throws Exception {
        super.setUp();
        context = new MockContext();
        assertNotNull(context);
    }

    public void testImageCaching() {
        final String url = "http://vps229493.ovh.net:8080/dashboard/img/7.jpg";

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                System.out.println("Cached : " + VolleySingleton.getInstance(context).getImageLoader().isCached(url, 0, 0));
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(imageRequest);
    }
}
