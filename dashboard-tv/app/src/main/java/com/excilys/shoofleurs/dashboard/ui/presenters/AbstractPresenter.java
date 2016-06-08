package com.excilys.shoofleurs.dashboard.ui.presenters;

import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.views.View;

/**
 * Created by excilys on 08/06/16.
 */
public abstract class AbstractPresenter<T extends View> implements Presenter<T> {
    protected DashboardApplication mDashboardApplication;

    public AbstractPresenter(DashboardApplication dashboardApplication) {
        this.mDashboardApplication = dashboardApplication;
    }
}
