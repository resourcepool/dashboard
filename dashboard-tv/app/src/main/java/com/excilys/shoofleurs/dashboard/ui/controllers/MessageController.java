package com.excilys.shoofleurs.dashboard.ui.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.ui.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.ui.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.model.entities.Message;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents the controller of the messages display.
 * It scroll automatically the current message during 1 min and
 * go to the next.
 */
public class MessageController {
    private static MessageController S_INSTANCE;
    private final static int S_DELAY_BEETWEEN_MESSAGES = 45000; // 45 seconds
    private final static int S_DELAY_BEFORE_SCROLLING = 3000; // 3 seconds

    private DashboardActivity mDashboardActivity;

    /**
     * List of messages to be display
     */
    private List<Message> mMessages;

    /**
     * The current message displayed.
     */
    private Message mCurrentMessage;

    private int mCurrentMessageIndex;

    private TextView mMessageTextView;
    private TextView mMessageTitleView;

    /**
     * Fade in animators for messages text views;
     */
    private Animator mMessageTextFadeInAnimator, mMessageTitleFadeInAnimator;

    /**
     * Fade out animators for messages text views;
     */
    private Animator mMessageTextFadeOutAnimator, mMessageTitleFadeOutAnimator;

    /**
     * The handler to set a delay between each messages
     */
    private Handler mHandler;

    public static MessageController getInstance(DashboardActivity dashboardActivity) {
        if (S_INSTANCE == null) S_INSTANCE = new MessageController(dashboardActivity);
        return S_INSTANCE;
    }

    private MessageController(DashboardActivity dashboardActivity) {
        this.mDashboardActivity = dashboardActivity;
        mMessages = new ArrayList<>();
        mHandler = new Handler();
        mCurrentMessageIndex = 0;
        mMessageTextView = (TextView) mDashboardActivity.findViewById(R.id.text_cnn);
        mMessageTitleView = (TextView) mDashboardActivity.findViewById(R.id.title_cnn);

        AndroidUtils.setVisibility(View.GONE, mMessageTextView, mMessageTitleView);

        int fadeDuration = 1000; //1 second
        mMessageTextFadeInAnimator = AnimatorFactory.createFadeInAnimator(mMessageTextView, fadeDuration);
        mMessageTitleFadeInAnimator = AnimatorFactory.createFadeInAnimator(mMessageTitleView, fadeDuration);
        mMessageTextFadeOutAnimator = AnimatorFactory.createFadeOutAnimator(mMessageTextView, fadeDuration);
        mMessageTitleFadeOutAnimator = AnimatorFactory.createFadeOutAnimator(mMessageTitleView, fadeDuration);
    }


    public void addMessages(Message... messages) {
        Log.i(MessageController.class.getSimpleName(), "addMessages: " + Arrays.asList(messages));
        mMessages.addAll(Arrays.asList(messages));
        refresh();
    }

    /**
     * If none message is displaying, start it.
     */
    private void refresh() {
        if (mCurrentMessage == null) {
            startMessages();
        }
    }

    private void startMessages() {
        if (!mMessages.isEmpty()) {
            mCurrentMessageIndex = 0;
            mCurrentMessage = mMessages.get(mCurrentMessageIndex);
            displayMessage(mCurrentMessage);
        } else {
            Log.i(MessageController.class.getSimpleName(), "startMessages: The messages are empty!");
        }
    }

    /**
     * Display the message in parameter. Before start scrolling with
     * mMessageTextView.setSelected, set a initial delay of 3 seconds.
     *
     * @param message
     */
    private void displayMessage(Message message) {
        Log.i(MessageController.class.getSimpleName(), "displayMessage: " + message);

        mMessageTextFadeInAnimator.start();
        mMessageTitleFadeInAnimator.start();
        mMessageTextView.setSelected(false);
        mMessageTitleView.setText(message.getTitle());
        mMessageTextView.setText(message.getMessage());

        /*Initial delay before scrolling*/
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageTextView.setSelected(true);
            }
        }, S_DELAY_BEFORE_SCROLLING);

        handleNextMessage();
    }

    /**
     * Handle the next message after 1 min of display.
     */
    private void handleNextMessage() {
        final Message nextMessage = getNextMessage();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageTitleFadeOutAnimator.start();
                mMessageTextFadeOutAnimator.removeAllListeners();
                mMessageTextFadeOutAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        displayMessage(nextMessage);
                    }
                });
                mMessageTextFadeOutAnimator.start();

            }
        }, S_DELAY_BEETWEEN_MESSAGES);
    }

    /**
     * Return the next message of the messages list or
     * return to first if the current is the last.
     */
    public Message getNextMessage() {
        mCurrentMessageIndex++;
        Message nextMessage = mCurrentMessageIndex < mMessages.size() ?
                mMessages.get(mCurrentMessageIndex) :
                mMessages.get((mCurrentMessageIndex = 0));
        Log.d(MessageController.class.getSimpleName(), "nextMessage: " + nextMessage);

        return nextMessage;
    }
}
