package io.resourcepool.dashboard.bundlecontroller.put;

import io.resourcepool.dashboard.bundlecontroller.AbstractBundleControllerTest;
import io.resourcepool.dashboard.dto.BundleMetadataDto;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Camille Vrod on 15/06/16.
 */
public class BundleControllerPutTest extends AbstractBundleControllerTest {
    @Test
    public void putWithoutUUID() throws Exception {
        final String name = "Babar";
        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder().name(name).build();
        bundleMetadataDto.setUuid(null);

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void putWithWrongUuid() throws Exception {
        final String name = "Babar";
        BundleMetadataDto bundleMetadataDto = new BundleMetadataDto.Builder().uuid("random").name(name).build();
        bundleMetadataDto.setUuid(null);

        mockMvc.perform(putAuthenticated(("/bundle"))
                .content(toJson(bundleMetadataDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
