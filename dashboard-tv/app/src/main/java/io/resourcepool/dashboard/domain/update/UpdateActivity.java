package io.resourcepool.dashboard.domain.update;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import butterknife.ButterKnife;
import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.ui.utils.AndroidUtils;

public class UpdateActivity extends FragmentActivity implements UpdateFragment.OnSplashScreenFragmentInteraction {
    private static final String TAG = "UpdateActivity";

    private FragmentManager mFragmentManager;

    private UpdateFragment mUpdateFragment;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtils.hideStatusBar(this);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        setUpFragments();
    }


    private void setUpFragments() {
        mFragmentManager = getSupportFragmentManager();
        mUpdateFragment = (UpdateFragment) mFragmentManager.findFragmentById(R.id.update_fragment);
    }

    @Override
    public void onSplashScreenFinish() {

    }
}
