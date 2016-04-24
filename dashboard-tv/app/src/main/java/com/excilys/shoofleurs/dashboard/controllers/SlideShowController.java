package com.excilys.shoofleurs.dashboard.controllers;

import android.util.Log;
import android.widget.RelativeLayout;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.displayables.AbstractDisplayable;
import com.excilys.shoofleurs.dashboard.displayables.Displayable;
import com.excilys.shoofleurs.dashboard.displayables.DisplayableFactory;
import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;
import com.excilys.shoofleurs.dashboard.managers.ContentCacheManager;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * This class represents the controller of the slideshows display.
 */
public class SlideShowController {
    private static SlideShowController S_INSTANCE;

    private DashboardActivity mDashboardActivity;
    /**
     * The layout encompassing the contents
     */
    private RelativeLayout mContentLayout;
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
        if (S_INSTANCE == null) S_INSTANCE = new SlideShowController(dashboardActivity);
        return S_INSTANCE;
    }

    private SlideShowController(DashboardActivity dashboardActivity) {
        this.mDashboardActivity = dashboardActivity;
        mContentLayout = (RelativeLayout) mDashboardActivity.findViewById(R.id.current_content_layout);
        mSlideShowQueue = new ArrayDeque<>();
    }


    public void addSlideShows(SlideShow... slideShows) {
        Log.i(getClass().getSimpleName(), "addSlideShows: " + Arrays.asList(slideShows));
        for (SlideShow d : slideShows) {
            //d.getContents().add(new VideoContent("Video", "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
            mSlideShowQueue.offer(d);
        }

        if (mCurrentSlideShow != null) {
            if (!mSlideShowQueue.peek().equals(mCurrentSlideShow)) {
                replaceSlideShow(mSlideShowQueue.poll());
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
        this.mCurrentSlideShow = newSlideShow;
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

        //mDashboardActivity.stopWaitingAnimation();
        if (mCurrentSlideShow != null) {
            if (mCurrentSlideShow.getContents().size() > 0){
                AbstractContent firstContent = mCurrentSlideShow.getContents().get(mCurrentContentIndex);
                displayContent(firstContent);
            }
            
            else {
                Log.i(SlideShowController.class.getSimpleName(), "startSlideShow: The contents are empty!!");
            }
        }
    }


    /**
     * This method displays the current slideshow's content and go to the next when it's completed.
     * Before going to the next, the method caches it asynchronously for better render without waiting.
     * @param content
     */
    private void displayContent(AbstractContent content) {
        Log.d(SlideShowController.class.getSimpleName(), "displayContent: " + content);
        final AbstractContent nextContent = getNextContent();

        mDashboardActivity.setDebugMessage(R.string.debug_download_content);

        /* Create the content to display and go to the next when it's completed */
        Displayable displayable = DisplayableFactory.create(content, new AbstractDisplayable.OnCompletionListener() {
            @Override
            public void onCompletion() {
                if (mCurrentSlideShow.getContents().size() != 1) {
                    displayContent(nextContent);
                }
                else {
                    Log.i(SlideShowController.class.getSimpleName(), "displayContent: The content is alone");
                }
            }
        });
        displayable.displayContent(mDashboardActivity, mContentLayout);
        /* Caching the next content during displaying the current*/
        ContentCacheManager.cacheContent(mDashboardActivity, nextContent);
    }

    /**
     * Return the next content of the slideshow or
     * return to first if the current is the last.
     */
    public AbstractContent getNextContent() {
        mCurrentContentIndex++;
        AbstractContent nextContent = mCurrentContentIndex < mCurrentSlideShow.getContents().size() ?
                mCurrentSlideShow.getContents().get(mCurrentContentIndex) :
                mCurrentSlideShow.getContents().get((mCurrentContentIndex = 0));
        Log.d(SlideShowController.class.getSimpleName(), "nextContent: " + nextContent);

        return nextContent;
    }

}
