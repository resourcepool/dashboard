package com.excilys.shoofleurs.dashboard.rest.events;

import com.excilys.shoofleurs.dashboard.model.entities.Message;

import java.util.List;

/**
 * Created by excilys on 07/06/16.
 */
public class MessageUpdatesResponseEvent {
    private List<Message> mMessages;

    public MessageUpdatesResponseEvent(List<Message> messages) {
        mMessages = messages;
    }

    public List<Message> getMessages() {
        return mMessages;
    }
}
