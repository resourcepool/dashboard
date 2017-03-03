package io.resourcepool.dashboard.rest.events;

import io.resourcepool.dashboard.model.entities.Message;

import java.util.List;


public class MessageUpdatesResponseEvent {
    private List<Message> mMessages;

    public MessageUpdatesResponseEvent(List<Message> messages) {
        mMessages = messages;
    }

    public List<Message> getMessages() {
        return mMessages;
    }
}
