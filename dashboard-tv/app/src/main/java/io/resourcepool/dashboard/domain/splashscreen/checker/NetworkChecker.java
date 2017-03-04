package io.resourcepool.dashboard.domain.splashscreen.checker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import io.resourcepool.dashboard.R;

/**
 * Created by loicortola on 04/03/2017.
 */
public class NetworkChecker extends Checker {

    public static final String TAG = NetworkChecker.class.getSimpleName();

    private final Context ctx;
    private BroadcastReceiver networkConnectivityBR;

    private NetworkChecker(int checkerId, CheckerStatusListener listener, Context context) {
        super(checkerId, listener);
        this.ctx = context.getApplicationContext();
    }

    private void startBroadcastReceiver() {
        this.networkConnectivityBR = getNetworkConnectivityBR();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        ctx.registerReceiver(networkConnectivityBR, intentFilter);
    }

    private void stopBroadcastReceiver() {
        ctx.unregisterReceiver(networkConnectivityBR);
    }

    @Override
    public boolean isReady() {
        NetworkInfo info = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
            Log.d(TAG, "no internet connection");
            return false;
        } else {
            if (info.isConnected()) {
                Log.d(TAG, " internet connection available...");
                return true;
            } else {
                Log.d(TAG, " internet connection");
                return true;
            }
        }
    }

    @Override
    public int getStatusMessage() {
        return R.string.progress_waiting_internet_connectivity;
    }

    @Override
    public void start() {
        super.start();
        startBroadcastReceiver();
    }

    @Override
    public void stop() {
        super.stop();
        stopBroadcastReceiver();
    }

    public static NetworkChecker create(int checkerId, CheckerStatusListener listener, Context context) {
        return new NetworkChecker(checkerId, listener, context);
    }

    private BroadcastReceiver getNetworkConnectivityBR() {
        return new BroadcastReceiver() {

            public static final String TAG = "InternetBR";
            public static final String NETWORK_INFO_PARAM = "networkInfo";

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                NetworkInfo info = extras.getParcelable(NETWORK_INFO_PARAM);
                NetworkInfo.State state = info.getState();
                Log.d(TAG, info.toString() + " " + state.toString());
                if (state == NetworkInfo.State.CONNECTED) {
                    setReady(true);
                } else {
                    setReady(false);
                }
            }
        };
    }
}
