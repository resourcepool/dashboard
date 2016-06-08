package com.excilys.shoofleurs.dashboard.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.model.entities.Message;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.ui.presenters.NewsPresenter;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;
import com.excilys.shoofleurs.dashboard.ui.views.NewsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment implements NewsView {
    private static final String TAG = "NewsFragment";
    private static final int DELAY_BEETWEEN_MESSAGES = 45000; // 45 seconds
    private static final int DELAY_BEFORE_SCROLLING = 3000; // 3 seconds

    /**
     * List of messages to be display
     */
    private List<Message> mMessages;

    /**
     * The current message displayed.
     */
    private Message mCurrentMessage;

    private int mCurrentMessageIndex;

    @BindView(R.id.text_cnn)
    TextView mMessageTextView;

    @BindView(R.id.title_cnn)
    TextView mMessageTitleView;

    /**
     * Fade-in animators for messages text views;
     */
    private Animator mMessageTextFadeInAnimator, mMessageTitleFadeInAnimator;

    /**
     * Fade-out animators for messages text views;
     */
    private Animator mMessageTextFadeOutAnimator, mMessageTitleFadeOutAnimator;

    /**
     * The handler to set a delay between each messages
     */
    private Handler mHandler;

    private NewsPresenter mNewsPresenter;

    public NewsFragment() {
        mMessages = new ArrayList<>();
        mHandler = new Handler();
        mCurrentMessageIndex = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_bar, container);
        ButterKnife.bind(this, rootView);
        setUpViews();

        mNewsPresenter = new NewsPresenter((DashboardApplication) getActivity().getApplication());
        mNewsPresenter.attachView(this);

        return rootView;
    }


    private void setUpViews() {
        AndroidUtils.setVisibility(View.GONE, mMessageTextView, mMessageTitleView);

        int fadeDuration = 1000; //1 second
        mMessageTextFadeInAnimator = AnimatorFactory.createFadeInAnimator(mMessageTextView, fadeDuration);
        mMessageTitleFadeInAnimator = AnimatorFactory.createFadeInAnimator(mMessageTitleView, fadeDuration);
        mMessageTextFadeOutAnimator = AnimatorFactory.createFadeOutAnimator(mMessageTextView, fadeDuration);
        mMessageTitleFadeOutAnimator = AnimatorFactory.createFadeOutAnimator(mMessageTitleView, fadeDuration);
    }

    @Override
    public void addMessages(List<Message> messages) {
        Log.i(TAG, "addMessages: " + messages);
        mMessages.addAll(messages);
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
            Log.i(TAG, "startMessages: The messages are empty!");
        }
    }

    /**
     * Displays the message in parameter. Before start scrolling with
     * mMessageTextView.setSelected, sets a initial delay of 3 seconds.
     *
     * @param message
     */
    private void displayMessage(Message message) {
        Log.i(TAG, "displayMessage: " + message);

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
        }, DELAY_BEFORE_SCROLLING);

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
        }, DELAY_BEETWEEN_MESSAGES);
    }

    /**
     * Returns the next message of the messages list or
     * return to first if the current is the last.
     */
    private Message getNextMessage() {
        mCurrentMessageIndex++;
        Message nextMessage = mCurrentMessageIndex < mMessages.size() ?
                mMessages.get(mCurrentMessageIndex) :
                mMessages.get((mCurrentMessageIndex = 0));
        Log.d(TAG, "nextMessage: " + nextMessage);

        return nextMessage;
    }

    @Override
    public void onPause() {
        super.onPause();
        mNewsPresenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mNewsPresenter.onResume();
    }
}
