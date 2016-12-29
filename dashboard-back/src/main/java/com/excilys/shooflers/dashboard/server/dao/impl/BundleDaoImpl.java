package com.excilys.shooflers.dashboard.server.dao.impl;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dao.util.BundleReverseIndex;
import com.excilys.shooflers.dashboard.server.dao.util.YamlUtils;
import com.excilys.shooflers.dashboard.server.exception.ResourceIoException;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
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
@DependsOn("daoInitializer")
public class BundleDaoImpl implements BundleDao {


    private static final Logger LOGGER = LoggerFactory.getLogger(BundleDaoImpl.class);

    @Autowired
    private DashboardProperties props;

    private BundleReverseIndex bri = new BundleReverseIndex();

    private Path bundleDatabasePath;

    @PostConstruct
    public void init() {
        bundleDatabasePath = Paths.get(props.getBasePath(), ENTITY_NAME);
        bri.refreshDataset(getAll());
    }

    @Override
    public BundleMetadata get(String uuid) {
        File dataFile = getBundleFile(uuid);
        return readBundleFromFile(dataFile);
    }

    @Override
    public BundleMetadata getByTag(String tag) {
        String uuid = bri.getBundleUuid(tag);
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
        refreshReverseIndex();
        //bri.addEntry(bundle.getTag(), bundle.getUuid());
        return bundle;
    }

    @Override
    public BundleMetadata delete(String uuid) {
        BundleMetadata result = get(uuid);
        boolean success = YamlUtils.delete(bundleDatabasePath.resolve(uuid + ".yaml").toFile());
        if (!success) {
            throw new ResourceIoException();
        }
        refreshReverseIndex();
        return result;
    }

    private File getBundleFile(String uuid) {
        String dataFileName = uuid + ".yaml";
        return bundleDatabasePath.resolve(dataFileName).toFile();
    }


    private BundleMetadata readBundleFromFile(File dataFile) {
        return YamlUtils.read(dataFile, BundleMetadata.class);
    }


    private void refreshReverseIndex() {
        bri.invalidate();
        bri.refreshDataset(getAll());
    }
}
