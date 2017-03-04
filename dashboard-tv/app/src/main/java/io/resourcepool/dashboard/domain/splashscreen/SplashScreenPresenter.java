package io.resourcepool.dashboard.domain.splashscreen;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.database.DashboardPrefs;
import io.resourcepool.dashboard.domain.splashscreen.checker.Checker;
import io.resourcepool.dashboard.domain.splashscreen.checker.DashboardServerChecker;
import io.resourcepool.dashboard.domain.splashscreen.checker.DeviceChecker;
import io.resourcepool.dashboard.domain.splashscreen.checker.NetworkChecker;
import io.resourcepool.dashboard.domain.splashscreen.checker.UpdateChecker;
import io.resourcepool.dashboard.rest.service.BundleService;
import io.resourcepool.dashboard.ui.DashboardApplication;
import io.resourcepool.dashboard.ui.presenters.AbstractPresenter;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class SplashScreenPresenter extends AbstractPresenter<SplashScreenView> implements Checker.CheckerStatusListener {
    private static final String TAG = "SplashScreenPresenter";

    private static final int CHECKER_NETWORK = 1;
    private static final int CHECKER_DASHBOARD_SERVER = 2;
    private static final int CHECKER_DEVICE = 3;
    private static final int CHECKER_UPDATE = 4;

    private final EventBus mEventBus;
    private final BundleService mBundleService;
    private SplashScreenView mSplashScreenView;
    private List<Checker> checkers;
    private Checker networkChecker;
    private Checker dashboardServerChecker;
    private Checker deviceChecker;
    private Checker updateChecker;

    public SplashScreenPresenter(DashboardApplication dashboardApplication) {
        super(dashboardApplication);
        mEventBus = dashboardApplication.getEventBus();
        mBundleService = dashboardApplication.getBundleService();
    }

    private void checkProgress() {
        for (Checker c : checkers) {
            if (!c.isStarted()) {
                c.start();
            }
            if (!c.isReady()) {
                mSplashScreenView.displayProgressMessage(c.getStatusMessage());
                return;
            }
        }
        // All checkers are ok
        mSplashScreenView.displayProgressMessage(R.string.progress_check_updates);
    }

    private void initCheckers() {
        checkers = new ArrayList<>();
        networkChecker = NetworkChecker.create(CHECKER_NETWORK, this, getApplicationContext());
        dashboardServerChecker = DashboardServerChecker.create(CHECKER_DASHBOARD_SERVER, this, getApplicationContext());
        deviceChecker = DeviceChecker.create(CHECKER_DEVICE, this, getApplicationContext());
        updateChecker = UpdateChecker.create(CHECKER_UPDATE, this, getApplicationContext());
        checkers.add(networkChecker);
        checkers.add(dashboardServerChecker);
        checkers.add(deviceChecker);
        checkers.add(updateChecker);
    }

    @Override
    public void attachView(SplashScreenView view) {
        this.mSplashScreenView = view;
        mSplashScreenView.showWaitingAnimation(true);
        initCheckers();
    }

    @Override
    public void onPause() {
        //mEventBus.unregister(this);
        stopCheckers();
    }

    private void stopCheckers() {
        if (checkers == null) {
            return;
        }
        for (Checker c : checkers) {
            c.stop();
        }
    }

    @Override
    public void onResume() {
        if (!mEventBus.isRegistered(this)) {
            //mEventBus.register(this);
        }
        checkProgress();
    }

    @Override
    public void onCheckerStatusChanged(int checkerId, boolean ready, Object result) {
        switch (checkerId) {
            case CHECKER_DASHBOARD_SERVER:
                if (ready) {
                    // Init services when dashboard server is found
                    getApplicationContext().initServices();
                }
                break;
            case CHECKER_UPDATE:
                if (ready) {
                    if (result != null && ((Boolean) result)) {
                        // We have updates. Load update activity
                        mSplashScreenView.launchUpdateActivity();
                    }
                } else {
                    // No updates. Load dashboard TV
                }
        }
        checkProgress();
    }

    @Override
    public void onCheckerStatusFailed(int checkerId, int errorCode) {
        switch (checkerId) {
            case CHECKER_DASHBOARD_SERVER:
                // Device was not configured
                mSplashScreenView.displayInvalidHostDialog(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DashboardPrefs.clearServerHost(getApplicationContext());
                                dashboardServerChecker.start();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText host = (EditText) ((AlertDialog) dialogInterface).findViewById(R.id.host);
                                DashboardPrefs.saveServerHost(getApplicationContext(), host.getText().toString());
                                dashboardServerChecker.start();
                            }
                        });
                return;
            case CHECKER_DEVICE:
                if (errorCode == DeviceChecker.ERROR_REGISTRATION_FAILED) {
                    // Server can't be reached
                    mSplashScreenView.displayInvalidHostDialog(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DashboardPrefs.clearServerHost(getApplicationContext());
                                    dashboardServerChecker.start();
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText host = (EditText) ((AlertDialog) dialogInterface).findViewById(R.id.host);
                                    DashboardPrefs.saveServerHost(getApplicationContext(), host.getText().toString());
                                    deviceChecker.start();
                                }
                            });
                } else if (errorCode == DeviceChecker.ERROR_NOT_CONFIGURED) {
                    // Device was not configured
                    mSplashScreenView.displayErrorDialog(
                            R.string.error_device_not_configured_title,
                            R.string.error_device_not_configured_message,
                            R.string.retry,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deviceChecker.start();
                                }
                            });
                }
                return;
        }
        checkProgress();
    }
}
