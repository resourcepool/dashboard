package com.excilys.shooflers.dashboard.server.dao.util;

import com.excilys.shooflers.dashboard.server.model.metadata.MediaMetadata;

import java.util.*;

/**
 * @author Loïc Ortola on 12/06/2016.
 */
public class MediaReverseIndex {
    private Map<String, Set<String>> reverseIndex;
    
    public MediaReverseIndex() {
        reverseIndex = new HashMap<>();
    }

    /**
     * Retrieve BundleUuid of a media.
     * @param uuid the media uuid
     * @return uuid
     */
    public String getBundleUuid(String uuid) {
        Map.Entry<String, Set<String>> entry = reverseIndex.entrySet().stream()
                .filter(e -> e.getValue().contains(uuid))
                .findFirst()
                .orElseGet(null);
        return entry == null ? null : entry.getKey();
    }
    
    public void addEntry(String bundleUuid, String uuid) {
        Set<String> medias = reverseIndex.get(bundleUuid);
        if (medias == null) {
            medias = new HashSet<>();
            reverseIndex.put(bundleUuid, medias);
        }
        medias.add(uuid);
    }
    
    public void invalidate() {
        reverseIndex.clear();
    }
    
    public void refreshDataset(List<MediaMetadata> medias) {
        if (medias == null || medias.isEmpty()) {
            return;
        }
        for (MediaMetadata media : medias) {
            addEntry(media.getBundleTag(), media.getUuid());
        }
    }
}
