package io.resourcepool.dashboard.domain.splashscreen.checker;

import android.os.Handler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.rest.events.HasUpdateResponseEvent;
import io.resourcepool.dashboard.rest.service.DiffService;
import io.resourcepool.dashboard.ui.DashboardApplication;

/**
 * Created by loicortola on 04/03/2017.
 */
public class UpdateChecker extends Checker {

    public static final String TAG = UpdateChecker.class.getSimpleName();

    private static final int MSG_CHECK_UPDATES = R.string.progress_check_updates;

    private final DashboardApplication ctx;
    private final DiffService diffService;
    private final EventBus eventBus;
    private Handler handler;
    private Handler mainHandler;

    private UpdateChecker(int checkerId, CheckerStatusListener listener, DashboardApplication application) {
        super(checkerId, listener);
        this.ctx = application;
        this.handler = new Handler();
        this.mainHandler = new Handler(ctx.getMainLooper());
        this.diffService = application.getDiffService();
        this.eventBus = application.getEventBus();

    }

    @Override
    public int getStatusMessage() {
        return MSG_CHECK_UPDATES;
    }

    @Override
    public void start() {
        super.start();
        eventBus.register(this);
        if (diffService.isInitialized()) {
            diffService.hasUpdate();
        } else {
            setFailed();
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.eventBus.unregister(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onHasUpdateResponseEvent(HasUpdateResponseEvent event) {
        if (!event.getHasUpdate()) {
            setReady(true);
            return;
        }
        setReady(true, true);
    }

    public static UpdateChecker create(int checkerId, CheckerStatusListener listener, DashboardApplication context) {
        return new UpdateChecker(checkerId, listener, context);
    }

}
