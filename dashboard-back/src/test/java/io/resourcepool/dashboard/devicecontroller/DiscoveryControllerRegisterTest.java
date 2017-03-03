package io.resourcepool.dashboard.devicecontroller;

import io.resourcepool.dashboard.bundlecontroller.AbstractBundleControllerTest;
import io.resourcepool.dashboard.model.Device;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DiscoveryControllerRegisterTest extends AbstractBundleControllerTest {

    public static final String VALID_DEVICE_ID = "00:AC:DE:EF:CD"; 
    
    @Test
    public void failedWithEmptyBody1() throws Exception {
        mockMvc.perform(postAuthenticated(("/discovery")))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void successRegister() throws Exception {
        // Simply create a new device and check its model
        MvcResult result = mockMvc.perform(postApiKey(("/discovery/" + VALID_DEVICE_ID))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        
        Device device = fromJson(result.getResponse().getContentAsString(), Device.class);
        
        assertNotNull(device);
        assertEquals(device.getId(), VALID_DEVICE_ID);
        assertNotNull(device.getLastKnownIp());
        assertNotNull(device.getLastHealthCheck());
        assertNull(device.getName());
        assertNull(device.getFeedId());

    }

    @Test
    public void successUpdate() throws Exception {
        // Simply create a new device and check its model
        MvcResult result = mockMvc.perform(postApiKey(("/discovery/" + VALID_DEVICE_ID))
          .contentType(MediaType.APPLICATION_JSON_UTF8))
          .andExpect(status().isOk())
          .andReturn();

        Device deviceOld = fromJson(result.getResponse().getContentAsString(), Device.class);
        
        // Update device
        result = mockMvc.perform(postApiKey(("/discovery/" + VALID_DEVICE_ID))
          .contentType(MediaType.APPLICATION_JSON_UTF8))
          .andExpect(status().isOk())
          .andReturn();
        Device deviceNew = fromJson(result.getResponse().getContentAsString(), Device.class);
        
        assertTrue(deviceNew.getLastHealthCheck().isAfter(deviceOld.getLastHealthCheck()));
    }

}
