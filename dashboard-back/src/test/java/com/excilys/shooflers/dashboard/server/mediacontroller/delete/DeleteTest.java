package com.excilys.shooflers.dashboard.server.mediacontroller.delete;

import com.excilys.shooflers.dashboard.server.DashboardApplication;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.mediacontroller.MediaControllerTest;
import com.excilys.shooflers.dashboard.server.model.Media;
import com.excilys.shooflers.dashboard.server.model.Revision;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Florian Castelain
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class DeleteTest extends MediaControllerTest {

    // ============================================================
    //	Tests
    // ============================================================

    @Test
    public void deleteByBadFormUuid() throws Exception {
        final long previousRevision = revisionService.getLatest();
        mockMvc.perform(deleteAuthenticated("/media/epzirpeoi"))
                .andExpect(status().isNotFound())
        ;
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void deleteByWrongUuid() throws Exception {
        final long previousRevision = revisionService.getLatest();
        mockMvc.perform(deleteAuthenticated("/media/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
        ;
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void deleteWebSuccess() throws Exception {
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

        final long previousRevision = revisionService.getLatest();

        mockMvc.perform(getAuthenticated("/media/" + m.getMetadata().getUuid()))
                .andExpect(status().isOk());

        mockMvc.perform(deleteAuthenticated("/media/" + m.getMetadata().getUuid()))
                .andExpect(status().isOk());

        mockMvc.perform(getAuthenticated("/media/" + m.getMetadata().getUuid()))
                .andExpect(status().isNotFound());

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

}
