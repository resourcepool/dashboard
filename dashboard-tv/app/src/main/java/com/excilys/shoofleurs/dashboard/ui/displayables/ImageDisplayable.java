package com.excilys.shoofleurs.dashboard.ui.displayables;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.excilys.shoofleurs.dashboard.rest.VolleySingleton;



public class ImageDisplayable extends AbstractDisplayable {
    private boolean waitForNoImmediate;

    public ImageDisplayable(String url, int duration, OnCompletionListener listener) {
        super(url, duration, listener);
    }


    @Override
    public void display(final Context context, ViewGroup layout) {
        final ImageView imageView = addOrReplaceViewByType(layout, context, ImageView.class);
        imageView.setImageBitmap(null);
        VolleySingleton.getInstance(context).getImageLoader().get(mUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                /* isImmediate = true is the response from the cache
                 * The bitmap will be null the first time */
                if (isImmediate) {
                    if (response.getBitmap() == null) {
                        waitForNoImmediate = true;
                    }
                    else{
                        imageView.setImageBitmap(response.getBitmap());
                    }
                }

                /* If this is the first time, we wait for the request response (when isImmediate = false)*/
                else {
                    if (waitForNoImmediate) {
                        imageView.setImageBitmap(response.getBitmap());
                        waitForNoImmediate = false;
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(getClass().getSimpleName(), "onErrorResponse :"+error);
            }
        });
    }

    @Override
    public void start() {
        handleDelayedCompletion();
    }
}

