package com.excilys.shoofleurs.dashboard.ui.views;

import com.excilys.shoofleurs.dashboard.model.entities.Media;

import java.util.List;

/**
 * This interface represents the view actions of the news bar
 */
public interface DashboardView extends View {
    void nextMedia();

    void clearMedias();

    void addMedias(List<Media> medias);
}
