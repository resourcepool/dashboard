package io.resourcepool.dashboard.domain.splashscreen;

import android.content.DialogInterface;

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
     * Launch update activity
     */
    void launchUpdateActivity();

    /**
     * Launch dashboard activity
     */
    void launchDashboardActivity();

    /**
     * Display the debug message in center of the background view
     * @param messageId
     */
    void displayProgressMessage(int messageId);

    /**
     * Display dialog requiring a user interaction
     * @param titleMessageId the title message id
     * @param messageId the message id
     * @param retryMessageId the retry button id
     * @param listener the listener
     */
    void displayErrorDialog(int titleMessageId, int messageId, int retryMessageId, DialogInterface.OnClickListener listener);

    void displayInvalidHostDialog(DialogInterface.OnClickListener onAuto, DialogInterface.OnClickListener onRetry);
}
