package com.excilys.shoofleurs.dashboard.ui.activities;

import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.rest.events.MessageUpdatesEvent;
import com.excilys.shoofleurs.dashboard.rest.events.MessageUpdatesResponseEvent;
import com.excilys.shoofleurs.dashboard.rest.events.SlideShowUpdatesEvent;
import com.excilys.shoofleurs.dashboard.rest.events.SlideShowUpdatesResponseEvent;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.controllers.MessageController;
import com.excilys.shoofleurs.dashboard.ui.controllers.SlideShowController;
import com.excilys.shoofleurs.dashboard.ui.event.SetDebugMessageEvent;
import com.excilys.shoofleurs.dashboard.ui.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * This Activity represents the main view of the application.
 * It asks the server for slideshows updates via the SlideShowService and
 * displays them.
 */
public class DashboardActivity extends FragmentActivity {
    private AnimatorSet[] mProgressAnimators = new AnimatorSet[4];
    /**
     * The waiting view points
     */
    @BindViews({R.id.progress_view_top_left_point,
            R.id.progress_view_top_right_point,
            R.id.progress_view_bottom_right_point,
            R.id.progress_view_bottom_left_point})
    View[] mProgressPointViews;

    @BindView(R.id.background_layout)
    RelativeLayout mBackgroundLayout;

    @BindView(R.id.debug_message)
    TextView mDebugTextView;

    private DashboardApplication mDashboardApplication;

    private EventBus mEventBus;

    /**
     * The controller of slideshows to displaying them
     */
    private SlideShowController mSlideShowController;

    /**
     * The controller of cnn messages
     */
    private MessageController mMessageController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtils.hideStatusBar(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDashboardApplication = (DashboardApplication) getApplication();
        mEventBus = mDashboardApplication.getEventBus();
        mEventBus.register(this);

        startWaitingAnimation();
        if (mSlideShowController == null) {
            mSlideShowController = new SlideShowController(this);
        }

        mMessageController = new MessageController(this);

        checkUpdates();
    }

    private void checkUpdates() {
        mEventBus.post(new SlideShowUpdatesEvent());
        mEventBus.post(new MessageUpdatesEvent());
    }

    /**
     * Start the waiting animation
     */
    public void startWaitingAnimation() {
        RelativeLayout progressViewLayout = (RelativeLayout) findViewById(R.id.progress_view_layout);
        mProgressAnimators[0] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[0], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_LEFT);
        mProgressAnimators[1] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[3], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_LEFT);
        mProgressAnimators[2] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[2], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_RIGHT);
        mProgressAnimators[3] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[1], progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_RIGHT);

        AndroidUtils.setVisibility(View.VISIBLE, mProgressPointViews);
        AndroidUtils.startAnimators(mProgressAnimators);
    }

    /**
     * Stop the waiting animation
     */
    public void stopWaitingAnimation() {
        AndroidUtils.cancelAnimators(mProgressAnimators[0]);
        AndroidUtils.setVisibility(View.GONE, mProgressPointViews);
    }

    public void hideBackground() {
        if (mBackgroundLayout.getVisibility() == View.INVISIBLE) {
            return;
        }
        Log.v(DashboardActivity.class.getSimpleName(), "hideBackground");
        mBackgroundLayout.setVisibility(View.INVISIBLE);
    }

    public void showBackground() {
        if (mBackgroundLayout.getVisibility() == View.VISIBLE) {
            return;
        }
        Log.v(DashboardActivity.class.getSimpleName(), "showBackground");
        mBackgroundLayout.setVisibility(View.VISIBLE);
    }

    public void setDebugMessage(int messageId) {
        mDebugTextView.setText(getResources().getString(messageId));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mEventBus.isRegistered(this)) {
            return;
        }
        mEventBus.register(this);
    }

    /****************************************************
    * EVENTS
    ****************************************************/

    @Subscribe
    public void onSetDebugMessageEvent(SetDebugMessageEvent setDebugMessageEvent) {
        setDebugMessage(setDebugMessageEvent.getMessageId());
    }

    @Subscribe
    public void onSlideShowUpdatesResponseEvent(SlideShowUpdatesResponseEvent slideShowUpdatesResponseEvent) {
        mSlideShowController.addSlideShows(slideShowUpdatesResponseEvent.getSlideShows());
    }

    @Subscribe
    public void onMessageUpdatesResponseEvent(MessageUpdatesResponseEvent messageUpdatesResponseEvent) {
        mMessageController.addMessages(messageUpdatesResponseEvent.getMessages());
    }
}
