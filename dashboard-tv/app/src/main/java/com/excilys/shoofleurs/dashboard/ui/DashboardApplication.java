package com.excilys.shoofleurs.dashboard.ui;

import android.app.Application;

import com.excilys.shoofleurs.dashboard.database.DatabaseController;
import com.excilys.shoofleurs.dashboard.rest.service.BundleService;
import com.excilys.shoofleurs.dashboard.rest.service.NewsService;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tommy on 10/05/16.
 */
public class DashboardApplication extends Application {
    private BundleService mBundleService;
    private NewsService mNewsService;
    private EventBus mEventBus;
    private DatabaseController mDatabaseController;

    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus = EventBus.getDefault();
        mBundleService = new BundleService(mEventBus);
        mNewsService = new NewsService(mEventBus);
        mDatabaseController = new DatabaseController(getApplicationContext(), mEventBus);
        Fresco.initialize(getApplicationContext());
    }

    public BundleService getBundleService() {
        return mBundleService;
    }

    public NewsService getNewsService() {
        return mNewsService;
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    public DatabaseController getDatabaseController() {
        return mDatabaseController;
    }
}
