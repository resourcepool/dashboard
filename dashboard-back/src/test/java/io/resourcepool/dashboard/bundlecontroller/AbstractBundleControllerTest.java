package io.resourcepool.dashboard.bundlecontroller;

import io.resourcepool.dashboard.AbstractControllerTest;
import io.resourcepool.dashboard.dto.mapper.BundleDtoMapper;
import io.resourcepool.dashboard.service.BundleService;
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
