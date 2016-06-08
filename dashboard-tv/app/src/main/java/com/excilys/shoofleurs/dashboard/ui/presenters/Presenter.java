package com.excilys.shoofleurs.dashboard.ui.presenters;

import com.excilys.shoofleurs.dashboard.ui.views.View;

/**
 * Created by excilys on 08/06/16.
 */
public interface Presenter<T extends View> {
    void attachView(T view);
    void onPause();
    void onResume();
}
