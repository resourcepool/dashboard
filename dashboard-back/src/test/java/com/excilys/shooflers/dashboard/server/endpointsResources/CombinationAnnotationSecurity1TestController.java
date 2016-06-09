package com.excilys.shooflers.dashboard.server.endpointsResources;

import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController to test combination of security annotations on Class and on Method.
 *
 * @author Mickael
 */
@RestController
@RequestMapping("test2")
@RequireValidUser
public class CombinationAnnotationSecurity1TestController {

    public static final String MESSAGE_OK = "OK";

    @RequestMapping("requireUser")
    public String publicResource() {
        return MESSAGE_OK;
    }

    @RequireValidApiKey
    @RequestMapping("requireUserOrApiKey")
    public String requireUserOrApiKey() {
        return MESSAGE_OK;
    }
}
