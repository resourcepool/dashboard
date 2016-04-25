package com.excilys.shoofleurs.dashboard.ui.activities;

import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.ui.controllers.MessageController;
import com.excilys.shoofleurs.dashboard.ui.controllers.SlideShowController;
import com.excilys.shoofleurs.dashboard.ui.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.service.MessageService;
import com.excilys.shoofleurs.dashboard.service.SlideShowService;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;

/**
 * This Activity represents the main view of the application.
 * It asks the server for slideshows updates via the SlideShowService and
 * display them.
 */
public class DashboardActivity extends FragmentActivity {
    private AnimatorSet mProgressAnimatorSet1,
            mProgressAnimatorSet2,
            mProgressAnimatorSet3,
            mProgressAnimatorSet4;

    /**
     * The waiting view points
     */
    private View mTopLeftPoint, mBottomLeftPoint, mBottomRightPoint, mTopRightPoint;

    private TextView mDebugTextView;

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
        mDebugTextView = (TextView) findViewById(R.id.debug_message);

        startWaitingAnimation();
        mSlideShowController = SlideShowController.getInstance(this);
        mSlideShowService = SlideShowService.getInstance(this);

        mMessageController = MessageController.getInstance(this);
        mMessageService = MessageService.getInstance(this);

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
        mTopLeftPoint = findViewById(R.id.progress_view_top_left_point);
        mBottomLeftPoint = findViewById(R.id.progress_view_bottom_left_point);
        mBottomRightPoint = findViewById(R.id.progress_view_bottom_right_point);
        mTopRightPoint = findViewById(R.id.progress_view_top_right_point);

        RelativeLayout progressViewLayout = (RelativeLayout) findViewById(R.id.progress_view_layout);
        mProgressAnimatorSet1 = AnimatorFactory.createSquarePointAnimatorSet(mTopLeftPoint, progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_LEFT);
        mProgressAnimatorSet2 = AnimatorFactory.createSquarePointAnimatorSet(mBottomLeftPoint, progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_LEFT);
        mProgressAnimatorSet3 = AnimatorFactory.createSquarePointAnimatorSet(mBottomRightPoint, progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_RIGHT);
        mProgressAnimatorSet4 = AnimatorFactory.createSquarePointAnimatorSet(mTopRightPoint, progressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_RIGHT);

        AndroidUtils.setVisibility(View.VISIBLE, mBottomLeftPoint, mBottomRightPoint, mTopLeftPoint, mTopRightPoint);
        AndroidUtils.startAnimators(mProgressAnimatorSet1, mProgressAnimatorSet2, mProgressAnimatorSet3, mProgressAnimatorSet4);
    }

    /**
     * Stop the waiting animation
     */
    public void stopWaitingAnimation() {
        AndroidUtils.cancelAnimators(mProgressAnimatorSet1, mProgressAnimatorSet2, mProgressAnimatorSet3, mProgressAnimatorSet4);
        AndroidUtils.setVisibility(View.GONE, mBottomLeftPoint, mBottomRightPoint, mTopLeftPoint, mTopRightPoint);
    }

    public void setDebugMessage(int messageId) {
        mDebugTextView.setText(getResources().getString(messageId));
    }

    public SlideShowController getSlideShowController() {
        return mSlideShowController;
    }

    public MessageController getMessageController() {
        return mMessageController;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkUpdates();
    }
}
