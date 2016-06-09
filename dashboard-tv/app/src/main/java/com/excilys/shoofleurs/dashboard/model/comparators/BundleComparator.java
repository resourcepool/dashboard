package com.excilys.shoofleurs.dashboard.model.comparators;

import com.excilys.shoofleurs.dashboard.model.entities.Bundle;

import java.util.Comparator;

/**
 * Created by tommy on 20/04/16.
 */
public class BundleComparator implements Comparator<Bundle> {
    @Override
    public int compare(Bundle lhs, Bundle rhs) {
        /**TODO compare the two bundles start dates and return the newest**/
        return lhs.getUuid().compareTo(rhs.getUuid());
    }
}
