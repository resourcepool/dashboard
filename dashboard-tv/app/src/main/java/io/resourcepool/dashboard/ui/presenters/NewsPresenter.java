package io.resourcepool.dashboard.ui.presenters;

import io.resourcepool.dashboard.rest.events.MessageUpdatesEvent;
import io.resourcepool.dashboard.rest.events.MessageUpdatesResponseEvent;
import io.resourcepool.dashboard.ui.DashboardApplication;
import io.resourcepool.dashboard.ui.views.NewsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class NewsPresenter extends AbstractPresenter<NewsView> {
    private EventBus mEventBus;

    private NewsView mNewsView;

    public NewsPresenter(DashboardApplication dashboardApplication) {
        super(dashboardApplication);
        mEventBus = dashboardApplication.getEventBus();
        mEventBus.register(this);
    }

    @Override
    public void attachView(NewsView view) {
        this.mNewsView = view;
        mEventBus.post(new MessageUpdatesEvent());
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
    public void onMessageUpdatesResponseEvent(MessageUpdatesResponseEvent messageUpdatesResponseEvent) {
        mNewsView.addMessages(messageUpdatesResponseEvent.getMessages());
    }
}
