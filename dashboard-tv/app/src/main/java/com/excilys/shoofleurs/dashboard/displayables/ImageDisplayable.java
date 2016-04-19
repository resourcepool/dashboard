package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.excilys.shoofleurs.dashboard.singletons.VolleySingleton;

public class ImageDisplayable implements Displayable {
    private String mUrl;

    public ImageDisplayable(String mUrl) {
        this.mUrl = mUrl;
    }


    @Override
    public void displayContent(Context context, ViewGroup layout) {
        NetworkImageView networkImageView;
        /* Une NetworkImageView à déjà été attribuée au layout, on réutilise la même */
        if (layout.getChildCount() == 1) {
            networkImageView = (NetworkImageView) layout.getChildAt(0);
        }

        /* Aucune view n'a encore été affectée au layout, on créé la NetworkImageView */
        else {
            networkImageView = new NetworkImageView(context);
            ViewGroup.LayoutParams params =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            networkImageView.setAdjustViewBounds(true);
            networkImageView.setLayoutParams(params);
            layout.addView(networkImageView);
        }

        networkImageView.setImageUrl(mUrl, VolleySingleton.getInstance(context).getImageLoader());
    }
}

