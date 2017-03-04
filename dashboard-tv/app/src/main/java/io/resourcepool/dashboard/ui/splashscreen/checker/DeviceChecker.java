package io.resourcepool.dashboard.ui.splashscreen.checker;

import android.os.Handler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.database.DashboardPrefs;
import io.resourcepool.dashboard.rest.dtos.Device;
import io.resourcepool.dashboard.rest.events.RegisterDeviceResponseEvent;
import io.resourcepool.dashboard.rest.service.DiscoveryService;
import io.resourcepool.dashboard.ui.DashboardApplication;

/**
 * Created by loicortola on 04/03/2017.
 */
public class DeviceChecker extends Checker {

    public static final int ERROR_REGISTRATION_FAILED = 0;
    public static final int ERROR_NOT_CONFIGURED = 1;

    public static final String TAG = DeviceChecker.class.getSimpleName();

    private static final int MSG_REGISTERING = R.string.progress_registering_to_server;

    private final DashboardApplication ctx;
    private final DiscoveryService discoveryService;
    private final EventBus eventBus;
    private Handler handler;
    private Handler mainHandler;

    private DeviceChecker(int checkerId, CheckerStatusListener listener, DashboardApplication application) {
        super(checkerId, listener);
        this.ctx = application;
        this.handler = new Handler();
        this.mainHandler = new Handler(ctx.getMainLooper());
        this.discoveryService = application.getDiscoveryService();
        this.eventBus = application.getEventBus();

    }

    @Override
    public int getStatusMessage() {
        return MSG_REGISTERING;
    }

    @Override
    public void start() {
        super.start();
        eventBus.register(this);
        if (discoveryService.isInitialized()) {
            discoveryService.registerDevice();
        } else {
            setFailed(ERROR_REGISTRATION_FAILED);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.eventBus.unregister(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onRegisterDeviceResponseEvent(RegisterDeviceResponseEvent event) {
        Device device = event.getDevice();
        if (device == null) {
            setFailed(ERROR_REGISTRATION_FAILED);
            return;
        }
        if (device.getFeedId() == null) {
            setFailed(ERROR_NOT_CONFIGURED);
            return;
        }
        DashboardPrefs.saveFeed(ctx, device.getFeedId());
        setReady(true);
    }

    public static DeviceChecker create(int checkerId, CheckerStatusListener listener, DashboardApplication context) {
        return new DeviceChecker(checkerId, listener, context);
    }

}
