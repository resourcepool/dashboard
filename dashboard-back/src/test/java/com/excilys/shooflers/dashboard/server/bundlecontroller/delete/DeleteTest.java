package com.excilys.shooflers.dashboard.server.bundlecontroller.delete;

import com.excilys.shooflers.dashboard.server.bundlecontroller.BundleControllerTest;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Camille Vrod on 15/06/16.
 */
public class DeleteTest extends BundleControllerTest {

    @Test
    @Ignore("NullPointerException on mockMvc.perform")
    public void deleteUnknown() throws Exception {
        final long previousSize = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();

        mockMvc.perform(deleteAuthenticated("/bundle/wronguid"))
                .andExpect(status().isNotFound());
        assertEquals(previousSize, bundleService.getAll().size());
        assertEquals(previousRevision, revisionService.getLatest());
    }

    @Test
    public void deleteBundle() throws Exception {

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name("ToDelete").build();
        bundleService.save(formBundleMetadata);

        final long previousSize = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();

        assertThat(bundleService.getAll().size(), Matchers.greaterThanOrEqualTo(1));

        BundleMetadata bundleMetadata = bundleService.getAll().get(bundleService.getAll().size() - 1);
        BundleMetadataDto bundleMetaDataDto = bundleDtoMapper.toDto(bundleMetadata);


        assertNotNull(bundleService.getByTag(bundleMetaDataDto.getTag()));

        mockMvc.perform(deleteAuthenticated("/bundle/" + bundleMetaDataDto.getUuid()))
                .andExpect(status().isOk());

        assertNotNull(bundleService.getByTag(bundleMetaDataDto.getTag()));

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
    }

    @Test
    @Ignore("Not yet implemented...")
    public void bundleDeleteContainingMedia() throws Exception {
        fail();
    }
}
