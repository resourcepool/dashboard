package io.resourcepool.dashboard.ui.presenters;

import io.resourcepool.dashboard.ui.views.View;

public interface Presenter<T extends View> {
    void attachView(T view);
    void onPause();
    void onResume();
}
