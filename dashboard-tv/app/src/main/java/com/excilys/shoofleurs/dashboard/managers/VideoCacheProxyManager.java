package com.excilys.shoofleurs.dashboard.managers;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by excilys on 22/04/16.
 */
public class VideoCacheProxyManager {
    private HttpProxyCacheServer proxy;

    private static VideoCacheProxyManager INSTANCE = null;

    public static VideoCacheProxyManager getInstance() {
        if (INSTANCE == null) INSTANCE = new VideoCacheProxyManager();
        return INSTANCE;
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        VideoCacheProxyManager app = VideoCacheProxyManager.getInstance();
        return app.proxy == null ? (app.proxy = app.newProxy(context)) : app.proxy;
    }

    private HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .build();
    }
}
