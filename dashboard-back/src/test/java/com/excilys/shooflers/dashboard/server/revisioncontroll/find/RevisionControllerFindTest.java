package com.excilys.shooflers.dashboard.server.revisioncontroll.find;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.revisioncontroll.AbstractRevisionControllerTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Florian Castelain on 17/06/16.
 */
public class RevisionControllerFindTest extends AbstractRevisionControllerTest {

    // ============================================================
    //	Tests
    // ============================================================

    @Test
    public void successGetLatestRevision() throws Exception {
        mockMvc.perform(getAuthenticated("/revision"))
                .andExpect(status().isOk())
                .andExpect(content().json("0"));
    }

    @Test
    public void successCheckRevisionVersionWithAddMedia() throws Exception {
        MvcResult resultOldRevision = mockMvc.perform(getAuthenticated("/revision"))
                .andExpect(status().isOk())
                .andReturn();

        final long oldRevision = fromJson(resultOldRevision.getResponse().getContentAsString(), Long.class);

        final String name = "Killua Zoldik";
        final String bundleName = "bundleRevision";
        final String url = "http://www.google.fr";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB;

        final BundleMetadata bundleEntity = new BundleMetadata.Builder().name(bundleName).build();
        bundleService.save(bundleEntity);
        BundleMetadataDto globalBundleMetadataDto = bundleDtoMapper.toDto(bundleService.getByTag(bundleEntity.getTag()));

        mockMvc.perform(fileUploadAuthenticated("/media")
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getUuid())
                        .url(url)
                        .build())))
                .andExpect(status().isOk());

        MvcResult resultLatestRevision = mockMvc.perform(getAuthenticated("/revision"))
                .andExpect(status().isOk())
                .andReturn();

        final long LatestRevision = fromJson(resultLatestRevision.getResponse().getContentAsString(), Long.class);

        assertNotEquals(oldRevision, LatestRevision);
    }

    @Test
    public void failedGetRevisionListBadRequest() throws Exception {
        mockMvc.perform(getAuthenticated("/revision/szfgqsf"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void successGetRevisionList() throws Exception {
        MvcResult resultOldRevision = mockMvc.perform(getAuthenticated("/revision"))
                .andExpect(status().isOk())
                .andReturn();

        final long oldRevision = fromJson(resultOldRevision.getResponse().getContentAsString(), Long.class);

        mockMvc.perform(getAuthenticated("/revision/" + oldRevision))
                .andExpect(status().isOk());
    }

}
