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
    private String adminLogin = "admin";
    private String adminPassword = "admin";
    private long sessionTimeout = 10;
    private String baseUrl = "http://localhost:8080/";

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

    public String getAdminLogin() {
        return adminLogin;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setAdminLogin(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}