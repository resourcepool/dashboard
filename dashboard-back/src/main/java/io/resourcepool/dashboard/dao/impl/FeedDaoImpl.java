package io.resourcepool.dashboard.dao.impl;

import io.resourcepool.dashboard.dao.FeedDao;
import io.resourcepool.dashboard.dao.util.YamlUtils;
import io.resourcepool.dashboard.exception.ResourceIoException;
import io.resourcepool.dashboard.model.metadata.FeedMetaData;
import io.resourcepool.dashboard.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Component
@DependsOn("daoInitializer")
public class FeedDaoImpl implements FeedDao {


  private static final Logger LOGGER = LoggerFactory.getLogger(FeedDaoImpl.class);

  @Autowired
  private DashboardProperties props;

  @Autowired
  private FileSystem fileSystem;

  private Path feedDatabasePath;

  @PostConstruct
  public void init() {
    feedDatabasePath = fileSystem.getPath(props.getBasePath(), ENTITY_NAME);
  }

  @Override
  public FeedMetaData get(String uuid) {
    Path dataFile = getFeedFile(uuid);
    return readFeedFromFile(dataFile);
  }

  @Override
  public List<FeedMetaData> getAll() {
    List<FeedMetaData> feedMetaDatas = new LinkedList<>();
    try {
      Files.walk(feedDatabasePath, 1)
        .filter(Files::isRegularFile)
        .forEach(path -> feedMetaDatas.add(readFeedFromFile(path)));
    } catch (IOException e) {
      LOGGER.error("exception in FeedDaoImpl#getAll", e);
      throw new ResourceIoException(e);
    }
    return feedMetaDatas;
  }

  @Override
  public FeedMetaData save(FeedMetaData feedMetaData) {
    if (feedMetaData.getUuid() == null) {
      feedMetaData.setUuid(UUID.randomUUID().toString());
    }
    Path dest = getFeedFile(feedMetaData.getUuid());
    YamlUtils.store(feedMetaData, dest);
    return feedMetaData;
  }

  @Override
  public FeedMetaData delete(String uuid) {
    FeedMetaData result = get(uuid);
    boolean success = YamlUtils.delete(feedDatabasePath.resolve(uuid + ".yaml"));
    if (!success) {
      throw new ResourceIoException();
    }
    return result;
  }

  @Override
  public List<FeedMetaData> getByBundleTag(String tag) {
    List<FeedMetaData> all = getAll();
    if (all == null || all.isEmpty()) {
      return null;
    }
    all.removeIf(feedMetaData -> !feedMetaData.getBundleTags().contains(tag));
    return all;
  }

  private Path getFeedFile(String uuid) {
    String dataFileName = uuid + ".yaml";
    return feedDatabasePath.resolve(dataFileName);
  }

  private FeedMetaData readFeedFromFile(Path path) {
    return YamlUtils.read(path, FeedMetaData.class);
  }
}
