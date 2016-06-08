package com.excilys.shoofleurs.dashboard.ui.presenters;

import com.excilys.shoofleurs.dashboard.rest.events.SlideShowUpdatesEvent;
import com.excilys.shoofleurs.dashboard.rest.events.SlideShowUpdatesResponseEvent;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.event.SetDebugMessageEvent;
import com.excilys.shoofleurs.dashboard.ui.views.DashboardView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Thic class represent the presenter of the DashboardActivity
 */
public class DashboardPresenter extends AbstractPresenter<DashboardView> {
    private EventBus mEventBus;
    private DashboardView mDashboardView;

    public DashboardPresenter(DashboardApplication dashboardApplication) {
        super(dashboardApplication);
        mEventBus = dashboardApplication.getEventBus();
        mEventBus.register(this);
    }

    @Override
    public void attachView(DashboardView view) {
        this.mDashboardView = view;
        mDashboardView.startWaitingAnimation();
        /*Check slideshows updates*/
        mEventBus.post(new SlideShowUpdatesEvent());
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
    public void onSetDebugMessageEvent(SetDebugMessageEvent setDebugMessageEvent) {
        mDashboardView.showDebugMessage(setDebugMessageEvent.getMessageId());
    }

    @Subscribe
    public void onSlideShowUpdatesResponseEvent(SlideShowUpdatesResponseEvent slideShowUpdatesResponseEvent) {
        mDashboardView.addSlideShows(slideShowUpdatesResponseEvent.getSlideShows());
    }
}
