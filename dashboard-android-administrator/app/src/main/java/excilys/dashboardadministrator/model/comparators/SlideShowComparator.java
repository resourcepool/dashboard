package excilys.dashboardadministrator.model.comparators;


import java.util.Comparator;

import excilys.dashboardadministrator.model.entities.SlideShow;

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
