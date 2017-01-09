package com.excilys.shooflers.dashboard.server.bundlecontroller;

import com.excilys.shooflers.dashboard.server.AbstractControllerTest;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapper;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.FileSystem;

/**
 * Created by Camille Vrod on 15/06/16.
 */
public abstract class AbstractBundleControllerTest extends AbstractControllerTest {

    @Autowired
    protected FileSystem fileSystem;
    
    @Autowired
    protected BundleService bundleService;

    @Autowired
    protected BundleDtoMapper bundleDtoMapper;

}
