package com.excilys.shooflers.dashboard.server.revisioncontroll;

import com.excilys.shooflers.dashboard.server.AbstractControllerTest;
import com.excilys.shooflers.dashboard.server.DashboardApplication;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapper;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Florian Castelain on 17/06/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public abstract class AbstractRevisionControllerTest extends AbstractControllerTest {


    // ============================================================
    //	Attributes
    // ============================================================

    @Autowired
    protected BundleService bundleService;

    @Autowired
    protected BundleDtoMapper bundleDtoMapper;

}
