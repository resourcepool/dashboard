package io.resourcepool.dashboard.ui.splashscreen.resolver;

/**
 * Created by loicortola on 04/03/2017.
 */


import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.util.Log;

/**
 * Created by loic on 04/03/2017.
 */
public class MulticastDNSResolver implements Runnable {

    public interface DNSResolverListener {
        void onServiceResolved(NsdServiceInfo info);
        void onServiceResolveFailed(NsdServiceInfo info, int errorCode);
        void onFinished(boolean success);
    }

    private static final String SERVICE_TYPE = "_http._tcp.";
    private static final String SERVICE_NAME = "Resourcepool-Dashboard";

    private static final String TAG = MulticastDNSResolver.class.getName();
    private static final long TIMEOUT_MILLIS = 30000;

    private final DNSResolverListener listener;
    private final NsdManager nsdManager;
    private NsdManager.DiscoveryListener discoveryListener;
    private Runnable stopper;
    private Handler handler;
    private boolean active;

    public MulticastDNSResolver(Context ctx, Handler handler, DNSResolverListener listener) {
        this.nsdManager = (NsdManager) ctx.getSystemService(Context.NSD_SERVICE);
        this.listener = listener;
        this.handler = handler;
        this.stopper = new Runnable() {
            @Override
            public void run() {
                // Stop after cycle is finished
                stop(false);
            }
        };
        // Stop after 30 seconds
        handler.postDelayed(stopper, TIMEOUT_MILLIS);
    }

    @Override
    public void run() {
        initializeDiscoveryListener();
    }

    public void stop(boolean success) {
        if (active) {
            active = false;
            nsdManager.stopServiceDiscovery(discoveryListener);
            handler.removeCallbacks(stopper);
            listener.onFinished(success);
        }
    }

    private void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        discoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!  Do something with it.
                Log.d(TAG, "Service discovery success " + service);
                if (SERVICE_TYPE.equals(service.getServiceType()) && SERVICE_NAME.equals(service.getServiceName())) {
                    Log.d(TAG, "Found one " + service.getServiceName() + ". Will attempt resolution.");
                    nsdManager.resolveService(service, new NsdManager.ResolveListener() {
                        @Override
                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                            Log.w(TAG, "Fail!: " + serviceInfo.toString() + " : " + errorCode);
                            listener.onServiceResolveFailed(serviceInfo, errorCode);
                        }

                        @Override
                        public void onServiceResolved(NsdServiceInfo serviceInfo) {
                            listener.onServiceResolved(serviceInfo);
                            stop(true);
                        }
                    });
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "Service lost" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                stop(false);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                stop(false);
            }
        };
        active = true;
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }
}