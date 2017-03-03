package io.resourcepool.dashboard.ui.presenters;

import io.resourcepool.dashboard.ui.DashboardApplication;
import io.resourcepool.dashboard.ui.views.View;

public abstract class AbstractPresenter<T extends View> implements Presenter<T> {
    protected DashboardApplication mDashboardApplication;

    public AbstractPresenter(DashboardApplication dashboardApplication) {
        this.mDashboardApplication = dashboardApplication;
    }
}
