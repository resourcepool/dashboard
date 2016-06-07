package com.excilys.shooflers.dashboard.server.dao;

import com.excilys.shooflers.dashboard.server.model.Revision;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public interface RevisionDao {

  String ENTITY_NAME = "revision";

  /**
   * Retrieve revision from DB.
   *
   * @param rev the revision
   * @return the retrieved Revision or null if no match
   */
  Revision get(Long rev);

  /**
   * Retrieve a set of revisions from DB.
   *
   * @param minRev the current revision of the client
   * @param maxRev the target revision of the client
   * @return the retrieved Revisions or empty
   */
  List<Revision> get(Long minRev, Long maxRev);

  /**
   * Retrieve all revisions from DB.
   *
   * @return the retrieved revisions or empty
   */
  List<Revision> getAll();

  /**
   * Save a revision into DB.
   *
   * @param rev the revision
   */
  void save(Revision rev);

  /**
   * Retrieve latest Revision.
   *
   * @return the latest revision
   */
  Long getLatest();
}
