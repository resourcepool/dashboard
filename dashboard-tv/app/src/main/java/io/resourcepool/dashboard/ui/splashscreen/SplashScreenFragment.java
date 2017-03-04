package io.resourcepool.dashboard.ui.splashscreen;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.ui.DashboardApplication;
import io.resourcepool.dashboard.ui.factories.AnimatorFactory;
import io.resourcepool.dashboard.ui.utils.AndroidUtils;

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
    @BindViews({R.id.progress_view_top_point,
            R.id.progress_view_left_point,
            R.id.progress_view_bottom_point,
            R.id.progress_view_right_point})
    View[] mProgressPointViews;

    @BindView(R.id.background_layout)
    RelativeLayout mBackgroundLayout;

    @BindView(R.id.debug_message)
    TextView mProgressTextView;
    @BindView(R.id.progress_view_layout)
    RelativeLayout mProgressViewLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splashscreen, container, false);
        ButterKnife.bind(this, rootView);
        mSplashScreenPresenter = new SplashScreenPresenter((DashboardApplication) getActivity().getApplication());
        mSplashScreenPresenter.attachView(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSplashScreenPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSplashScreenPresenter.onPause();
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
    public void showBackground(boolean show) {
        mBackgroundLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void displayProgressMessage(int messageId) {
        mProgressTextView.setText(getResources().getString(messageId));
    }

    @Override
    public void displayErrorDialog(int titleMessageId, int messageId, int retryMessageId, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titleMessageId)
                .setMessage(messageId);
        builder.setPositiveButton(retryMessageId, listener);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
