package com.excilys.shoofleurs.dashboard.ui.activities;

import android.animation.AnimatorSet;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.model.entities.Media;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.adapters.MediaPagerAdapter;
import com.excilys.shoofleurs.dashboard.ui.displayables.AbstractDisplayable;
import com.excilys.shoofleurs.dashboard.ui.displayables.Displayable;
import com.excilys.shoofleurs.dashboard.ui.displayables.DisplayableFactory;
import com.excilys.shoofleurs.dashboard.ui.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.ui.presenters.DashboardPresenter;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;
import com.excilys.shoofleurs.dashboard.ui.utils.ViewPagerCustomDuration;
import com.excilys.shoofleurs.dashboard.ui.utils.ZoomOutPageTransformer;
import com.excilys.shoofleurs.dashboard.ui.views.DashboardView;

import java.util.ArrayList;
import java.util.List;

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

    private DashboardPresenter mDashboardPresenter;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtils.hideStatusBar(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDashboardPresenter = new DashboardPresenter((DashboardApplication) getApplication());
        mDashboardPresenter.attachView(this);

        setUpViewPager();
    }

    private void setUpViewPager() {
        mAdapter = new MediaPagerAdapter(getSupportFragmentManager(), this, new ArrayList<AbstractDisplayable>());

        mViewPager.addOnPageChangeListener(mAdapter);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    public void clearMedias() {
        mAdapter.clearAllDatas();
    }

    @Override
    public void addMedias(List<Media> medias) {
        mAdapter.addAllDatas(DisplayableFactory.createAll(medias, mAdapter));
        mAdapter.onPageSelected(0);
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
    public void showWaitingAnimation(boolean show) {
        if (show) {
            RelativeLayout progressViewLayout = (RelativeLayout) findViewById(R.id.progress_view_layout);
            mProgressAnimators[0] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[0], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_LEFT);
            mProgressAnimators[1] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[3], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_LEFT);
            mProgressAnimators[2] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[2], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_RIGHT);
            mProgressAnimators[3] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[1], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_RIGHT);

            AndroidUtils.setVisibility(View.VISIBLE, mProgressPointViews);
            AndroidUtils.startAnimators(mProgressAnimators);
        } else {
            AndroidUtils.cancelAnimators(mProgressAnimators[0]);
            AndroidUtils.setVisibility(View.GONE, mProgressPointViews);
        }
    }

    @Override
    public void showBackground(boolean show) {
        mBackgroundLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void displayDebugMessage(int messageId) {
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
