package io.resourcepool.dashboard.ui.presenters;

import android.util.Log;

import io.resourcepool.dashboard.model.comparators.BundleComparator;
import io.resourcepool.dashboard.model.entities.Bundle;
import io.resourcepool.dashboard.ui.DashboardApplication;
import io.resourcepool.dashboard.ui.views.DashboardView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class represent the presenter of the DashboardActivity
 */
public class DashboardPresenter extends AbstractPresenter<DashboardView> {
    private static final String TAG = "DashboardPresenter";
    private DashboardView mDashboardView;

    private EventBus mEventBus;

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

        mBundleQueue = new PriorityQueue<>(10, new BundleComparator());
    }

    @Override
    public void attachView(DashboardView view) {
        this.mDashboardView = view;
    }

    public void addBundles(List<Bundle> bundles) {
        Log.d(TAG, "addBundles: " + bundles);
        for (Bundle d : bundles) {
            if (!mBundleQueue.contains(d)) {
                mBundleQueue.add(d);
            }
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
//        mBundleService.getMedias(mCurrentBundle.getUuid());
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }
}
