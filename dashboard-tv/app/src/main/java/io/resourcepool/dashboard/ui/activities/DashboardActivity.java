package io.resourcepool.dashboard.ui.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.model.entities.Media;
import io.resourcepool.dashboard.ui.DashboardApplication;
import io.resourcepool.dashboard.ui.adapters.MediaPagerAdapter;
import io.resourcepool.dashboard.ui.displayables.AbstractDisplayable;
import io.resourcepool.dashboard.ui.displayables.Displayable;
import io.resourcepool.dashboard.ui.displayables.DisplayableFactory;
import io.resourcepool.dashboard.ui.fragments.NewsFragment;
import io.resourcepool.dashboard.ui.splashscreen.SplashScreenFragment;
import io.resourcepool.dashboard.ui.presenters.DashboardPresenter;
import io.resourcepool.dashboard.ui.utils.AndroidUtils;
import io.resourcepool.dashboard.ui.utils.ViewPagerCustomDuration;
import io.resourcepool.dashboard.ui.utils.ZoomOutPageTransformer;
import io.resourcepool.dashboard.ui.views.DashboardView;
import io.resourcepool.dashboard.ui.splashscreen.SplashScreenView;

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
