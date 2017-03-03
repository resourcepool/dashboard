package io.resourcepool.dashboard.service.impl;

import io.resourcepool.dashboard.dao.FeedDao;
import io.resourcepool.dashboard.model.Revision;
import io.resourcepool.dashboard.model.metadata.FeedMetaData;
import io.resourcepool.dashboard.service.DeviceService;
import io.resourcepool.dashboard.service.FeedService;
import io.resourcepool.dashboard.service.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 03/03/2017
 */
@Service
public class FeedServiceImpl implements FeedService {
  
  @Autowired
  private RevisionService revisionService;
  
  @Autowired
  private FeedDao feedDao;
  
  @Autowired
  private DeviceService deviceService;
  
  @Override
  public List<FeedMetaData> getAll() {
    return feedDao.getAll();
  }

  @Override
  public void save(FeedMetaData feedMetaData) {
    // Save is ALWAYS a new UUID as bundle is immutable
    feedMetaData.setUuid(UUID.randomUUID().toString());
    // Save new bundle
    feedDao.save(feedMetaData);
    revisionService.add(Revision.Type.FEED, Revision.Action.ADD, feedMetaData.getUuid());
  }

  @Override
  public void update(FeedMetaData feedMetaData) {
    // Save is NOT a new UUID as bundle is mutable
    feedDao.delete(feedMetaData.getUuid());
    feedDao.save(feedMetaData);
    // Tell revision service we updated a feed
    revisionService.add(Revision.Type.FEED, Revision.Action.UPDATE, feedMetaData.getUuid());
  }

  @Override
  public void delete(String uuid) {
    feedDao.delete(uuid);
    // Create a new revision
    revisionService.add(Revision.Type.FEED, Revision.Action.DELETE, uuid);
    deviceService.removeFeed(uuid);
    
  }

  @Override
  public FeedMetaData get(String uuid) {
    return feedDao.get(uuid);
  }

  @Override
  public void replaceTag(String oldTag, String newTag) {
    List<FeedMetaData> byBundleTag = feedDao.getByBundleTag(oldTag);
    if (byBundleTag == null) {
      return;
    }
    // Update each with new tag
    byBundleTag.forEach(feedMetaData -> {
      int i = feedMetaData.getBundleTags().indexOf(oldTag);
      feedMetaData.getBundleTags().set(i, newTag);
      update(feedMetaData);
    });
  }

  @Override
  public void deleteBundleTag(String tag) {
    List<FeedMetaData> byBundleTag = feedDao.getByBundleTag(tag);
    if (byBundleTag == null) {
      return;
    }
    // Update each without specific tag
    for (FeedMetaData feedMetaData : byBundleTag) {
      feedMetaData.getBundleTags().remove(tag);
      update(feedMetaData);
    }
  }

}
