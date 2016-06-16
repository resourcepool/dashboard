package com.excilys.shooflers.dashboard.server.bundlecontroller.create;

import com.excilys.shooflers.dashboard.server.DashboardApplication;
import com.excilys.shooflers.dashboard.server.bundlecontroller.BundleControllerTest;
import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.model.Revision;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Camille Vrod on 15/06/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DashboardApplication.class)
@WebAppConfiguration
public class CreateTest extends BundleControllerTest {

    @Test
    public void bundleCreateEmptyBody1() throws Exception {
        mockMvc.perform(postAuthenticated(("/bundle")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void bundleCreateBasicSuccess() throws Exception {
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

        File file = new File(props.getBasePath() + "/" + BundleDao.ENTITY_NAME + "/" + bundleMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

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
    public void bundleCreateCompleteSuccess() throws Exception {
        final long previousRevision = revisionService.getLatest();
        final String name = "UnNom";
        final LocalDateTime startDateTime = LocalDateTime.now().minusMonths(3).minusDays(10);
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(10);

        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().name(name).validity(makeValidityDto(startDateTime, endDateTime)).build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);

        assertEquals(name, bundleMetadataDto.getName());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getStart()));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDto.getValidity().getEnd()));
        assertNotNull(bundleMetadataDto.getUuid());

        File file = new File(props.getBasePath() + "/" + BundleDao.ENTITY_NAME + "/" + bundleMetadataDto.getUuid() + ".yaml");
        assertTrue(file.isFile());

        assertEquals(previousRevision + 1, revisionService.getLatest());
    }

    @Test
    public void bundleCreateFailedWithoutName() throws Exception {
        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(new BundleMetadataDto.Builder().build()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void bundleCreateSuccessButCantSetUuid() throws Exception {
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
    }

    @Test
    public void bundleCreateFailedStartAfterEnd() throws Exception {
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
    public void bundleCreateSuccesWithValidityWithoutStartWithoutEnd() throws Exception {
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
    }

    @Test
    public void bundleCreateSuccesWithValidityWithStartWithoutEnd() throws Exception {
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
    }

    @Test
    public void bundleCreateSuccesWithValidityWithoutStartWithEnd() throws Exception {
        final String name = "Bouikbouik";
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(9);

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, endDateTime))
                .build();
        MvcResult result = mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(name, bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertThat(toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()), allOf(LocalDateTimeMatchers.before(LocalDateTime.now().plus(Duration.ofSeconds(1))), LocalDateTimeMatchers.after(LocalDateTime.now().minus(Duration.ofSeconds(1)))));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getEnd()));
    }

    @Test
    public void bundleCreateFailedWithValidityWithoutStartWithEndAnteriorNow() throws Exception {
        final String name = "Bouikbouik";

        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder()
                .name(name)
                .validity(makeValidityDto(null, LocalDateTime.now().minusDays(9)))
                .build();
        mockMvc.perform(postAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void bundleCreateFailedWithWrongFormatStart() throws Exception {
        final String name = "Bouikbouik";

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
    }

    @Test
    public void bundleCreateFailedWithWrongFormatEnd() throws Exception {
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
    public void bundleCreateFailedIfUnknownProperty() throws Exception {
        mockMvc.perform(postAuthenticated(("/bundle"))
                .content("{unknown:null}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
