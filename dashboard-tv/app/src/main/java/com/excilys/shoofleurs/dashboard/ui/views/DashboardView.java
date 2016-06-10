package com.excilys.shoofleurs.dashboard.ui.views;

import com.excilys.shoofleurs.dashboard.model.entities.Media;

import java.util.List;

/**
 * This interface represents the view actions of the news bar
 */
public interface DashboardView extends View {
    /**
     * Show the wainting animation on the dashboard background view
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


    void nextMedia();

    void clearMedias();

    void addMedias(List<Media> medias);
}
