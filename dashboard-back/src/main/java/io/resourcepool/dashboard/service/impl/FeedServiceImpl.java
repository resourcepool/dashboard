package io.resourcepool.dashboard.service.impl;

import io.resourcepool.dashboard.dao.FeedDao;
import io.resourcepool.dashboard.model.Revision;
import io.resourcepool.dashboard.model.metadata.Feed;
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
  public List<Feed> getAll() {
    return feedDao.getAll();
  }

  @Override
  public void save(Feed feed) {
    // Save is ALWAYS a new UUID as bundle is immutable
    feed.setUuid(UUID.randomUUID().toString());
    // Save new bundle
    feedDao.save(feed);
    revisionService.add(Revision.Type.FEED, Revision.Action.ADD, feed.getUuid());
  }

  @Override
  public void update(Feed feed) {
    // Save is NOT a new UUID as bundle is mutable
    feedDao.delete(feed.getUuid());
    feedDao.save(feed);
    // Tell revision service we updated a feed
    revisionService.add(Revision.Type.FEED, Revision.Action.UPDATE, feed.getUuid());
  }

  @Override
  public void delete(String uuid) {
    feedDao.delete(uuid);
    // Create a new revision
    revisionService.add(Revision.Type.FEED, Revision.Action.DELETE, uuid);
    deviceService.removeFeed(uuid);

  }

  @Override
  public Feed get(String uuid) {
    return feedDao.get(uuid);
  }

  @Override
  public void replaceTag(String oldTag, String newTag) {
    List<Feed> byBundleTag = feedDao.getByBundleTag(oldTag);
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
    List<Feed> byBundleTag = feedDao.getByBundleTag(tag);
    if (byBundleTag == null) {
      return;
    }
    // Update each without specific tag
    for (Feed feed : byBundleTag) {
      feed.getBundleTags().remove(tag);
      update(feed);
    }
  }

}
