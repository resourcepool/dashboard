package com.excilys.shooflers.dashboard.server.service;

import com.excilys.shooflers.dashboard.server.model.Revision;

import java.util.List;

/**
 * Revision Service.
 *
 * @author Loïc Ortola on 08/06/2016.
 */
public interface RevisionService {

    /**
     * Get list of previous revisions from a specific revision.
     *
     * @param revision the revision
     * @return the list of revisions
     */
    List<Revision> getDiffs(long revision);

    /**
     * Get latest revision integer value.
     *
     * @return the latest revision
     */
    long getLatest();
}
