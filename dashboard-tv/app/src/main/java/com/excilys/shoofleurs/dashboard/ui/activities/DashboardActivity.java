package com.excilys.shoofleurs.dashboard.ui.activities;

import android.animation.AnimatorSet;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.model.comparators.BundleComparator;
import com.excilys.shoofleurs.dashboard.model.entities.Bundle;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.adapters.MediaPagerAdapter;
import com.excilys.shoofleurs.dashboard.ui.displayables.AbstractDisplayable;
import com.excilys.shoofleurs.dashboard.ui.displayables.Displayable;
import com.excilys.shoofleurs.dashboard.ui.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.ui.presenters.DashboardPresenter;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;
import com.excilys.shoofleurs.dashboard.ui.utils.ViewPagerCustomDuration;
import com.excilys.shoofleurs.dashboard.ui.utils.ZoomOutPageTransformer;
import com.excilys.shoofleurs.dashboard.ui.views.DashboardView;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * This Activity represents the main view of the application.
 * It asks the server for bundles updates via the BundleService and
 * displays them.
 */
public class DashboardActivity extends FragmentActivity implements DashboardView {
    private static final String TAG = "DashboardActivity";
    private AnimatorSet[] mProgressAnimators = new AnimatorSet[4];
    /**
     * The waiting view points
     */
    @BindViews({R.id.progress_view_top_left_point,
            R.id.progress_view_top_right_point,
            R.id.progress_view_bottom_right_point,
            R.id.progress_view_bottom_left_point})
    View[] mProgressPointViews;

    @BindView(R.id.background_layout)
    RelativeLayout mBackgroundLayout;

    @BindView(R.id.debug_message)
    TextView mDebugTextView;

    @BindView(R.id.view_pager)
    ViewPagerCustomDuration mViewPager;

    private MediaPagerAdapter mAdapter;

    /**
     * The sorted slides queue
     */
    private Queue<Bundle> mBundleQueue;

    /**
     * The current bundle to be displayed
     */
    private Bundle mCurrentBundle;

    private DashboardPresenter mDashboardPresenter;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtils.hideStatusBar(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDashboardPresenter = new DashboardPresenter((DashboardApplication) getApplication());
        mDashboardPresenter.attachView(this);

        mBundleQueue = new PriorityQueue<>(10, new BundleComparator());
        setUpViewPager();
    }

    private void setUpViewPager() {
        mAdapter = new MediaPagerAdapter(getSupportFragmentManager(), this, new ArrayList<AbstractDisplayable>());

        mViewPager.addOnPageChangeListener(mAdapter);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }


    @Override
    public void addBundles(List<Bundle> bundles) {
        Log.d(TAG, "addBundles: " + bundles);
        for (Bundle d : bundles) {
            if (!mBundleQueue.contains(d)) {
                mBundleQueue.add(d);
            }
        }

        if (mBundleQueue.isEmpty()) {
            showDebugMessage(R.string.debug_no_bundle_to_display);
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
        stopBundle();
        mCurrentBundle = newBundle;
        Log.i(TAG, "replaceBundle: " + mCurrentBundle);
        startBundle();
    }

    public void stopBundle() {
    }


    /**
     * Start the display of the current bundle
     */
    private void startBundle() {
        Log.i(TAG, "startBundle " + mCurrentBundle);
        /**TODO send medias request of the bundle**/
//        if (mCurrentBundle != null) {
//            if (mCurrentBundle.getContents().size() > 0) {
//                mAdapter.clearAllDatas();
//                mAdapter.addAllDatas(DisplayableFactory.createAll(mCurrentBundle.getContents(), mAdapter));
//                mAdapter.onPageSelected(0);
//            } else {
//                Log.i(TAG, "startBundle: The contents are empty!!");
//            }
//        }
    }

    /**
     * Go to the next media in the bundle
     */
    @Override
    public void nextMedia() {
        /* Stop the current displayable to prevent bugs */
        Displayable displayable = mAdapter.getCurrentDisplayable();
        if (displayable != null) {
            displayable.stop();
        }

        int currentItem = mViewPager.getCurrentItem();
        int nextPage = currentItem < (mAdapter.getCount() - 1) ?
                currentItem + 1 : 0;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(nextPage, true);
        }
    }

    /**
     * Start the waiting animation
     */
    @Override
    public void startWaitingAnimation() {
        RelativeLayout progressViewLayout = (RelativeLayout) findViewById(R.id.progress_view_layout);
        mProgressAnimators[0] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[0], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_LEFT);
        mProgressAnimators[1] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[3], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_LEFT);
        mProgressAnimators[2] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[2], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_RIGHT);
        mProgressAnimators[3] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[1], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_RIGHT);

        AndroidUtils.setVisibility(View.VISIBLE, mProgressPointViews);
        AndroidUtils.startAnimators(mProgressAnimators);
    }

    /**
     * Stop the waiting animation
     */
    @Override
    public void stopWaitingAnimation() {
        AndroidUtils.cancelAnimators(mProgressAnimators[0]);
        AndroidUtils.setVisibility(View.GONE, mProgressPointViews);
    }

    @Override
    public void hideBackground() {
        if (mBackgroundLayout.getVisibility() == View.INVISIBLE) {
            return;
        }
        Log.v(TAG, "hideBackground");
        mBackgroundLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showBackground() {
        if (mBackgroundLayout.getVisibility() == View.VISIBLE) {
            return;
        }
        Log.v(TAG, "showBackground");
        mBackgroundLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDebugMessage(int messageId) {
        mDebugTextView.setText(getResources().getString(messageId));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDashboardPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDashboardPresenter.onResume();
    }
}
