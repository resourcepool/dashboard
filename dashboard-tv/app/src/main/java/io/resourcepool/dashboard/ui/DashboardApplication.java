package io.resourcepool.dashboard.ui;

import android.app.Application;

import io.resourcepool.dashboard.database.DashboardPrefs;
import io.resourcepool.dashboard.database.DatabaseController;
import io.resourcepool.dashboard.rest.service.BundleService;
import io.resourcepool.dashboard.rest.service.DiscoveryService;
import io.resourcepool.dashboard.rest.service.NewsService;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tommy on 10/05/16.
 */
public class DashboardApplication extends Application {
    private BundleService mBundleService;
    private NewsService mNewsService;
    private DiscoveryService mDiscoveryService;
    private EventBus mEventBus;
    private DatabaseController mDatabaseController;

    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus = EventBus.getDefault();
        mBundleService = new BundleService(mEventBus);
        mNewsService = new NewsService(mEventBus);
        mDiscoveryService = new DiscoveryService(this, mEventBus);
        mDatabaseController = new DatabaseController(getApplicationContext(), mEventBus);
        Fresco.initialize(getApplicationContext());
    }

    public void initServices() {
        String serverHost = "http://" + DashboardPrefs.getServerHost(this);
        mBundleService.initialize(serverHost);
        mDiscoveryService.initialize(serverHost);
        mNewsService.initialize(serverHost);
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

    public DiscoveryService getDiscoveryService() {
        return mDiscoveryService;
    }
}
