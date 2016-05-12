package com.excilys.shoofleurs.dashboard.ui;

import android.app.Application;

import com.excilys.shoofleurs.dashboard.rest.ServiceGenerator;
import com.excilys.shoofleurs.dashboard.service.SlideShowService;

/**
 * Created by tommy on 10/05/16.
 */
public class DashboardApplication extends Application {

    private SlideShowService mSlideShowService;

    @Override
    public void onCreate() {
        super.onCreate();
        mSlideShowService = new SlideShowService();
    }
}