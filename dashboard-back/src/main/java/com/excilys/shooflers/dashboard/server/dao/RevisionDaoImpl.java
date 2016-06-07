package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.dao.util.YamlUtils;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@Component
public class RevisionDaoImpl implements RevisionDao {

    private static final String REVISION_LATEST_FILE = "latest";

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionDaoImpl.class);

    @Autowired
    private DashboardProperties props;

    private Path revDatabasePath;

    @PostConstruct
    public void init() {
        revDatabasePath = Paths.get(props.getBasePath(), ENTITY_NAME);
    }

    @Override
    public Revision get(Long rev) {
        File dataFile = getRevFile(rev.toString());
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
        for (File b : revDatabasePath.toFile().listFiles(File::isFile)) {
            // Skip latest
            if (!b.getName().equals(REVISION_LATEST_FILE + ".yaml")) {
                revisions.add(readRevFromFile(b));
            }
        }
        return revisions;
    }

    @Override
    public void save(Revision rev) {
        if (rev.getRevision() == null) {
            rev.setRevision(getLatest());
        }
        File dest = getRevFile(rev.getRevision().toString());
        YamlUtils.store(rev, dest);

        Revision latest = new Revision();
        latest.setRevision(getLatest() + 1);
        YamlUtils.store(latest, getRevFile(REVISION_LATEST_FILE));
    }

    @Override
    public Long getLatest() {
        File dataFile = getRevFile(REVISION_LATEST_FILE);
        Revision rev = readRevFromFile(dataFile);
        return rev == null ? null : rev.getRevision();
    }

    private File getRevFile(String rev) {
        String dataFileName = rev + ".yaml";
        return revDatabasePath.resolve(dataFileName).toFile();
    }


    private Revision readRevFromFile(File dataFile) {
        return YamlUtils.read(dataFile, Revision.class);
    }

}
