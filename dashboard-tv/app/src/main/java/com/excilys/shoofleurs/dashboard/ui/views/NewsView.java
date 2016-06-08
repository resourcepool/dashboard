package com.excilys.shoofleurs.dashboard.ui.views;

import com.excilys.shoofleurs.dashboard.model.entities.Message;

import java.util.List;

/**
 * Created by excilys on 08/06/16.
 */
public interface NewsView extends View {
    void addMessages(List<Message> messages);
}
