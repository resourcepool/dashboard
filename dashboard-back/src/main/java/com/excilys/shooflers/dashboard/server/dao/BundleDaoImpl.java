package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Component
public class BundleDaoImpl implements BundleDao {
  
  
  private static final Logger LOGGER = LoggerFactory.getLogger(BundleDaoImpl.class);
  
  @Autowired
  private DashboardProperties props;
  
  private Path bundleDatabasePath;
  
  @PostConstruct
  public void init() {
    bundleDatabasePath = Paths.get(props.getBasePath(), ENTITY_NAME);
  }
  
  @Override
  public BundleMetadata get(String uuid) {
    File dataFile = getBundleFile(uuid);
    return readBundleFromFile(dataFile);
  }

  @Override
  public List<BundleMetadata> getAll() {
    List<BundleMetadata> bundles = new LinkedList<>();
    for (File b : bundleDatabasePath.toFile().listFiles(File::isFile)) {
      bundles.add(readBundleFromFile(b));
    }
    return bundles;
  }

  @Override
  public void save(BundleMetadata bundle) {
    if (bundle.getUuid() == null) {
      bundle.setUuid(UUID.randomUUID().toString());
    }
    
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    File dest = getBundleFile(bundle.getUuid());
    FileOutputStream fos = null;
    try {
      if (!dest.exists()) {
        dest.createNewFile();
      }
      fos = new FileOutputStream(dest);
      mapper.writeValue(fos, bundle);
    } catch (IOException e) {
      LOGGER.warn("Error while storing file", e);
      throw new IllegalStateException(e);
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException e) {
          LOGGER.warn("Error while closing file", e);
          throw new IllegalStateException(e);
        }
      }
    }
  }
  
  private File getBundleFile(String uuid) {
    String dataFileName = uuid + ".yaml";
    return bundleDatabasePath.resolve(dataFileName).toFile();
  }
  

  private BundleMetadata readBundleFromFile(File dataFile) {
    try {
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      InputStream data = new FileInputStream(dataFile);
      BundleMetadata metadata = mapper.readValue(data, BundleMetadata.class);
      return metadata;
    } catch (java.io.IOException e) {
      LOGGER.warn("Error while retrieving file", e);
      throw new IllegalStateException(e);
    }
  }

}
