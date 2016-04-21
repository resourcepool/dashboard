package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.excilys.shoofleurs.dashboard.singletons.VolleySingleton;

import java.lang.reflect.InvocationTargetException;

public class ImageDisplayable extends AbstractDisplayable {
    public ImageDisplayable(String url, int duration) {
        super(url, duration);
    }

    @Override
    public int displayContent(Context context, ViewGroup layout) {
        NetworkImageView networkImageView = addOrReplaceViewByType(layout, context, NetworkImageView.class);
        networkImageView.setImageUrl(mUrl, VolleySingleton.getInstance(context).getImageLoader());

        return getDurationInSec();
    }
}

