package com.excilys.shoofleurs.dashboard.ui.presenters;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.model.comparators.BundleComparator;
import com.excilys.shoofleurs.dashboard.model.entities.Bundle;
import com.excilys.shoofleurs.dashboard.model.entities.Media;
import com.excilys.shoofleurs.dashboard.rest.events.GetBundleResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.events.GetMediaResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.service.BundleService;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.views.DashboardView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Thic class represent the presenter of the DashboardActivity
 */
public class DashboardPresenter extends AbstractPresenter<DashboardView> {
    private static final String TAG = "DashboardPresenter";
    private DashboardView mDashboardView;

    private EventBus mEventBus;

    private BundleService mBundleService;
    /**
     * The sorted slides queue
     */
    private Queue<Bundle> mBundleQueue;

    /**
     * The current bundle to be displayed
     */
    private Bundle mCurrentBundle;

    public DashboardPresenter(DashboardApplication dashboardApplication) {
        super(dashboardApplication);
        mEventBus = dashboardApplication.getEventBus();
        mBundleService = dashboardApplication.getBundleService();

        mBundleQueue = new PriorityQueue<>(10, new BundleComparator());
    }

    @Override
    public void attachView(DashboardView view) {
        this.mDashboardView = view;
        mDashboardView.showWaitingAnimation(true);
        /*Check bundles updates*/
        mBundleService.getBundles();
        mDashboardView.displayDebugMessage(R.string.debug_check_updates);
    }

    public void addBundles(List<Bundle> bundles) {
        Log.d(TAG, "addBundles: " + bundles);
        for (Bundle d : bundles) {
            if (!mBundleQueue.contains(d)) {
                mBundleQueue.add(d);
            }
        }

        if (mBundleQueue.isEmpty()) {
            mDashboardView.displayDebugMessage(R.string.debug_no_bundle_to_display);
            return;
        }

        if (mCurrentBundle != null) {
            Bundle headBundle = mBundleQueue.peek();
            if (!mCurrentBundle.equals(headBundle)) {
                replaceBundle(headBundle);
            }
        } else {
            replaceBundle(mBundleQueue.poll());
        }
    }

    /**
     * Replace or create the current bundle
     *
     * @param newBundle
     */
    private void replaceBundle(Bundle newBundle) {
        mCurrentBundle = newBundle;
        Log.i(TAG, "replaceBundle: " + mCurrentBundle);
        startBundle();
    }

    /**
     * Start the display of the current bundle
     */
    private void startBundle() {
        Log.i(TAG, "startBundle " + mCurrentBundle);
        mBundleService.getMedias(mCurrentBundle.getUuid());
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
        addBundles(getBundleResponseEvent.getBundles());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onGetMediasResponseEvent(GetMediaResponseEvent getMediaResponseEvent) {
        List<Media> medias = getMediaResponseEvent.getMedias();
        Log.i(TAG, "onGetMediasResponseEvent: " + medias);
        mDashboardView.showWaitingAnimation(false);
        if (mCurrentBundle != null) {
            if (medias.size() > 0) {
                mDashboardView.clearMedias();
                mDashboardView.addMedias(medias);
            } else {
                Log.i(TAG, "onGetMediasResponseEvent: The contents are empty!!");
            }
        }
    }
}
