package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.excilys.shoofleurs.dashboard.rest.VolleySingleton;

public class ImageDisplayable extends AbstractDisplayable {
    private Handler mHandler;
    private boolean waitForNoImmediate;

    public ImageDisplayable(String url, int duration) {
        super(url, duration);
        mHandler = new Handler();
    }

    public ImageDisplayable(String url, int duration, OnCompletionListener listener) {
        super(url, duration, listener);
        mHandler = new Handler();
    }


    @Override
    public void displayContent(final Context context, ViewGroup layout) {
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
                        handleCompletion();
                    }
                }

                /* If this is the first time, we wait for the request response (when isImmediate = false)*/
                else {
                    if (waitForNoImmediate) {
                        imageView.setImageBitmap(response.getBitmap());
                        handleCompletion();
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

    /**
     * Handle the onCompletion method when the duration delay is reached.
     */
    private void handleCompletion() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCompletionListener != null) {
                    mCompletionListener.onCompletion();
                }
            }
        }, mDurationInSec*1000);
    }
}

