package com.excilys.shooflers.dashboard.server.bundlecontroller.create;

import com.excilys.shooflers.dashboard.server.bundlecontroller.AbstractBundleControllerTest;
import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.service.impl.BundleServiceImpl;
import com.excilys.shooflers.dashboard.server.util.StringUtils;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Camille Vrod on 15/06/16.
 */
public class BundleControllerCreateTest extends AbstractBundleControllerTest {

    @Test
    public void failedWithEmptyBody1() throws Exception {
        mockMvc.perform(postAuthenticated(("/bundle")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void successWithBasic() throws Exception {
        long n = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();
        final String name = "UnNom";

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().name(name).build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);

        assertEquals(name, bundleMetadataDto.getName());
        assertNull(bundleMetadataDto.getValidity());
        assertNotNull(bundleMetadataDto.getUuid());
        assertEquals(StringUtils.normalize(name), bundleMetadataDto.getTag());

        // Vérification de la persistence
        assertNotNull(bundleService.getByTag(bundleMetadataDto.getTag()));
        Path path = fileSystem.getPath(props.getBasePath(), BundleDao.ENTITY_NAME, bundleMetadataDto.getUuid() + ".yaml");
        assertTrue(Files.isRegularFile(path));
        assertEquals(n + 1, bundleService.getAll().size());

        // Vérification de la récision
        assertEquals(previousRevision + 1, revisionService.getLatest());

        List<Revision> revisions = revisionService.getDiffs(previousRevision);
        assertThat(revisions, IsCollectionWithSize.hasSize(1));

        Revision revision = revisions.get(0);
        assertEquals(revision.getAction(), Revision.Action.ADD);
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), bundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void successWithComplete() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "UnNom";
        final String tag = "tag";
        final LocalDateTime startDateTime = LocalDateTime.now().minusMonths(3).minusDays(10);
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(10);

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().name(name).tag(tag).validity(makeValidityDto(startDateTime, endDateTime)).build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);

        // Vérification données de retour
        assertEquals(name, bundleMetadataDto.getName());
        assertEquals(tag, bundleMetadataDto.getTag());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getStart()));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getEnd()));
        assertNotNull(bundleMetadataDto.getUuid());

        // Vérification de la persistence
        Path path = fileSystem.getPath(props.getBasePath(), BundleDao.ENTITY_NAME, bundleMetadataDto.getUuid() + ".yaml");
        assertTrue(Files.isRegularFile(path));
        assertNotNull(bundleService.getByTag(tag));

        // Vérification du Révision
        assertEquals(previousRevision + 1, revisionService.getLatest());

    }

    @Test
    public void failedWithoutName() throws Exception {
        long n = bundleService.getAll().size();
        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals(n, bundleService.getAll().size());
    }

    @Test
    public void successButCantSetUuid() throws Exception {
        final String name = "Bouikbouik";
        final String chosenUuid = UUID.randomUUID().toString();

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().name(name).uuid(chosenUuid).build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);

        assertEquals(name, bundleMetadataDto.getName());
        assertNull(bundleMetadataDto.getValidity());
        assertNotEquals(chosenUuid, bundleMetadataDto.getUuid());

        assertNotNull(bundleService.getByTag(bundleMetadataDto.getTag()));
    }

    @Test
    public void failedStartAfterEnd() throws Exception {
        final String name = "Bouikbouik";

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder()
                        .name(name)
                        .validity(makeValidityDto(LocalDateTime.now(), LocalDateTime.now().minusDays(9)))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void successWithValidityWithoutStartWithoutEnd() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, null))
                .build();
        assertNotNull(bundleMetadataDto.getValidity());

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNull(bundleMetadataDtoAfter.getValidity());

        assertNotNull(bundleService.getByTag(StringUtils.normalize(name)));
    }

    @Test
    public void successWithValidityWithStartWithoutEnd() throws Exception {
        final String name = "Bouikbouik";
        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(6);

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(startDateTime, null))
                .build();
        assertNotNull(bundleMetadataDto.getValidity());

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertNull(bundleMetadataDtoAfter.getValidity().getEnd());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()));

        assertNotNull(bundleService.getByTag(StringUtils.normalize(name)));
    }

    @Test
    public void successWithValidityWithoutStartWithEnd() throws Exception {
        final String name = "Bouikbouik";
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(9);

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, endDateTime))
                .build();
        MvcResult result = mockMvc.perform(postAuthenticated("/bundle")
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertThat(toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()), allOf(LocalDateTimeMatchers.before(LocalDateTime.now().plus(Duration.ofSeconds(1))), LocalDateTimeMatchers.after(LocalDateTime.now().minus(Duration.ofSeconds(1)))));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getEnd()));

        assertNotNull(bundleService.getByTag(StringUtils.normalize(name)));
    }

    @Test
    public void failedWithValidityWithoutStartWithEndAnteriorNow() throws Exception {
        final String name = "Bouikbouik";
        long n = bundleService.getAll().size();

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, LocalDateTime.now().minusDays(9)))
                .build();
        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals(n, bundleService.getAll().size());
    }

    @Test
    public void failedWithWrongFormatStart() throws Exception {
        final String name = "Bouikbouik";
        final long size = bundleService.getAll().size();

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, null))
                .build();
        bundleMetadataDto.getValidity().setStart("zesljkl");
        assertNotNull(bundleMetadataDto.getValidity());

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals(size, bundleService.getAll().size());
    }

    @Test
    public void failedWithWrongFormatEnd() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(LocalDateTime.now(), null))
                .build();
        bundleMetadataDto.getValidity().setEnd("zesljkl");
        assertNotNull(bundleMetadataDto.getValidity());

        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    public void failedIfUnknownProperty() throws Exception {
        mockMvc.perform(postAuthenticated("/bundle")
                .content("{unknown:null}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void failedWithTagAlreadyUsed() throws Exception {
        successWithComplete();

        final long n = bundleService.getAll().size();
        final long previousRevision = revisionService.getLatest();
        final String name = "UnNom";
        final String tag = "tag";
        final LocalDateTime startDateTime = LocalDateTime.now().minusMonths(3).minusDays(10);
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(10);

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().name(name).tag(tag).validity(makeValidityDto(startDateTime, endDateTime)).build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Vérification du retour
        assertEquals(BundleServiceImpl.ERROR_TAG_ALREADY_EXISTS, result.getResponse().getContentAsString());

        // Vérification du Révision
        assertEquals(previousRevision, revisionService.getLatest());

        //
        assertEquals(n, bundleService.getAll().size());
    }

}
