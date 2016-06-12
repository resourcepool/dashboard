package com.excilys.shoofleurs.dashboard.ui;

import android.app.Application;

import com.excilys.shoofleurs.dashboard.database.DatabaseManager;
import com.excilys.shoofleurs.dashboard.rest.service.BundleService;
import com.excilys.shoofleurs.dashboard.rest.service.DashboardService;
import com.excilys.shoofleurs.dashboard.rest.service.NewsService;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tommy on 10/05/16.
 */
public class DashboardApplication extends Application {
    private BundleService mBundleService;
    private NewsService mNewsService;
    private DashboardService mDashboardService;
    private EventBus mEventBus;
    private DatabaseManager mDatabaseManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus = EventBus.getDefault();
        mBundleService = new BundleService(mEventBus);
        mNewsService = new NewsService(mEventBus);
        mDashboardService = new DashboardService(mEventBus);
        mDatabaseManager = new DatabaseManager(getApplicationContext(), mEventBus);
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

    public DashboardService getDashboardService() {
        return mDashboardService;
    }

    public DatabaseManager getDatabaseManager() {
        return mDatabaseManager;
    }
}
