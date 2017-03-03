package io.resourcepool.dashboard.ui.views;

import io.resourcepool.dashboard.model.entities.Message;

import java.util.List;

public interface NewsView extends View {
    void addMessages(List<Message> messages);
}
