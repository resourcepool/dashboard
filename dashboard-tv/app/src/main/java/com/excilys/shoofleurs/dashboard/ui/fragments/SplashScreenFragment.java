package com.excilys.shoofleurs.dashboard.ui.fragments;

import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.ui.DashboardApplication;
import com.excilys.shoofleurs.dashboard.ui.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.ui.presenters.SplashScreenPresenter;
import com.excilys.shoofleurs.dashboard.ui.utils.AndroidUtils;
import com.excilys.shoofleurs.dashboard.ui.views.SplashScreenView;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class SplashScreenFragment extends Fragment implements SplashScreenView {
    private AnimatorSet[] mProgressAnimators = new AnimatorSet[4];
    private OnSplashScreenFragmentInteraction mListener;
    private SplashScreenPresenter mSplashScreenPresenter;

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
    @BindView(R.id.progress_view_layout)
    RelativeLayout mProgressViewLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        ButterKnife.bind(this, rootView);
        mSplashScreenPresenter = new SplashScreenPresenter((DashboardApplication) getActivity().getApplication());
        mSplashScreenPresenter.attachView(this);
        return rootView;
    }

    /**
     * Start the waiting animation
     */
    @Override
    public void showWaitingAnimation(boolean show) {
        if (show) {
            mProgressAnimators[0] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[0], mProgressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_LEFT);
            mProgressAnimators[1] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[3], mProgressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_LEFT);
            mProgressAnimators[2] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[2], mProgressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.BOTTOM_RIGHT);
            mProgressAnimators[3] = AnimatorFactory.createSquarePointAnimatorSet(mProgressPointViews[1], mProgressViewLayout.getLayoutParams().width, 300, 0.07f, AnimatorFactory.Position.TOP_RIGHT);

            AndroidUtils.setVisibility(View.VISIBLE, mProgressPointViews);
            AndroidUtils.startAnimators(mProgressAnimators);
        } else {
            AndroidUtils.cancelAnimators(mProgressAnimators[0]);
            AndroidUtils.setVisibility(View.GONE, mProgressPointViews);
        }
    }

    @Override
    public void showBackground(boolean show) {
        mBackgroundLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void displayDebugMessage(int messageId) {
        mDebugTextView.setText(getResources().getString(messageId));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSplashScreenFragmentInteraction) {
            mListener = (OnSplashScreenFragmentInteraction) context;
        } else {
            throw new RuntimeException("The activity " + context.toString() + "must implements OnSplashScreenActivityFragment");
        }
    }

    public interface OnSplashScreenFragmentInteraction {
        void onSplashScreenFinish();
    }
}
