package com.excilys.shoofleurs.dashboard.ui.activities;

import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;
import com.excilys.shoofleurs.dashboard.rest.service.MessageService;
import com.excilys.shoofleurs.dashboard.rest.service.SlideShowService;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.controllers.MessageController;
import com.excilys.shoofleurs.dashboard.ui.controllers.SlideShowController;
import com.excilys.shoofleurs.dashboard.ui.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * This Activity represents the main view of the application.
 * It asks the server for slideshows updates via the SlideShowService and
 * displays them.
 */
public class DashboardActivity extends FragmentActivity implements
        SlideShowService.OnDebugMessageListener, SlideShowService.OnMessageServiceListener {
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

    /**
     * The service for the slideshows
     */
    private SlideShowService mSlideShowService;

    /**
     * The controller of slideshows to displaying them
     */
    private SlideShowController mSlideShowController;

    /**
     * The service for the cnn messages to displaying them
     */
    private MessageService mMessageService;

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

        startWaitingAnimation();
        if (mSlideShowController == null) {
            mSlideShowController = new SlideShowController(this);
        }

        mSlideShowService = mDashboardApplication.getSlideShowService();
        mSlideShowService.setDebugMessageListener(this);
        mSlideShowService.setMessageServiceListener(this);

        mMessageController = new MessageController(this);
        mMessageService = mDashboardApplication.getMessageService();

        checkUpdates();
    }

    private void checkUpdates() {
        mSlideShowService.checkUpdates();
        mMessageService.checkUpdates();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDebugMessage(int messageId) {
        setDebugMessage(messageId);
    }

    @Override
    public void onCheckUpdatesResponse(List<SlideShow> slideShows) {
        mSlideShowController.addSlideShows(slideShows);
    }
}
