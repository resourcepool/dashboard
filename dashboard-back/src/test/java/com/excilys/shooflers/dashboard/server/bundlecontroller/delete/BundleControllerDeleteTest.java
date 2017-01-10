package com.excilys.shooflers.dashboard.server.bundlecontroller.delete;

import com.excilys.shooflers.dashboard.server.bundlecontroller.AbstractBundleControllerTest;
import com.excilys.shooflers.dashboard.server.dao.impl.BundleDaoImpl;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Camille Vrod on 15/06/16.
 */
public class BundleControllerDeleteTest extends AbstractBundleControllerTest {

    @Autowired
    private MediaService mediaService;

    @Test
    public void failedDeleteUnknown() throws Exception {
        final long previousSize = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();


        mockMvc.perform(deleteAuthenticated("/bundle/wrongtag"))
                .andExpect(status().isNotFound());
        assertEquals(previousSize, bundleService.getAll().size());
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void successDeleteBundle() throws Exception {

        BundleMetadata bundleMetadata = new BundleMetadata.Builder().name("ToDelete").build();
        bundleService.save(bundleMetadata);

        final long previousSize = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();

        assertThat(bundleService.getAll().size(), Matchers.equalTo(1));
        assertThat(bundleMetadata.getUuid(), not(Matchers.isEmptyOrNullString()));
        assertThat(bundleMetadata.getTag(), not(Matchers.isEmptyOrNullString()));
        assertTrue(Files.exists(fileSystem.getPath(props.getBasePath(), BundleDaoImpl.ENTITY_NAME, bundleMetadata.getUuid() + ".yaml")));

        assertNotNull(bundleService.getByTag(bundleMetadata.getTag()));

        mockMvc.perform(deleteAuthenticated("/bundle/" + bundleMetadata.getTag()))
                .andExpect(status().isOk());

        mockMvc.perform(deleteAuthenticated("/bundle/" + bundleMetadata.getTag()))
                .andExpect(status().isNotFound());

        assertFalse(Files.exists(fileSystem.getPath(props.getBasePath(), BundleDaoImpl.ENTITY_NAME, bundleMetadata.getUuid() + ".yaml")));
        assertNull(bundleService.getByTag(bundleMetadata.getTag()));

        assertEquals(previousSize - 1, bundleService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.DELETE);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetadata.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    @Ignore
    public void successWithMediaContent() throws Exception {
        BundleMetadata formBundleMetadata = new BundleMetadata.Builder()
                .name("ToDeleteWithMedia")
                .build();
        bundleService.save(formBundleMetadata);

        Media media1 = Media.builder()
                .metadata(MediaMetadata.builder()
                        .name("Jojo")
                        .mediaType(MediaType.WEB)
                        .bundleTag(formBundleMetadata.getUuid())
                        .url("http://www.jba-france.fr/")
                        .build())
                .build();

        mediaService.save(media1);

        final long previousSize = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();

        assertThat(bundleService.getAll().size(), Matchers.greaterThanOrEqualTo(1));

        BundleMetadata bundleMetadata = bundleService.getAll().get(bundleService.getAll().size() - 1);
        BundleMetadataDto bundleMetaDataDto = bundleDtoMapper.toDto(bundleMetadata);

        assertNotNull(bundleService.getByTag(bundleMetaDataDto.getTag()));
        assertNotNull(mediaService.get(media1.getMetadata().getUuid()));

        mockMvc.perform(deleteAuthenticated("/bundle/" + bundleMetaDataDto.getUuid()))
                .andExpect(status().isOk());

        assertNull(bundleService.getByTag(bundleMetaDataDto.getTag()));
        assertNotNull(mediaService.get(media1.getMetadata().getUuid()));

        assertEquals(previousSize - 1, bundleService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.DELETE);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetaDataDto.getUuid());
        assertEquals(revision.getResult(), null);

        mockMvc.perform(deleteAuthenticated("/bundle/" + bundleMetaDataDto.getUuid()))
                .andExpect(status().isNotFound());
    }
}
