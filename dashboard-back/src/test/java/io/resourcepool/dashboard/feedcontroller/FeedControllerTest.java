package io.resourcepool.dashboard.feedcontroller;

import edu.emory.mathcs.backport.java.util.Arrays;
import io.resourcepool.dashboard.AbstractControllerTest;
import io.resourcepool.dashboard.dto.FeedDto;
import io.resourcepool.dashboard.model.metadata.BundleMetadata;
import io.resourcepool.dashboard.service.BundleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedControllerTest extends AbstractControllerTest {

  @Autowired
  protected BundleService bundleService;


  @Test
  public void successCreate() throws Exception {
    BundleMetadata bundle = new BundleMetadata.Builder().name("test").tag("test").build();
    bundleService.save(bundle);
    // Simply create a new feed and check its model
    MvcResult result = mockMvc.perform(postAuthenticated(("/feed"))
      .content(toJson(FeedDto.builder().name("test").bundleTags(Arrays.asList(new String[]{bundle.getTag()})).build()))
      .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk())
      .andReturn();

    FeedDto feed = fromJson(result.getResponse().getContentAsString(), FeedDto.class);

    assertEquals("test", feed.getName());
    assertNotNull(feed.getBundleTags());
    assertEquals(1, feed.getBundleTags().size());
    assertEquals(bundle.getTag(), feed.getBundleTags().get(0));
  }

  @Test
  public void createWithoutBundle() throws Exception {
    
    // Simply create a new feed and check its model
    MvcResult result = mockMvc.perform(postAuthenticated(("/feed"))
      .content(toJson(FeedDto.builder().name("test").build()))
      .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void deleteSuccess() throws Exception {

    BundleMetadata bundle = new BundleMetadata.Builder().name("test").tag("test").build();
    bundleService.save(bundle);
    
    // Simply create a new feed and check its model
    MvcResult result = mockMvc.perform(postAuthenticated(("/feed"))
      .content(toJson(FeedDto.builder().name("test").bundleTags(Arrays.asList(new String[]{bundle.getTag()})).build()))
      .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk())
      .andReturn();

    FeedDto feed = fromJson(result.getResponse().getContentAsString(), FeedDto.class);

    // Delete active feed
    mockMvc.perform(deleteAuthenticated(("/feed/" + feed.getUuid()))
      .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk())
      .andReturn();
    
    // Check feed has been deleted indeed
    result = mockMvc.perform(getAuthenticated(("/feed"))
      .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk())
      .andReturn();

    assertEquals("[]", result.getResponse().getContentAsString());
    
  }
  
}
