package com.excilys.shooflers.dashboard.server.discovery;

import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;

/**
 * This class allows to advertise the service using the mDNS Service-discovery protocol.
 * The feature should facilitate the binding between the Android TVs and the backend.
 *
 * @author Loïc Ortola on 09/06/2016.
 */
@Service
public class ServiceDiscovery implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    @Autowired
    DashboardProperties props;
    
    private JmDNS jmDNS;

    private ServiceInfo serviceInfo;

    private int port;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        if (props.isServiceDiscoveryEnabled()) {
            port = event.getEmbeddedServletContainer().getPort();
            init();
        }
    }

    /**
     * Initialize and run the mDNS Service discovery. Will start advertising
     */
    private void init() {
        try {
            jmDNS = JmDNS.create();
            serviceInfo = ServiceInfo.create("_http._tcp.local.",
                    "Shooflers-Dashboard", port,
                    "An awesome webapp to handle your monitor dashboards");
            jmDNS.registerService(serviceInfo);
        } catch (IOException e) {
            LOGGER.error("Couldn't initialize mDNS for service discovery.", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        jmDNS.unregisterAllServices();
        jmDNS.close();
        super.finalize();
    }
}
