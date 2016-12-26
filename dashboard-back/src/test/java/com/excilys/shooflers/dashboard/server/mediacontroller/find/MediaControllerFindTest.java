package com.excilys.shooflers.dashboard.server.mediacontroller.find;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.mediacontroller.AbstractMediaControllerTest;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import org.junit.Test;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Florian Castelain
 */
public class MediaControllerFindTest extends AbstractMediaControllerTest {

    // ============================================================
    //	Tests
    // ============================================================

    /**
     * Get the list of the media.
     */
    @Test
    public void allMedia() throws Exception {
        mockMvc.perform(getAuthenticated("/media"))
                .andExpect(status().isOk());
    }

    /**
     * The main goal is to check that an empty list doesn't generate an error.
     */
    @Test
    public void allMediaByEmptyBundle() throws Exception {
        final String bundleName = "BundleEmptyList";
        final BundleMetadata bundleEntity = new BundleMetadata.Builder().name(bundleName).build();
        bundleService.save(bundleEntity);
        BundleMetadataDto bundleMetadataDto = bundleDtoMapper.toDto(bundleService.getByTag(bundleEntity.getTag()));
        mockMvc.perform(getAuthenticated("/media?bundle=" + bundleMetadataDto.getTag()))
                .andExpect(status().isOk());
    }

    @Test
    public void allMediaByBundleNotFound() throws Exception {
        mockMvc.perform(getAuthenticated("/media?bundle=bundlefindfail"))
                .andExpect(status().isOk());
    }

    @Test
    public void notFoundMediaBadformedUuid() throws Exception {
        mockMvc.perform(getAuthenticated("/media/baduuid"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void notFound() throws Exception {
        mockMvc.perform(getAuthenticated("/media/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void find() throws Exception {
        final String name = "needToTest";
        MediaMetadata mmd = MediaMetadata.builder().name(name).mediaType(MediaType.WEB).url("http://www.perdu.com/").bundleTag(globalBundleMetadataDto.getTag()).build();
        Media media = Media.builder().metadata(mmd).build();
        mediaService.save(media);

        mockMvc.perform(getAuthenticated("/media/" + media.getMetadata().getUuid()))
                .andExpect(status().isOk());
    }

}
