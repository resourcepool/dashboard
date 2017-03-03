package io.resourcepool.dashboard.bundlecontroller.edit;

import io.resourcepool.dashboard.bundlecontroller.AbstractBundleControllerTest;
import io.resourcepool.dashboard.dto.BundleMetadataDto;
import io.resourcepool.dashboard.dto.MediaMetadataDto;
import io.resourcepool.dashboard.model.Media;
import io.resourcepool.dashboard.model.Revision;
import io.resourcepool.dashboard.model.metadata.BundleMetadata;
import io.resourcepool.dashboard.model.metadata.MediaMetadata;
import io.resourcepool.dashboard.service.MediaService;
import io.resourcepool.dashboard.service.impl.BundleServiceImpl;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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

    private BundleMetadata formBundleMetadata;

    private BundleMetadataDto formBundleMetadataDto;

    @Autowired
    private MediaService mediaService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        // Créer un bundle de base pour l'édition
        formBundleMetadata = new BundleMetadata.Builder()
                .name("baseBundleMetadata")
                .tag("baseTag")
                .build();
        bundleService.save(formBundleMetadata);

        assertNotNull(formBundleMetadata.getUuid());

        formBundleMetadataDto = bundleDtoMapper.toDto(formBundleMetadata);
    }

    @Test
    public void successBasic() throws Exception {
        final String newName = "Nouveau Nom";

        final String formName = formBundleMetadataDto.getName();
        final long previousRevision = revisionService.getLatest();
        final long previousSize = bundleService.getAll().size();

        assertNotEquals("Choose a newName different", newName, formName);
        assertNull(formBundleMetadataDto.getValidity());

        formBundleMetadataDto.setName(newName);

        MvcResult result = mockMvc.perform(putAuthenticated("/bundle")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJson(formBundleMetadataDto)))
                .andExpect(status().isOk()).andReturn();

        BundleMetadataDto newBundleMetadataDto = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(newName, newBundleMetadataDto.getName());
        assertEquals(formBundleMetadataDto.getTag(), newBundleMetadataDto.getTag());
        assertNull(newBundleMetadataDto.getValidity());
        assertNotEquals(newBundleMetadataDto.getUuid(), formBundleMetadataDto.getUuid());
        assertNull(newBundleMetadataDto.getValidity());
        assertNull(formBundleMetadataDto.getValidity());

        assertNotNull(bundleService.getByTag(formBundleMetadataDto.getTag()));
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
        assertEquals(revision.getTarget(), formBundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);

        revision = revisions.get(0);
        assertEquals(Revision.Action.ADD, revision.getAction());
        assertEquals(((long) revision.getRevision()), previousRevision + 1);
        assertEquals(revision.getType(), Revision.Type.BUNDLE);
        assertEquals(revision.getTarget(), newBundleMetadataDto.getUuid());
        assertEquals(revision.getResult(), null);
    }

    @Test
    public void successOnValidity() throws Exception {
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
        assertEquals(formBundleMetadataDto.getName(), bundleMetadataDto.getName());
        assertEquals(formBundleMetadataDto.getTag(), bundleMetadataDto.getTag());
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
    public void failedWithoutName() throws Exception {
        formBundleMetadataDto.setName(null);

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void failedWithStartAfterEnd() throws Exception {
        final String name = "Bouikbouik";

        formBundleMetadataDto.setValidity(makeValidityDto(LocalDateTime.now(), LocalDateTime.now().minusDays(9)));
        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void succesWithValidityWithoutStartWithoutEnd() throws Exception {
        formBundleMetadataDto.setValidity(makeValidityDto(null, null));
        assertNotNull(formBundleMetadataDto.getValidity());

        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(formBundleMetadataDto.getName(), bundleMetadataDtoAfter.getName());
        assertNull(bundleMetadataDtoAfter.getValidity());
    }

    @Test
    public void succesWithValidityWithStartWithoutEnd() throws Exception {
        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(6);

        formBundleMetadataDto.setValidity(makeValidityDto(startDateTime, null));

        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(formBundleMetadataDto.getName(), bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertNull(bundleMetadataDtoAfter.getValidity().getEnd());
        assertEquals(startDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()));
    }

    @Test
    public void succesWithValidityWithoutStartWithEnd() throws Exception {
        final LocalDateTime endDateTime = LocalDateTime.now().plusDays(9);

        formBundleMetadataDto.setValidity(makeValidityDto(null, endDateTime));

        MvcResult result = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(formBundleMetadataDto.getName(), bundleMetadataDtoAfter.getName());
        assertNotNull(bundleMetadataDtoAfter.getValidity());
        assertThat(toLocalDateTime(bundleMetadataDtoAfter.getValidity().getStart()), allOf(LocalDateTimeMatchers.before(LocalDateTime.now().plus(Duration.ofSeconds(1))), LocalDateTimeMatchers.after(LocalDateTime.now().minus(Duration.ofSeconds(1)))));
        assertEquals(endDateTime, toLocalDateTime(bundleMetadataDtoAfter.getValidity().getEnd()));
    }

    @Test
    public void failedWithValidityWithoutStartWithEndAnteriorNow() throws Exception {
        formBundleMetadataDto.setValidity(makeValidityDto(null, LocalDateTime.now().minusDays(9)));

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void failedWithWrongFormatStart() throws Exception {
        formBundleMetadataDto.setValidity(makeValidityDto(null, null));
        formBundleMetadataDto.getValidity().setStart("zesljkl");

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void failedWithWrongFormatEnd() throws Exception {
        formBundleMetadataDto.setValidity(makeValidityDto(LocalDateTime.now(), null));
        formBundleMetadataDto.getValidity().setEnd("zesljkl");

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void failedIfUnknownProperty() throws Exception {
        mockMvc.perform(putAuthenticated(("/bundle"))
                .content("{tag:null, unknown:null}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void failedWithTagAlreadyUsed() throws Exception {
        // Création d'un bundle avec le tag réservé
        final String tagUsed = "tagUsed";
        final String secondName = "secondName";
        bundleService.save(new BundleMetadata.Builder().name(secondName).tag(tagUsed).build());

        formBundleMetadataDto.setTag(tagUsed);

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(BundleServiceImpl.ERROR_TAG_ALREADY_EXISTS))
                .andReturn();
    }

    @Test
    public void successWithNewTagWithEmpty() throws Exception {
        final String newTag = "newTag";
        assertNotEquals(newTag, formBundleMetadataDto.getName());

        formBundleMetadataDto.setTag(newTag);

        MvcResult result = mockMvc.perform(putAuthenticated("/bundle")
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDtoAfter = fromJson(result.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertEquals(formBundleMetadataDto.getName(), bundleMetadataDtoAfter.getName());
        assertEquals(formBundleMetadataDto.getValidity(), bundleMetadataDtoAfter.getValidity());
        assertEquals(newTag, bundleMetadataDtoAfter.getTag());

        assertNull(bundleService.getByTag(formBundleMetadata.getTag()));
        assertNotNull(bundleService.getByTag(bundleMetadataDtoAfter.getTag()));
    }

    @Test
    public void successWithContent() throws Exception {
        // Créer d'un Media sans fichier
        Media media1 = Media.builder()
                .metadata(MediaMetadata.builder()
                        .name("Jojo")
                        .mediaType(io.resourcepool.dashboard.model.type.MediaType.WEB)
                        .bundleTag(formBundleMetadata.getTag())
                        .url("http://www.jba-france.fr/")
                        .build())
                .build();

        mediaService.save(media1);

        // Création d'un Media avec fichier
        final String name = "Zelda";
        final io.resourcepool.dashboard.model.type.MediaType mediaType = io.resourcepool.dashboard.model.type.MediaType.IMAGE;
        final MockMultipartFile jsonFile = new MockMultipartFile("file", "texte.jpeg", "image/jpeg", "{json:null}".getBytes());
        MvcResult result = mockMvc.perform(fileUploadAuthenticated("/media")
                .file(jsonFile)
                .param("media", toJson(new MediaMetadataDto.Builder()
                        .name(name)
                        .mediaType(mediaType)
                        .bundleTag(formBundleMetadata.getTag())
                        .build()))
        )
                .andExpect(status().isOk())
                .andReturn();
        MediaMetadataDto mediaMetadataDto = fromJson(result.getResponse().getContentAsString(), MediaMetadataDto.class);

        final long previousBundleSize = bundleService.getAll().size();
        final long previousMediaSize = mediaService.getAll().size();
        final long previousRevision = revisionService.getLatest();

        assertThat(bundleService.getAll().size(), Matchers.greaterThanOrEqualTo(1));

        BundleMetadata bundleMetadata = bundleService.getAll().get(bundleService.getAll().size() - 1);
        BundleMetadataDto bundleMetaDataDto = bundleDtoMapper.toDto(bundleMetadata);

        assertNotNull(bundleService.getByTag(bundleMetaDataDto.getTag()));
        assertNotNull(mediaService.get(media1.getMetadata().getUuid()));
        assertNotNull(mediaService.get(mediaMetadataDto.getUuid()));

        final String secondBundleName = "secondBundleName";
        formBundleMetadata.setName(secondBundleName);

        MvcResult mvcResult = mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(formBundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        BundleMetadataDto bundleMetadataDto = fromJson(mvcResult.getResponse().getContentAsString(), BundleMetadataDto.class);
        assertNotEquals(mediaMetadataDto.getUuid(), formBundleMetadata.getUuid());

        assertNotNull(bundleService.getByTag(bundleMetaDataDto.getTag()));
        assertNotNull(mediaService.get(media1.getMetadata().getUuid()));
        assertNotNull(mediaService.get(mediaMetadataDto.getUuid()));

        assertEquals(previousBundleSize, bundleService.getAll().size());
        assertEquals(previousMediaSize, mediaService.getAll().size());
        assertEquals(previousRevision + 1, revisionService.getLatest());
        assertEquals(2, mediaService.getByBundleTag(bundleMetadata.getTag()).size());
    }
}
