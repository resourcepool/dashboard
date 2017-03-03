package io.resourcepool.dashboard.revisioncontroll;

import io.resourcepool.dashboard.AbstractControllerTest;
import io.resourcepool.dashboard.dto.mapper.BundleDtoMapper;
import io.resourcepool.dashboard.service.BundleService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Florian Castelain on 17/06/16.
 */
public abstract class AbstractRevisionControllerTest extends AbstractControllerTest {


    // ============================================================
    //	Attributes
    // ============================================================

    @Autowired
    protected BundleService bundleService;

    @Autowired
    protected BundleDtoMapper bundleDtoMapper;

}
