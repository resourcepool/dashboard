package com.excilys.shooflers.dashboard.server.mediacontroller;

import com.excilys.shooflers.dashboard.server.AbstractControllerTest;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapper;
import com.excilys.shooflers.dashboard.server.dto.mapper.MediaDtoMapper;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Florian Castelain
 */
public abstract class AbstractMediaControllerTest extends AbstractControllerTest {

    // ============================================================
    //	Attributes
    // ============================================================

    @Autowired
    protected MediaService mediaService;

    @Autowired
    protected BundleService bundleService;

    @Autowired
    protected BundleDtoMapper bundleDtoMapper;

    @Autowired
    protected MediaDtoMapper mediaDtoMapper;

    /**
     * GlobalBundle.
     */
    protected BundleMetadataDto globalBundleMetadataDto;

    // ============================================================
    //	Callback for Tests
    // ============================================================
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        final String bundleName = "bundleMetadataDto";
        final BundleMetadata bundleEntity = new BundleMetadata.Builder().name(bundleName).build();
        bundleService.save(bundleEntity);
        globalBundleMetadataDto = bundleDtoMapper.toDto(bundleService.getByTag(bundleEntity.getTag()));
    }
}
