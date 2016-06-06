package com.excilys.shoofleurs.dashboard.ui;

import android.app.Application;

import com.excilys.shoofleurs.dashboard.rest.service.SlideShowService;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by tommy on 10/05/16.
 */
public class DashboardApplication extends Application {

    private SlideShowService mSlideShowService;

    @Override
    public void onCreate() {
        super.onCreate();
        mSlideShowService = new SlideShowService();
        Fresco.initialize(getApplicationContext());
    }
}
