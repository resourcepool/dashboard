package com.excilys.shooflers.dashboard.server.bundlecontroller;

import com.excilys.shooflers.dashboard.server.AbstractControllerTest;
import com.excilys.shooflers.dashboard.server.DashboardApplication;
import com.excilys.shooflers.dashboard.server.TestConfiguration;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapper;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.nio.file.FileSystem;

/**
 * Created by Camille Vrod on 15/06/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({DashboardApplication.class, TestConfiguration.class})
@WebAppConfiguration
public abstract class AbstractBundleControllerTest extends AbstractControllerTest {

    @Autowired
    protected FileSystem fileSystem;
    
    @Autowired
    protected BundleService bundleService;

    @Autowired
    protected BundleDtoMapper bundleDtoMapper;

}
