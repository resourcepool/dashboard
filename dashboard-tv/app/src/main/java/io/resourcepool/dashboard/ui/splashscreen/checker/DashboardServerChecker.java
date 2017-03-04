package io.resourcepool.dashboard.ui.splashscreen.checker;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.util.Log;

import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.database.DashboardPrefs;
import io.resourcepool.dashboard.ui.splashscreen.resolver.MulticastDNSResolver;

/**
 * Created by loicortola on 04/03/2017.
 */
public class DashboardServerChecker extends Checker implements MulticastDNSResolver.DNSResolverListener {

    public static final String TAG = DashboardServerChecker.class.getSimpleName();

    private final Context ctx;
    private MulticastDNSResolver resolver;
    private Handler handler;
    private Handler mainHandler;

    private DashboardServerChecker(int checkerId, CheckerStatusListener listener, Context context) {
        super(checkerId, listener);
        this.ctx = context.getApplicationContext();
        this.handler = new Handler();
        this.mainHandler = new Handler(context.getMainLooper());
    }

    @Override
    public int getStatusMessage() {
        return R.string.progress_looking_for_server;
    }

    @Override
    public void start() {
        super.start();
        this.resolver = new MulticastDNSResolver(ctx, handler, this);
        handler.post(resolver);
    }

    @Override
    public void stop() {
        super.stop();
        resolver.stop(false);
    }

    public static DashboardServerChecker create(int checkerId, CheckerStatusListener listener, Context context) {
        return new DashboardServerChecker(checkerId, listener, context);
    }

    @Override
    public void onServiceResolved(NsdServiceInfo info) {
        Log.d(TAG, "Resolved Resourcepool-Dashboard on IP: " + info.getHost().getHostAddress() + ":" + info.getPort());
        DashboardPrefs.saveServerHost(ctx, info.getHost().getHostAddress() + ":" + info.getPort());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                setReady(true);
            }
        });
    }

    @Override
    public void onServiceResolveFailed(NsdServiceInfo info, int errorCode) {
        Log.d(TAG, "Failed to resolve Resourcepool-Dashboard on IP: " + info.getHost().toString());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                setFailed();
            }
        });
    }

    @Override
    public void onFinished(final boolean success) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!success) {
                    setFailed();
                }
            }
        });
    }
}