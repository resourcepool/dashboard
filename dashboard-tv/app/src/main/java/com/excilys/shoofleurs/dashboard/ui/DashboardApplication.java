package com.excilys.shoofleurs.dashboard.ui;

import android.app.Application;

import com.excilys.shoofleurs.dashboard.rest.service.MessageService;
import com.excilys.shoofleurs.dashboard.rest.service.SlideShowService;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tommy on 10/05/16.
 */
public class DashboardApplication extends Application {
    private SlideShowService mSlideShowService;
    private MessageService mMessageService;
    private EventBus mEventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus = EventBus.getDefault();
        mSlideShowService = new SlideShowService(mEventBus);
        mMessageService = new MessageService(mEventBus);
        Fresco.initialize(getApplicationContext());
    }

    public SlideShowService getSlideShowService() {
        return mSlideShowService;
    }

    public MessageService getMessageService() {
        return mMessageService;
    }

    public EventBus getEventBus() {
        return mEventBus;
    }
}
