package com.excilys.shoofleurs.dashboard.model.comparators;

import com.excilys.shoofleurs.dashboard.model.entities.SlideShow;

import java.util.Comparator;

/**
 * Created by tommy on 20/04/16.
 */
public class SlideShowComparator implements Comparator<SlideShow> {
    @Override
    public int compare(SlideShow lhs, SlideShow rhs) {
        /**TODO compare the two slideshows start dates and return the newest**/
        return Integer.compare(lhs.getId(), rhs.getId());
    }
}
