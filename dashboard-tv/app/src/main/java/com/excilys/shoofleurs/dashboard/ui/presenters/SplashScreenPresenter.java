package com.excilys.shoofleurs.dashboard.ui.presenters;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.database.DatabaseManager;
import com.excilys.shoofleurs.dashboard.model.entities.Media;
import com.excilys.shoofleurs.dashboard.rest.events.GetBundleResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.events.GetMediaResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.events.GetRevisionResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.service.BundleService;
import com.excilys.shoofleurs.dashboard.rest.service.DashboardService;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.views.SplashScreenView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class SplashScreenPresenter extends AbstractPresenter<SplashScreenView> {
    private static final String TAG = "SplashScreenPresenter";

    private final EventBus mEventBus;
    private final BundleService mBundleService;
    private final DashboardService mDashboardService;
    private SplashScreenView mSplashScreenView;
    private DatabaseManager mDatabaseManager;

    public SplashScreenPresenter(DashboardApplication dashboardApplication) {
        super(dashboardApplication);
        mEventBus = dashboardApplication.getEventBus();
        mBundleService = dashboardApplication.getBundleService();
        mDatabaseManager = dashboardApplication.getDatabaseManager();
        mDashboardService = dashboardApplication.getDashboardService();
        mEventBus.register(this);
    }

    @Override
    public void attachView(SplashScreenView view) {
        this.mSplashScreenView = view;

        mSplashScreenView.showWaitingAnimation(true);
        checkRevision();
    }

    /**
     * Depending of the local revision, check the server if there is some updates.
     */
    public void checkRevision() {
        Long revision = mDatabaseManager.readLocalRevision();
        if (revision == null) {
            /* This is the first time, we download all the bundles and
             * we persist them into the database.*/
            mSplashScreenView.displayDebugMessage(R.string.debug_download_bundles);
            mBundleService.getAllBundles();
        } else {
            mSplashScreenView.displayDebugMessage(R.string.debug_check_updates);
            mDashboardService.getRevision();
        }
    }

    @Override
    public void onPause() {
        mEventBus.unregister(this);
    }

    @Override
    public void onResume() {
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    /****************************************************
     * EVENTS
     ****************************************************/
    @Subscribe
    @SuppressWarnings("unused")
    public void onGetBundleResponseEvent(GetBundleResponseEvent getBundleResponseEvent) {
//        addBundles(getBundleResponseEvent.getAllBundles());
        if (getBundleResponseEvent.getBundles().isEmpty()) {
            mSplashScreenView.displayDebugMessage(R.string.debug_no_bundles);
        } else {
            mDatabaseManager.saveBundles(getBundleResponseEvent.getBundles());
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onGetMediasResponseEvent(GetMediaResponseEvent getMediaResponseEvent) {
        List<Media> medias = getMediaResponseEvent.getMedias();
        Log.i(TAG, "onGetMediasResponseEvent: " + medias);
        mSplashScreenView.showWaitingAnimation(false);
//        if (mCurrentBundle != null) {
//            if (medias.size() > 0) {
//                mDashboardView.clearMedias();
//                mDashboardView.addMedias(medias);
//            } else {
//                Log.i(TAG, "onGetMediasResponseEvent: The contents are empty!!");
//            }
//        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onGetRevisionResponseEvent(GetRevisionResponseEvent getRevisionResponseEvent) {
        Log.i(SplashScreenPresenter.class.getSimpleName(), "onGetRevisionResponseEvent: " + getRevisionResponseEvent.getRevision());
        mDatabaseManager.saveLocalRevision(getRevisionResponseEvent.getRevision());
    }
}
