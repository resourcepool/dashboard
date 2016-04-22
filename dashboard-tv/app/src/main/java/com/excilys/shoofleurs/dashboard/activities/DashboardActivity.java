package com.excilys.shoofleurs.dashboard.activities;

import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.controllers.DiaporamaController;
import com.excilys.shoofleurs.dashboard.factories.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.service.DiaporamaService;
import com.excilys.shoofleurs.dashboard.utils.Utils;


public class DashboardActivity extends AppCompatActivity {
    /**
     * Affichage des images.
     */
    private RelativeLayout mContentLayout;

    private AnimatorSet mProgressAnimatorSet1, mProgressAnimatorSet2;


    /**
     * Index de l'image affichée en ce moment.
     */
    private int mCurrentBitmap = 0;


    /**
     * Création d'un interval entre les affichages.
     */
    private Handler mHandler = new Handler();

    private DiaporamaService mDiaporamaService;

    private DiaporamaController mDiaporamaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.hideStatusBar(this);
        setContentView(R.layout.activity_main);
        startProgressView();
        mDiaporamaController = DiaporamaController.getInstance(this);
        mDiaporamaService = DiaporamaService.getInstance(this);
        mDiaporamaService.checkUpdates();

        mContentLayout = (RelativeLayout) findViewById(R.id.current_content_layout);
    }

    public void startProgressView() {
        View point1 = findViewById(R.id.progress_view_point1);
        View point2 = findViewById(R.id.progress_view_point2);
        RelativeLayout progressViewLayout = (RelativeLayout) findViewById(R.id.progress_view_layout);
        mProgressAnimatorSet1 = AnimatorFactory.createProgressPointAnimatorSet(point1, progressViewLayout.getLayoutParams().width);
        mProgressAnimatorSet2 = AnimatorFactory.createProgressPointAnimatorSet(point2, progressViewLayout.getLayoutParams().width);

        mProgressAnimatorSet1.start();
        mProgressAnimatorSet2.setStartDelay(1000);
        mProgressAnimatorSet2.start();
    }


    public void stopProgressView() {
        if (mProgressAnimatorSet1 != null) {
            mProgressAnimatorSet1.cancel();
        }
        if (mProgressAnimatorSet2 != null) {
            mProgressAnimatorSet2.cancel();
        }
    }

    public DiaporamaController getDiaporamaController() {
        return mDiaporamaController;
    }
}
