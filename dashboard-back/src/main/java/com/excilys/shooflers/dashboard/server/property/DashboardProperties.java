package com.excilys.shooflers.dashboard.server.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Component
@ConfigurationProperties
public class DashboardProperties {
    private String basePath = System.getProperty("user.dir") + "/db";
    private String apiKey = "default";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}