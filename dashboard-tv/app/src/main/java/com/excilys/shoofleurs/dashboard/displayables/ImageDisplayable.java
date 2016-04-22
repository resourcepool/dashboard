package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.excilys.shoofleurs.dashboard.singletons.VolleySingleton;

public class ImageDisplayable extends AbstractDisplayable {
    private Handler mHandler;
    public ImageDisplayable(String url, int duration) {
        super(url, duration);
        mHandler = new Handler();
    }

    public ImageDisplayable(String url, int duration, OnCompletionListener listener) {
        super(url, duration, listener);
        mHandler = new Handler();
    }

    @Override
    public void displayContent(Context context, ViewGroup layout) {
        NetworkImageView networkImageView = addOrReplaceViewByType(layout, context, NetworkImageView.class);
        networkImageView.setImageUrl(mUrl, VolleySingleton.getInstance(context).getImageLoader());
        handleCompletion();
    }

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

