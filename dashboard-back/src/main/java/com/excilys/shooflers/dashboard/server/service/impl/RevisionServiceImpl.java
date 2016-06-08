package com.excilys.shooflers.dashboard.server.service.impl;

import com.excilys.shooflers.dashboard.server.dao.RevisionDao;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Revision Service.
 *
 * @author Loïc Ortola on 08/06/2016.
 */
@Service
public class RevisionServiceImpl implements RevisionService {

    @Autowired
    private RevisionDao revisionDao;

    @Override
    public List<Revision> getDiffs(long revision) {
        List<Revision> revisions = revisionDao.get(++revision, revisionDao.getLatest());

        Map<String, Revision> diffs = new HashMap<>();

        for (Revision rev : revisions) {
            switch (rev.getAction()) {
                case ADD:
                    // An addToDiffs should only be computed if no target exists within the map yet.
                    // If a target already exists, it can only mean one thing:
                    // The object was deleted.
                    // In this case, target should be removed from map (operations cancel each other)
                    if (shouldRemoveFromDiffs(diffs, rev.getTarget())) {
                        // The antagonist operation exists. ADD + DELETE cancel each other (whatever the order)
                        removeFromDiffs(diffs, rev.getTarget());
                    } else {
                        addToDiffs(diffs, rev);
                    }
                    break;
                case UPDATE:
                    // An update is two actions: A "DELETE" of the previous target, and an "ADD" of a new result.
                    // If previous target is in the map, simply removeFromDiffs the record (operations cancel each other).
                    // Else, it means original target has either not been parsed yet, or is simply beyond scope (in a previous revision for instance).
                    // in this case, create a "DELETE" action. It will be either cancelled later by the ADD rule, or applied to older revisions client-side.

                    // Remove old object
                    if (shouldRemoveFromDiffs(diffs, rev.getTarget())) {
                        // The antagonist operation exists. ADD + DELETE cancel each other (whatever the order)
                        removeFromDiffs(diffs, rev.getTarget());
                    } else {
                        // Antagonist operation not found. Create delete action
                        Revision deleteAction = Revision
                                .builder()
                                .action(Revision.Action.DELETE)
                                .revision(rev.getRevision())
                                .target(rev.getTarget())
                                .build();
                        addToDiffs(diffs, deleteAction);
                    }

                    // Add the new object (equivalent of the updated one, but "new" as state is immutable
                    Revision addAction = Revision
                            .builder()
                            .action(Revision.Action.ADD)
                            .revision(rev.getRevision())
                            .target(rev.getResult()) // Target of the new object is result of the update object
                            .build();
                    addToDiffs(diffs, addAction);

                    break;
                case DELETE:
                    // Delete action works exactly as the add action.
                    // It will only be added to the diffs if it doesn't contain the antagonist operation (ADD).

                    if (shouldRemoveFromDiffs(diffs, rev.getTarget())) {
                        // The opposite operation exists. ADD + DELETE cancel each other (whatever the order)
                        removeFromDiffs(diffs, rev.getTarget());
                    } else {
                        addToDiffs(diffs, rev);
                    }
                    break;
            }

        }
        return diffs.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    /**
     * Check whether an entry should be deleted or not.
     *
     * @param diffs  the dataset
     * @param target the target entry
     * @return true if should delete, false otherwise
     */
    private boolean shouldRemoveFromDiffs(Map<String, Revision> diffs, String target) {
        Revision r = diffs.get(target);
        return r != null && (r.getAction().equals(Revision.Action.DELETE) || r.getAction().equals(Revision.Action.ADD));
    }

    /**
     * Removes item from diffs dataset.
     *
     * @param diffs  the diffs dataset
     * @param target the target key to remove
     * @return the removed object
     */
    private Revision removeFromDiffs(Map<String, Revision> diffs, String target) {
        return diffs.remove(target);
    }

    /**
     * Adds item to the diffs dataset.
     *
     * @param diffs the diffs dataset
     * @param rev   the target revision to add
     */
    private void addToDiffs(Map<String, Revision> diffs, Revision rev) {
        diffs.put(rev.getTarget(), rev);
    }

    @Override
    public long getLatest() {
        return revisionDao.getLatest();
    }

    @Override
    public Revision add(Revision.Action action, String target, Revision.Type type, String result) {
        Revision revision = new Revision();
        revision.setRevision(getLatest() + 1);
        revision.setAction(action);
        revision.setTarget(target);
        revision.setResult(result);
        revision.setType(type);
        revisionDao.save(revision);
        return revision;
    }
}
