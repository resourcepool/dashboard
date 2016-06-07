package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Component
public class DaoInitializer {


  private static final Logger LOGGER = LoggerFactory.getLogger(DaoInitializer.class);
  
  @Autowired
  private DashboardProperties props;
  
  @PostConstruct
  public void init() {
    Path dbPath = Paths.get(props.getBasePath());
    if (!Files.exists(dbPath)) {
      String err = "The bundle database path does not exist. Please check your application properties";
      LOGGER.error(err);
      throw new IllegalStateException(err);
    }
    
    createEntityDbs(dbPath, BundleDao.ENTITY_NAME);
    createEntityDbs(dbPath, MediaDao.ENTITY_NAME);
  }
  
  private void createEntityDbs(Path basePath, String entity) {
    Path p = basePath.resolve(entity);
    if (!Files.exists(p)) {
      try {
        Files.createDirectory(p);
      } catch (IOException e) {
        String err = "The database failed to create a directory. Please check you have the right permissions";
        LOGGER.error(err);
        throw new IllegalStateException(err);
      }
    }
    
  }
}
