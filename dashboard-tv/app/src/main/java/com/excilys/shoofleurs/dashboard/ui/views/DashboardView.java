package com.excilys.shoofleurs.dashboard.ui.views;

import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;

import java.util.List;

/**
 * This interface represents the view actions of the news bar
 */
public interface DashboardView extends View {
    void startWaitingAnimation();
    void stopWaitingAnimation();
    void hideBackground();
    void showBackground();
    void showDebugMessage(int messageId);
    void addSlideShows(List<SlideShow> slideShows);
    void nextMedia();
}
