package com.excilys.shoofleurs.dashboard.ui.controllers;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.ui.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.ui.adapters.SlideShowPagerAdapter;
import com.excilys.shoofleurs.dashboard.model.comparators.SlideShowComparator;
import com.excilys.shoofleurs.dashboard.ui.displayables.AbstractDisplayable;
import com.excilys.shoofleurs.dashboard.ui.displayables.Displayable;
import com.excilys.shoofleurs.dashboard.ui.displayables.DisplayableFactory;
import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;
import com.excilys.shoofleurs.dashboard.ui.utils.ViewPagerCustomDuration;
import com.excilys.shoofleurs.dashboard.ui.utils.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class represents the controller of the slideshows display.
 */
public class SlideShowController {
    private static SlideShowController S_INSTANCE;

    private DashboardActivity mDashboardActivity;

    private ViewPagerCustomDuration mViewPager;

    private SlideShowPagerAdapter mAdapter;

    /**
     * The sorted slides queue
     */
    private Queue<SlideShow> mSlideShowQueue;

    /**
     * The current slideshow to be displayed
     */
    private SlideShow mCurrentSlideShow;

    /**
     * The index of the current content displayed.
     */
    private int mCurrentContentIndex;

    public static SlideShowController getInstance(DashboardActivity dashboardActivity) {
        if (S_INSTANCE == null) {
            S_INSTANCE = new SlideShowController(dashboardActivity);
        }
        return S_INSTANCE;
    }

    private SlideShowController(DashboardActivity dashboardActivity) {
        this.mDashboardActivity = dashboardActivity;
        mSlideShowQueue = new PriorityQueue<>(10, new SlideShowComparator());

        mAdapter = new SlideShowPagerAdapter(mDashboardActivity.getSupportFragmentManager(), this, new ArrayList<AbstractDisplayable>());

        mViewPager = (ViewPagerCustomDuration) mDashboardActivity.findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(mAdapter);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    public void addSlideShows(List<SlideShow> slideShows) {
        Log.d(getClass().getSimpleName(), "addSlideShows: " + slideShows);
        for (SlideShow d : slideShows) {
            if (!mSlideShowQueue.contains(d)) {
                mSlideShowQueue.add(d);
            }
        }

        if (mSlideShowQueue.isEmpty()) {
            mDashboardActivity.setDebugMessage(R.string.debug_no_slideshows_to_display);
            return;
        }

        if (mCurrentSlideShow != null) {
            SlideShow headSlideShow = mSlideShowQueue.peek();
            if (!mCurrentSlideShow.equals(headSlideShow)) {
                replaceSlideShow(headSlideShow);
            }
        } else {
            replaceSlideShow(mSlideShowQueue.poll());
        }
    }

    /**
     * Replace or create the current slideshow
     *
     * @param newSlideShow
     */
    public void replaceSlideShow(SlideShow newSlideShow) {
        stopSlideShow();
        mCurrentSlideShow = newSlideShow;
        Log.i(DashboardActivity.class.getSimpleName(), "replaceSlideShow: " + mCurrentSlideShow);
        startSlideShow();
    }

    public void stopSlideShow() {

    }

    public void pauseSlideShow() {
    }

    /**
     * Start the display of the current slideshow
     */
    public void startSlideShow() {
        Log.i(getClass().getSimpleName(), "startSlideShow " + mCurrentSlideShow);
        mCurrentContentIndex = 0;

        if (mCurrentSlideShow != null) {
            if (mCurrentSlideShow.getContents().size() > 0) {
                mAdapter.clearAllDatas();
                mAdapter.addAllDatas(DisplayableFactory.createAll(mCurrentSlideShow.getContents(), mAdapter));
                mAdapter.onPageSelected(0);
            } else {
                Log.i(SlideShowController.class.getSimpleName(), "startSlideShow: The contents are empty!!");
            }
        }
    }

    public void nextPage() {
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

    public void previousPage() {
        /* Stop the current displayable to prevent bugs */
        Displayable displayable = mAdapter.getCurrentDisplayable();
        if (displayable != null) {
            displayable.stop();
        }

        int currentItem = mViewPager.getCurrentItem();
        int previousPage = currentItem > 0 ? currentItem - 1 : mAdapter.getCount() - 1;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(previousPage, true);
        }
    }

    public DashboardActivity getDashboardActivity() {
        return mDashboardActivity;
    }
}
