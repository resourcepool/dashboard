package io.resourcepool.dashboard.filecontroller;

import io.resourcepool.dashboard.AbstractControllerTest;
import io.resourcepool.dashboard.dto.BundleMetadataDto;
import io.resourcepool.dashboard.dto.MediaMetadataDto;
import io.resourcepool.dashboard.dto.mapper.BundleDtoMapperImpl;
import io.resourcepool.dashboard.model.metadata.BundleMetadata;
import io.resourcepool.dashboard.model.type.MediaType;
import io.resourcepool.dashboard.property.DashboardProperties;
import io.resourcepool.dashboard.service.BundleService;
import io.resourcepool.dashboard.service.MediaService;
import io.resourcepool.dashboard.rest.FileController;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static io.resourcepool.dashboard.security.interceptor.CorsInterceptor.HEADER_API_KEY;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de {@link FileController}.
 *
 * @author VIEGAS Mickael
 */
public class FileControllerTest extends AbstractControllerTest {
    @Autowired
    private MediaService mediaService;

    @Autowired
    private DashboardProperties props;

    @Autowired
    private BundleService bundleService;

    @Autowired
    private BundleDtoMapperImpl bundleDtoMapper;

    @Test
    @Ignore("Waiting for Spring 5 for the test running")
    public void success() throws Exception {
        final String bundleName = "bundleMetadataDto";
        final BundleMetadata bundleEntity = new BundleMetadata.Builder().name(bundleName).build();
        bundleService.save(bundleEntity);
        BundleMetadataDto globalBundleMetadataDto = bundleDtoMapper.toDto(bundleService.getByTag(bundleEntity.getTag()));

        final long previousRevision = revisionService.getLatest();
        final long size = mediaService.getAll().size();
        final String name = "Zelda";
        final String chosenUuid = UUID.randomUUID().toString();
        final MediaType mediaType = MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpeg", "image/jpeg", "{json:null}".getBytes());

        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .uuid(chosenUuid)
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(globalBundleMetadataDto.getTag())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();

        // Vérification de l'objet Retour
        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        mockMvc.perform(MockMvcRequestBuilders.get(mediaMetadataDto.getUrl())
                .header(HEADER_API_KEY, props.getApiKey())
        )
                .andExpect(status().isOk());
    }

    @Test
    public void failedEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/file"))
                .andExpect(status().isNotFound());
    }
}
