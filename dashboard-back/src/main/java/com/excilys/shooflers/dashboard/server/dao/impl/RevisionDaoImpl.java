package com.excilys.shooflers.dashboard.server.dao.impl;

import com.excilys.shooflers.dashboard.server.dao.RevisionDao;
import com.excilys.shooflers.dashboard.server.dao.util.YamlUtils;
import com.excilys.shooflers.dashboard.server.exception.ResourceIoException;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
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

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Component
@DependsOn("daoInitializer")
public class RevisionDaoImpl implements RevisionDao {

    private static final String REVISION_LATEST_FILE = "latest";

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionDaoImpl.class);

    @Autowired
    private DashboardProperties props;

    @Autowired
    private FileSystem fs;

    private Path revDatabasePath;

    @PostConstruct
    public void init() {
        revDatabasePath = fs.getPath(props.getBasePath(), ENTITY_NAME);
    }

    @Override
    public Revision get(Long rev) {
        Path dataFile = getRevFile(rev.toString());
        return readRevFromFile(dataFile);
    }

    @Override
    public List<Revision> get(Long minRev, Long maxRev) {
        List<Revision> revisions = new LinkedList<>();
        for (Long i = minRev; i <= maxRev; i++) {
            revisions.add(readRevFromFile(getRevFile(i.toString())));
        }
        return revisions;
    }

    @Override
    public List<Revision> getAll() {
        List<Revision> revisions = new LinkedList<>();
        try {
            // Skip latest
            Files.walk(revDatabasePath, 1)
                    .filter(path -> path.getFileName().equals(REVISION_LATEST_FILE + ".yaml"))
                    .forEach(path -> revisions.add(readRevFromFile(path)));
        } catch (IOException e) {
            LOGGER.error("exception in RevisionDaoImpl#getAll", e);
            throw new ResourceIoException(e);
        }
        return revisions;
    }

    @Override
    public void save(Revision rev) {
        if (rev.getRevision() == null) {
            rev.setRevision(getLatest());
        }
        Path dest = getRevFile(rev.getRevision().toString());
        YamlUtils.store(rev, dest);

        Revision latest = new Revision();
        latest.setRevision(getLatest() + 1);
        YamlUtils.store(latest, getRevFile(REVISION_LATEST_FILE));
    }

    @Override
    public Long getLatest() {
        Path path = getRevFile(REVISION_LATEST_FILE);
        Revision rev = readRevFromFile(path);
        return rev == null ? 0 : rev.getRevision();
    }

    private Path getRevFile(String rev) {
        String dataFileName = rev + ".yaml";
        return revDatabasePath.resolve(dataFileName);
    }


    private Revision readRevFromFile(Path path) {
        return YamlUtils.read(path, Revision.class);
    }

}
