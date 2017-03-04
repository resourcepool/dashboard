package io.resourcepool.dashboard.ui.splashscreen;

import io.resourcepool.dashboard.ui.views.View;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public interface SplashScreenView extends View {
    /**
     * Show the waiting animation on the dashboard background view
     * @param show
     */
    void showWaitingAnimation(boolean show);

    /**
     * Show the background
     * @param show
     */
    void showBackground(boolean show);

    /**
     * Display the debug message in center of the background view
     * @param messageId
     */
    void displayDebugMessage(int messageId);
}
