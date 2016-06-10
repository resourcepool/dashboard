package com.excilys.shoofleurs.dashboard.ui.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.model.entities.Media;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.adapters.MediaPagerAdapter;
import com.excilys.shoofleurs.dashboard.ui.displayables.AbstractDisplayable;
import com.excilys.shoofleurs.dashboard.ui.displayables.Displayable;
import com.excilys.shoofleurs.dashboard.ui.displayables.DisplayableFactory;
import com.excilys.shoofleurs.dashboard.ui.fragments.NewsFragment;
import com.excilys.shoofleurs.dashboard.ui.fragments.SplashScreenFragment;
import com.excilys.shoofleurs.dashboard.ui.presenters.DashboardPresenter;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;
import com.excilys.shoofleurs.dashboard.ui.utils.ViewPagerCustomDuration;
import com.excilys.shoofleurs.dashboard.ui.utils.ZoomOutPageTransformer;
import com.excilys.shoofleurs.dashboard.ui.views.DashboardView;
import com.excilys.shoofleurs.dashboard.ui.views.SplashScreenView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This Activity represents the main view of the application.
 * It asks the server for bundles updates via the BundleService and
 * displays them.
 */
public class DashboardActivity extends FragmentActivity implements DashboardView,
        SplashScreenFragment.OnSplashScreenFragmentInteraction {
    private static final String TAG = "DashboardActivity";

    @BindView(R.id.view_pager)
    ViewPagerCustomDuration mViewPager;

    private MediaPagerAdapter mAdapter;
    private DashboardPresenter mDashboardPresenter;
    private SplashScreenView mSplashScreenView;

    private FragmentManager mFragmentManager;

    private SplashScreenFragment mSplashScreenFragment;
    private NewsFragment mNewsFragment;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtils.hideStatusBar(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDashboardPresenter = new DashboardPresenter((DashboardApplication) getApplication());
        mDashboardPresenter.attachView(this);

        setUpViewPager();
        setUpFragments();
    }

    private void setUpViewPager() {
        mAdapter = new MediaPagerAdapter(getSupportFragmentManager(), this, new ArrayList<AbstractDisplayable>());

        mViewPager.addOnPageChangeListener(mAdapter);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setUpFragments() {
        mFragmentManager = getSupportFragmentManager();
        mSplashScreenFragment = (SplashScreenFragment) mFragmentManager.findFragmentById(R.id.splash_fragment);
        mNewsFragment = (NewsFragment) mFragmentManager.findFragmentById(R.id.news_fragment);

        mFragmentManager.beginTransaction().hide(mNewsFragment).commit();

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

    @Override
    public void onSplashScreenFinish() {
    }
}
