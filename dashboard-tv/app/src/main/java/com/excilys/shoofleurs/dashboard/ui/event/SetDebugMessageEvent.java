package com.excilys.shoofleurs.dashboard.ui.event;

import android.support.annotation.StringRes;

/**
 * Created by excilys on 07/06/16.
 */
public class SetDebugMessageEvent {
    @StringRes
    private int mMessageId;

    public SetDebugMessageEvent(int messageId) {
        mMessageId = messageId;
    }

    public int getMessageId() {
        return mMessageId;
    }
}
