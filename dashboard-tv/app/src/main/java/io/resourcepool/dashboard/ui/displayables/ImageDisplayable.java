package io.resourcepool.dashboard.ui.displayables;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

public class ImageDisplayable extends AbstractDisplayable {
    private static final String TAG = "ImageDisplayable";

    public ImageDisplayable(String url, int duration, OnCompletionListener listener) {
        super(url, duration, listener);
    }

    @Override
    public void display(final Context context, ViewGroup layout) {
        final SimpleDraweeView simpleDraweeView = addOrReplaceViewByType(layout, context, SimpleDraweeView.class);
        simpleDraweeView.setImageURI(Uri.parse(mUrl));
    }

    @Override
    public void start() {
        mStopDisplay = false;
        handleDelayedCompletion();
    }
}

