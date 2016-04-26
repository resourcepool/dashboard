package excilys.dashboardadministrator.ui.managers;

import android.content.Context;

import excilys.dashboardadministrator.model.entities.AbstractContent;
import excilys.dashboardadministrator.model.entities.ImageContent;
import excilys.dashboardadministrator.rest.VolleySingleton;


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
