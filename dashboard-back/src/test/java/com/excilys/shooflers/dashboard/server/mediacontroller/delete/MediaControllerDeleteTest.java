package com.excilys.shooflers.dashboard.server.mediacontroller.delete;

import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.mediacontroller.AbstractMediaControllerTest;
import com.excilys.shooflers.dashboard.server.model.Content;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Florian Castelain
 */
public class MediaControllerDeleteTest extends AbstractMediaControllerTest {

    // ============================================================
    //	Tests
    // ============================================================

    @Test
    public void failedWithBadTag() throws Exception {
        final long previousRevision = revisionService.getLatest();
        mockMvc.perform(deleteAuthenticated("/media/epzirpeoi"))
                .andExpect(status().isNotFound())
        ;
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void successMediaWeb() throws Exception {
        final String name = "MediaName";
        final com.excilys.shooflers.dashboard.server.model.type.MediaType mediaType = com.excilys.shooflers.dashboard.server.model.type.MediaType.WEB;
        final String url = "http://perdu.com";
        final MediaMetadataDto mmDto = new MediaMetadataDto.Builder()
                .bundleTag(globalBundleMetadataDto.getUuid())
                .url(url)
                .name(name)
                .mediaType(mediaType)
                .build();
        final Media m = Media.builder().metadata(mediaDtoMapper.fromDto(mmDto)).build();
        mediaService.save(m);

        Path path = fileSystem.getPath(props.getBasePath(), MediaDao.ENTITY_NAME, m.getMetadata().getBundleTag(), m.getMetadata().getUuid() + ".yaml");

        assertTrue(Files.isRegularFile(path));

        final long previousRevision = revisionService.getLatest();

        mockMvc.perform(getAuthenticated("/media/" + m.getMetadata().getUuid()))
                .andExpect(status().isOk());

        mockMvc.perform(deleteAuthenticated("/media/" + m.getMetadata().getUuid()))
                .andExpect(status().isOk());

        mockMvc.perform(getAuthenticated("/media/" + m.getMetadata().getUuid()))
                .andExpect(status().isNotFound());

        assertFalse(Files.exists(path));

        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.DELETE);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.MEDIA);
        assertEquals(revision.getTarget(), m.getMetadata().getUuid());
        assertEquals(revision.getResult(), null);
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

        // Vérification de la persistence
        Path path = fileSystem.getPath(props.getBasePath(), MediaDao.ENTITY_NAME, mediaMetadata.getBundleTag(), mediaMetadata.getUuid() + ".yaml");
        assertTrue(Files.isRegularFile(path));

        // Vérification de la peristence de l'écriture
        path = fileSystem.getPath(props.getBaseResources(), mediaMetadata.getUrl().substring(mediaMetadata.getUrl().lastIndexOf("/") + 1));
        assertTrue(Files.isRegularFile(path));

        // TEST
        mockMvc.perform(deleteAuthenticated("/media/" + mediaMetadata.getUuid()))
                .andExpect(status().isOk());

        // Vérification Révision
        assertEquals(previousRevision + 2, revisionService.getLatest());

        // Vérification de la persistence
        path = fileSystem.getPath(props.getBasePath(), MediaDao.ENTITY_NAME, mediaMetadata.getBundleTag(), mediaMetadata.getUuid() + ".yaml");
        assertFalse(Files.isRegularFile(path));

        // Vérification de la peristence de l'écriture
        path = fileSystem.getPath(props.getBaseResources(), mediaMetadata.getUrl().substring(mediaMetadata.getUrl().lastIndexOf("/") + 1));
        assertFalse(Files.isRegularFile(path));
    }
}
