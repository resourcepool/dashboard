package com.excilys.shoofleurs.dashboard.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.rest.events.MessageUpdatesEvent;
import com.excilys.shoofleurs.dashboard.rest.events.MessageUpdatesResponseEvent;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.controllers.NewsController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    private EventBus mEventBus;

    /**
     * The controller of cnn messages
     */
    private NewsController mNewsController;

    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_bar, container);
        ButterKnife.bind(this, rootView);
        mNewsController = new NewsController(rootView);

        DashboardApplication dashboardApplication = (DashboardApplication) getActivity().getApplication();
        mEventBus = dashboardApplication.getEventBus();
        mEventBus.register(this);
        mEventBus.post(new MessageUpdatesEvent());
        return rootView;
    }

    @Subscribe
    public void onMessageUpdatesResponseEvent(MessageUpdatesResponseEvent messageUpdatesResponseEvent) {
        mNewsController.addMessages(messageUpdatesResponseEvent.getMessages());
    }
}
