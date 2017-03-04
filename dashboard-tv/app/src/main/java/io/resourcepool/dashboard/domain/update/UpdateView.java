package io.resourcepool.dashboard.domain.update;

import io.resourcepool.dashboard.ui.views.View;

public interface UpdateView extends View {
    /**
     * Show the waiting animation on the dashboard background view
     * @param show
     */
    void showWaitingAnimation(boolean show);

    void showLoadingAnimation(boolean show, int size);

    void launchDashboardActivity();

    void incProgress();
}
