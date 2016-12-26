package com.excilys.shooflers.dashboard.server.bundlecontroller.edit;

import com.excilys.shooflers.dashboard.server.bundlecontroller.AbstractBundleControllerTest;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Camille Vrod on 15/06/16.
 */
public class BundleControllerEditTest extends AbstractBundleControllerTest {

    @Test
    public void editBundleSuccessBasic() throws Exception {
        final String newName = "Nouveau Nom";
        BundleMetadata bundleMetadata = new BundleMetadata.Builder().name("Name").build();
        bundleService.save(bundleMetadata);

        BundleMetadataDto bundleMetadataDto = bundleDtoMapper.toDto(bundleMetadata);

        final String formName = bundleMetadataDto.getName();
        final long previousRevision = revisionService.getLatest();
        final long previousSize = bundleService.getAll().size();

        assertNotEquals("Choose a newName different", newName, formName);
        assertNull(bundleMetadataDto.getValidity());

        bundleMetadataDto.setName(newName);

        MvcResult result = mockMvc.perform(putAuthenticated("/bundle")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJson(bundleMetadataDto)))
                .andExpect(status().isOk()).andReturn();

        BundleMetadataDto newBundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(newName, newBundleMetadataDto.getName());
        assertNull(newBundleMetadataDto.getValidity());
        assertNotEquals(newBundleMetadataDto.getUuid(), bundleMetadataDto.getUuid());
        assertNull(newBundleMetadataDto.getValidity());
        assertNull(bundleMetadataDto.getValidity());

        assertNotNull(bundleService.getByTag(bundleMetadataDto.getTag()));
        assertNotNull(bundleService.getByTag(newBundleMetadataDto.getTag()));

        assertEquals(previousSize, bundleService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        revisions.sort((revision1, revision2) -> revision1.getAction().compareTo(revision2.getAction()));
        assertThat(revisions, Matchers.hasSize(2));
        System.out.println(revisions);

        Revision revision = revisions.get(1);
        assertEquals(Revision.Action.DELETE, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

        revision = revisions.get(0);
        assertEquals(Revision.Action.ADD, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), newBundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void editBundleSuccessOnValidity() throws Exception {
        final String name = "AncienNom";

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        final LocalDateTime startDateTime = LocalDateTime.now();
        final long previousRevision = revisionService.getLatest();
        final long previousSize = bundleService.getAll().size();

        assertNull(formBundleMetadataDto.getValidity());

        formBundleMetadataDto.setValidity(makeValidityDto(startDateTime, null));

        MvcResult result = mockMvc.perform(putAuthenticated("/bundle")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJson(formBundleMetadataDto)))
                .andExpect(status().isOk()).andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDto.getName());
        assertNotNull(bundleMetadataDto.getValidity());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getStart()));
        assertNull(bundleMetadataDto.getValidity().getEnd());
        assertNotEquals(bundleMetadataDto.getUuid(), formBundleMetadataDto.getUuid());
        assertEquals(bundleMetadataDto.getValidity().getStart(), formBundleMetadataDto.getValidity().getStart());
        assertEquals(bundleMetadataDto.getValidity().getEnd(), formBundleMetadataDto.getValidity().getEnd());

        assertNotNull(bundleService.getByTag(formBundleMetadataDto.getTag()));
        assertNotNull(bundleService.getByTag(bundleMetadataDto.getTag()));

        assertEquals(previousSize, bundleService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        revisions.sort((revision1, revision2) -> revision1.getAction().compareTo(revision2.getAction()));
        assertThat(revisions, Matchers.hasSize(2));
        System.out.println(revisions);

        Revision revision = revisions.get(1);
        assertEquals(Revision.Action.DELETE, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), formBundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

        revision = revisions.get(0);
        assertEquals(Revision.Action.ADD, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void editBundleFailedWithoutName() throws Exception {
        final String name = "Toupoutou";
        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        formBundleMetadataDto.setName(null);

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void editBundleFailedStartAfterEnd() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        formBundleMetadataDto.setValidity(makeValidityDto(LocalDateTime.now(), LocalDateTime.now().minusDays(9)));
        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void editBundleSuccesWithValidityWithoutStartWithoutEnd() throws Exception {
        final String name = "Jojo";

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        formBundleMetadataDto.setValidity(makeValidityDto(null, null));
        assertNotNull(formBundleMetadataDto.getValidity());

        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNull(bundleMetadataDtoAfter.getValidity());
    }

    @Test
    public void editBundleSuccesWithValidityWithStartWithoutEnd() throws Exception {
        final String name = "Jojo";

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(6);

        formBundleMetadataDto.setValidity(makeValidityDto(startDateTime, null));


        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertNull(bundleMetadataDtoAfter.getValidity().getEnd());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()));
    }

    @Test
    public void editBundleSuccesWithValidityWithoutStartWithEnd() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(9);

        formBundleMetadataDto.setValidity(makeValidityDto(null, endDateTime));

        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertThat(toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()), allOf(LocalDateTimeMatchers.before(LocalDateTime.now().plus(Duration.ofSeconds(1))), LocalDateTimeMatchers.after(LocalDateTime.now().minus(Duration.ofSeconds(1)))));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getEnd()));
    }

    @Test
    public void editBundleFailedWithValidityWithoutStartWithEndAnteriorNow() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        formBundleMetadataDto.setValidity(makeValidityDto(null, LocalDateTime.now().minusDays(9)));

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void editBundleFailedWithWrongFormatStart() throws Exception {
        final String name = "Bunduru";

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        formBundleMetadataDto.setValidity(makeValidityDto(null, null));
        formBundleMetadataDto.getValidity().setStart("zesljkl");

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void editFailedWithWrongFormatEnd() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadata formBundleMetadata = new BundleMetadata.Builder().name(name).build();
        bundleService.save(formBundleMetadata);

        BundleMetadataDto formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);

        formBundleMetadataDto.setValidity(makeValidityDto(LocalDateTime.now(), null));
        formBundleMetadataDto.getValidity().setEnd("zesljkl");

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void editBundleCreateFailedIfUnknownProperty() throws Exception {
        mockMvc.perform(putAuthenticated(("/bundle"))
                .content("{tag:null, unknown:null}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

}
