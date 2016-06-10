package com.excilys.shoofleurs.dashboard.ui.presenters;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.database.dao.impl.BundleDaoImpl;
import com.excilys.shoofleurs.dashboard.model.entities.Media;
import com.excilys.shoofleurs.dashboard.rest.events.GetBundleResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.events.GetMediaResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.service.BundleService;
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
    private SplashScreenView mSplashScreenView;

    public SplashScreenPresenter(DashboardApplication dashboardApplication) {
        super(dashboardApplication);
        mEventBus = dashboardApplication.getEventBus();
        mBundleService = dashboardApplication.getBundleService();
    }

    @Override
    public void attachView(SplashScreenView view) {
        this.mSplashScreenView = view;

        mSplashScreenView.showWaitingAnimation(true);
        /*Check bundles updates*/
        //mBundleService.getBundles();
        mSplashScreenView.displayDebugMessage(R.string.debug_check_updates);
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
//        addBundles(getBundleResponseEvent.getBundles());
        new BundleDaoImpl().save(getBundleResponseEvent.getBundles());
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
}
