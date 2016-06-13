package com.excilys.shooflers.dashboard.server.dao.util;

import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Loïc Ortola on 12/06/2016.
 */
public class BundleReverseIndex {
    private Map<String, String> reverseIndex;

    public BundleReverseIndex() {
        reverseIndex = new HashMap<>();
    }

    /**
     * Retrieve the bundle uuid associated with a bundle tag.
     *
     * @param bundleTag the bundle tag
     * @return null if no match, the uuid otherwise
     */
    public String getBundleUuid(String bundleTag) {
        return reverseIndex.get(bundleTag);
    }

    public boolean containsTag(String bundleTag) {
        return reverseIndex.containsKey(bundleTag);
    }

    public void addEntry(String bundleTag, String uuid) {
        reverseIndex.put(bundleTag, uuid);
    }

    public void invalidate() {
        reverseIndex.clear();
    }

    public void refreshDataset(List<BundleMetadata> bundles) {
        if (bundles == null || bundles.isEmpty()) {
            return;
        }
        for (BundleMetadata media : bundles) {
            addEntry(media.getTag(), media.getUuid());
        }
    }
}
