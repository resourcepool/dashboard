package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.dao.util.YamlUtils;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
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
    public BundleMetadata save(BundleMetadata bundle) {
        if (bundle.getUuid() == null) {
            bundle.setUuid(UUID.randomUUID().toString());
        }
        File dest = getBundleFile(bundle.getUuid());
        YamlUtils.store(bundle, dest);
        return bundle;
    }

    @Override
    public boolean delete(String uuid) {
        return YamlUtils.delete(bundleDatabasePath.resolve(uuid + ".yaml").toFile());
    }

    private File getBundleFile(String uuid) {
        String dataFileName = uuid + ".yaml";
        return bundleDatabasePath.resolve(dataFileName).toFile();
    }


    private BundleMetadata readBundleFromFile(File dataFile) {
        return YamlUtils.read(dataFile, BundleMetadata.class);
    }

}
