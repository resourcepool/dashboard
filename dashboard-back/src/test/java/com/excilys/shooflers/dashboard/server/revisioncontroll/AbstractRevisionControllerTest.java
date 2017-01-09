package com.excilys.shooflers.dashboard.server.revisioncontroll;

import com.excilys.shooflers.dashboard.server.AbstractControllerTest;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapper;
import com.excilys.shooflers.dashboard.server.service.BundleService;
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
