package io.resourcepool.dashboard.mediacontroller;

import io.resourcepool.dashboard.AbstractControllerTest;
import io.resourcepool.dashboard.dto.BundleMetadataDto;
import io.resourcepool.dashboard.dto.mapper.BundleDtoMapper;
import io.resourcepool.dashboard.dto.mapper.MediaDtoMapper;
import io.resourcepool.dashboard.model.metadata.BundleMetadata;
import io.resourcepool.dashboard.service.BundleService;
import io.resourcepool.dashboard.service.MediaService;
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
