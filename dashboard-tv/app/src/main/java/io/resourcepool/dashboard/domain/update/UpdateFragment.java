package io.resourcepool.dashboard.domain.update;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.ui.DashboardApplication;
import io.resourcepool.dashboard.ui.activities.DashboardActivity;
import io.resourcepool.dashboard.ui.factories.AnimatorFactory;
import io.resourcepool.dashboard.ui.utils.AndroidUtils;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class UpdateFragment extends Fragment implements UpdateView {
    private AnimatorSet[] mProgressAnimators = new AnimatorSet[4];
    private OnSplashScreenFragmentInteraction mListener;
    private UpdatePresenter mUpdatePresenter;

    /**
     * The waiting view points
     */
    @BindViews({R.id.progress_view_top_point,
            R.id.progress_view_left_point,
            R.id.progress_view_bottom_point,
            R.id.progress_view_right_point})
    View[] mProgressPointViews;

    @BindView(R.id.background_layout)
    RelativeLayout mBackgroundLayout;

    @BindView(R.id.progress_message)
    TextView mProgressTextView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.progress_view_layout)
    RelativeLayout mProgressViewLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update, container, false);
        ButterKnife.bind(this, rootView);
        mUpdatePresenter = new UpdatePresenter((DashboardApplication) getActivity().getApplication());
        mUpdatePresenter.attachView(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUpdatePresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUpdatePresenter.onPause();
    }

    /**
     * Start the waiting animation
     */
    @Override
    public void showWaitingAnimation(boolean show) {
        if (show) {
            mProgressAnimators[0] = AnimatorFactory.createDiamondPointAnimatorSet(mProgressPointViews[0], mProgressViewLayout.getLayoutParams().width, 300, 0.03f, AnimatorFactory.Position.TOP);
            mProgressAnimators[1] = AnimatorFactory.createDiamondPointAnimatorSet(mProgressPointViews[1], mProgressViewLayout.getLayoutParams().width, 300, 0.03f, AnimatorFactory.Position.LEFT);
            mProgressAnimators[2] = AnimatorFactory.createDiamondPointAnimatorSet(mProgressPointViews[2], mProgressViewLayout.getLayoutParams().width, 300, 0.03f, AnimatorFactory.Position.BOTTOM);
            mProgressAnimators[3] = AnimatorFactory.createDiamondPointAnimatorSet(mProgressPointViews[3], mProgressViewLayout.getLayoutParams().width, 300, 0.03f, AnimatorFactory.Position.RIGHT);

            AndroidUtils.setVisibility(View.VISIBLE, mProgressPointViews);
            AndroidUtils.startAnimators(mProgressAnimators);
        } else {
            AndroidUtils.cancelAnimators(mProgressAnimators[0]);
            AndroidUtils.setVisibility(View.GONE, mProgressPointViews);
        }
    }

    @Override
    public void showLoadingAnimation(boolean show, int size) {
        showWaitingAnimation(false);
        mProgressBar.setMax(size);
        mProgressBar.setProgress(0);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void launchDashboardActivity() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void incProgress() {
        mProgressBar.incrementProgressBy(1);
        if (mProgressBar.getMax() == mProgressBar.getProgress()) {
            launchDashboardActivity();
        }
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
