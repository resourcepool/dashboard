package io.resourcepool.dashboard.dao;

import io.resourcepool.dashboard.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Component
public class DaoInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoInitializer.class);

    @Autowired
    private FileSystem fs;

    @Autowired
    private DashboardProperties props;

    /**
     * Create base directories if not exist.
     */
    @PostConstruct
    public void init() {
        Path dbPath = fs.getPath(props.getBasePath());
        if (!Files.exists(dbPath)) {
            try {
                LOGGER.warn("Dashboard database path does not exist: {}. It will be created.", dbPath.toString());
                Files.createDirectories(dbPath);
            } catch (IOException e) {
                String err = "The dashboard database path does not exist. Please check your application properties";
                LOGGER.error(err);
                throw new IllegalStateException(err);
            }
        }

        Path dbRes = fs.getPath(props.getBaseResources());
        if (!Files.exists(dbRes)) {
            try {
                LOGGER.warn("Dashboard resources path does not exist: {}. It will be created.", dbRes.toString());
                Files.createDirectories(dbRes);
            } catch (IOException e) {
                String err = "The dashboard resources path does not exist. Please check your application properties";
                LOGGER.error(err);
                throw new IllegalStateException(err);
            }
        }

        createEntityDbs(dbPath, BundleDao.ENTITY_NAME);
        createEntityDbs(dbPath, MediaDao.ENTITY_NAME);
        createEntityDbs(dbPath, RevisionDao.ENTITY_NAME);
        createEntityDbs(dbPath, DeviceDao.ENTITY_NAME);
        createEntityDbs(dbPath, FeedDao.ENTITY_NAME);

    }

    private void createEntityDbs(Path basePath, String entity) {
        Path p = basePath.resolve(entity);
        if (!Files.exists(p)) {
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                String err = "The database failed to save a directory. Please check you have the right permissions";
                LOGGER.error(err);
                throw new IllegalStateException(err);
            }
        }

    }
}
