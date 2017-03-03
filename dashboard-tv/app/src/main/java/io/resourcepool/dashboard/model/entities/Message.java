package io.resourcepool.dashboard.model.entities;

/**
 * Created by tommy on 24/04/16.
 */
public class Message {
    private String mTitle;

    private String mMessage;

    public Message(String mTitle, String mMessage) {
        this.mTitle = mTitle;
        this.mMessage = mMessage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mTitle='" + mTitle + '\'' +
                ", mMessage='" + mMessage + '\'' +
                '}';
    }
}
