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
            if (rev.getAction() == Revision.Action.UPDATE) {
                // Remove add
                Revision revCompare = diffs.get(rev.getTarget());
                if (revCompare != null) {
                    diffs.remove(revCompare.getTarget());
                }

                // Keep the most recent update
                if (revCompare == null) {
                    diffs.put(rev.getResult(), rev);
                } else if (revCompare.getRevision() < rev.getRevision()) {
                    diffs.remove(revCompare.getResult());
                    diffs.put(rev.getResult(), rev);
                }

            } else if (rev.getAction() == Revision.Action.ADD && diffs.get(rev.getTarget()) == null) {
                // Only add if update or delete does not exist
                diffs.put(rev.getTarget(), rev);
            } else if (rev.getAction() == Revision.Action.DELETE) {
                // Delete action prevails all others actions
                diffs.remove(rev.getTarget());
                diffs.put(rev.getTarget(), rev);
            }
        }
        return diffs.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public long getLatest() {
        return revisionDao.getLatest();
    }
}
