package com.excilys.shoofleurs.dashboard.ui.managers;

import android.content.Context;

import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.ImageContent;
import com.excilys.shoofleurs.dashboard.rest.VolleySingleton;


public class ContentCacheManager {
    public static void cacheContent(Context context, AbstractContent content) {
        if (content instanceof ImageContent) {
            VolleySingleton volleySingleton = VolleySingleton.getInstance(context);
            if (!volleySingleton.getImageLoader().isCached(content.getUrl(), 0, 0)){
                volleySingleton.putImageInCache(content.getUrl());
            }
        }
    }
}
