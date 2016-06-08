package com.excilys.shoofleurs.dashboard.ui;

import android.app.Application;

import com.excilys.shoofleurs.dashboard.rest.service.NewsService;
import com.excilys.shoofleurs.dashboard.rest.service.SlideShowService;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tommy on 10/05/16.
 */
public class DashboardApplication extends Application {
    private SlideShowService mSlideShowService;
    private NewsService mNewsService;
    private EventBus mEventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus = EventBus.getDefault();
        mSlideShowService = new SlideShowService(mEventBus);
        mNewsService = new NewsService(mEventBus);
        Fresco.initialize(getApplicationContext());
    }

    public SlideShowService getSlideShowService() {
        return mSlideShowService;
    }

    public NewsService getNewsService() {
        return mNewsService;
    }

    public EventBus getEventBus() {
        return mEventBus;
    }
}
