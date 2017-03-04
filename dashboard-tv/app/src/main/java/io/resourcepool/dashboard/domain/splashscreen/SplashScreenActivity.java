package io.resourcepool.dashboard.domain.splashscreen;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import butterknife.ButterKnife;
import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.ui.utils.AndroidUtils;

/**
 * This Activity represents the main view of the application.
 * It asks the server for bundles updates via the BundleService and
 * displays them.
 */
public class SplashScreenActivity extends FragmentActivity implements SplashScreenFragment.OnSplashScreenFragmentInteraction {
    private static final String TAG = "SplashScreenActivity";

    private SplashScreenView mSplashScreenView;

    private FragmentManager mFragmentManager;

    private SplashScreenFragment mSplashScreenFragment;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtils.hideStatusBar(this);
        setContentView(R.layout.activity_splashscreen);
        ButterKnife.bind(this);
        setUpFragments();
    }


    private void setUpFragments() {
        mFragmentManager = getSupportFragmentManager();
        mSplashScreenFragment = (SplashScreenFragment) mFragmentManager.findFragmentById(R.id.splash_fragment);
    }

    @Override
    public void onSplashScreenFinish() {

    }
}
