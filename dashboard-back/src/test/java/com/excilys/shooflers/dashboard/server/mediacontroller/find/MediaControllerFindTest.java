package com.excilys.shooflers.dashboard.server.mediacontroller.find;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.mediacontroller.AbstractMediaControllerTest;
import com.excilys.shooflers.dashboard.server.model.Content;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    public void successAllMedia() throws Exception {
        mockMvc.perform(getAuthenticated("/media"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    /**
     * The main goal is to check that an empty list doesn't generate an error.
     */
    @Test
    public void succesAllMediaEmptyByGoodBundle() throws Exception {
        final String bundleName = "BundleEmptyList";
        final BundleMetadata bundleEntity = new BundleMetadata.Builder().name(bundleName).build();
        bundleService.save(bundleEntity);
        BundleMetadataDto bundleMetadataDto = bundleDtoMapper.toDto(bundleService.getByTag(bundleEntity.getTag()));
        mockMvc.perform(getAuthenticated("/media?bundle=" + bundleMetadataDto.getTag()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void successAllMediaByBundleNotFound() throws Exception {
        mockMvc.perform(getAuthenticated("/media?bundle=bundlefindfail"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void failedWrongFormatUuid() throws Exception {
        mockMvc.perform(getAuthenticated("/media/baduuid"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void failedFindByWrongUuid() throws Exception {
        mockMvc.perform(getAuthenticated("/media/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void successMediaWeb() throws Exception {
        final String name = "needToTest";
        final String url = "http://www.perdu.com/";
        MediaMetadata mmd = MediaMetadata.builder().name(name).mediaType(MediaType.WEB).url(url).bundleTag(globalBundleMetadataDto.getTag()).build();
        Media media = Media.builder().metadata(mmd).build();
        mediaService.save(media);

        MvcResult mvcResult = mockMvc.perform(getAuthenticated("/media/" + media.getMetadata().getUuid()))
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(mvcResult.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(globalBundleMetadataDto.getTag(), mediaMetadataDto.getBundleTag());
        assertNull(mediaMetadataDto.getContent());
        assertEquals(url, mediaMetadataDto.getUrl());
        assertEquals(MediaType.WEB.toString(), mediaMetadataDto.getMediaType());
    }

    @Test
    public void successMediaImage() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final long size = mediaService.getAll().size();
        final String name = "Zelda";
        final String chosenUuid = UUID.randomUUID().toString();
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpeg", "image/jpeg", "{json:null}".getBytes());
        final MediaMetadata mediaMetadata = MediaMetadata.builder()
                .uuid(chosenUuid)
                .name(name)
                .mediaType(mediaType)
                .bundleTag(globalBundleMetadataDto.getTag())
                .build();

        mediaService.save(Media.builder()
                .metadata(mediaMetadata)
                .content(new Content(jsonFile, mediaMetadata.getMediaType()))
                .build());
        assertEquals(previousRevision + 1, revisionService.getLatest());
        assertEquals(size + 1, mediaService.getAll().size());

        MvcResult mvcResult = mockMvc.perform(getAuthenticated("/media/" + mediaMetadata.getUuid()))
                .andExpect(status().isOk())
                .andReturn();

        MediaMetadataDto mediaMetadataDto = fromJson(mvcResult.getResponse().getContentAsString(), MediaMetadataDto.class);

        assertEquals(name, mediaMetadataDto.getName());
        assertEquals(globalBundleMetadataDto.getTag(), mediaMetadataDto.getBundleTag());
        assertNull(mediaMetadataDto.getContent());
        assertNotNull(mediaMetadataDto.getUrl());
        assertEquals(MediaType.IMAGE.toString(), mediaMetadataDto.getMediaType());
    }
}
