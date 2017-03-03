package io.resourcepool.dashboard.endpointsResources;

import io.resourcepool.dashboard.security.annotation.RequireValidApiKey;
import io.resourcepool.dashboard.security.annotation.RequireValidUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController to tests AnnotationSecurity only on Methods
 */
@RestController
@RequestMapping("test1")
public class AnnotationSecurityOnlyOnMethodsTestController {

    public static final String MESSAGE_OK = "OK";

    @RequestMapping("public")
    public String publicResource() {
        return MESSAGE_OK;
    }

    @RequireValidUser
    @RequestMapping("requireUser")
    public String requireUser() {
        return MESSAGE_OK;
    }

    @RequireValidApiKey
    @RequestMapping("requireApiKey")
    public String requireApiKey() throws Exception {
        return MESSAGE_OK;
    }

    @RequireValidUser
    @RequireValidApiKey
    @RequestMapping("requireUserOrApiKey")
    public String requireUserOrApiKey() throws Exception {
        return MESSAGE_OK;
    }
}
