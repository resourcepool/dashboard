package com.excilys.shooflers.dashboard.server.mediacontroller;

import com.excilys.shooflers.dashboard.server.BaseControllerTest;
import com.excilys.shooflers.dashboard.server.DashboardApplication;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapper;
import com.excilys.shooflers.dashboard.server.dto.mapper.MediaDtoMapper;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Florian Castelain
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public abstract class MediaControllerTest extends BaseControllerTest {

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
    protected static BundleMetadataDto globalBundleMetadataDto;

    // ============================================================
    //	Callback for Tests
    // ============================================================
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        final String bundleName = "bundleMetadataDto";
        if (globalBundleMetadataDto == null) {
            final BundleMetadata bundleEntity = new BundleMetadata.Builder().name(bundleName).build();
            bundleService.save(bundleEntity);
            globalBundleMetadataDto = bundleDtoMapper.toDto(bundleService.getByTag(bundleEntity.getTag()));
        }
    }

}
