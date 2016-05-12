package com.excilys.shoofleurs.dashboard.business.service;

import com.excilys.shoofleurs.dashboard.business.dao.ContentDao;
import com.excilys.shoofleurs.dashboard.entities.AbstractContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;

/**
 * Service for AbstractContent.
 */
@Stateless
public class ContentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentService.class.getSimpleName());

	@EJB
	private ContentDao mAbstractContentDao;


	/**
	 * Create a content.
	 * @param abstractContent Content to persit
	 * @return Content persited with refreshed id or null if something went wrong.
	 */
	public AbstractContent create(AbstractContent abstractContent) {
		try {
			abstractContent = mAbstractContentDao.create(abstractContent);
		} catch (SQLException e) {
			abstractContent = null;
			LOGGER.error("Error during the creation of the content. Cause : " + e.getMessage());
		}
		return abstractContent;
	}


	/**
	 * Update a content.
	 * @param abstractContent Content to update
	 * @return Update content or null if something went wrong
	 */
	public AbstractContent update(AbstractContent abstractContent) {
		try {
			abstractContent = mAbstractContentDao.update(abstractContent);
		} catch (SQLException e) {
			abstractContent = null;
			LOGGER.error("Error during the update of the content. Cause : " + e.getMessage());
		}
		return abstractContent;
	}


	/**
	 * Delete a content.
	 * @param id ID to delete
	 * @return Result of the operation
	 */
	public boolean delete(int id) {
		boolean result;
		try {
			result = mAbstractContentDao.delete(id);
		} catch (SQLException e) {
			result = false;
			LOGGER.error("Delete content with id " + id + " failded. Cause : " + e.getMessage());
		}
		return result;
	}


	/**
	 * Find a content by its id.
	 * @param id ID to find
	 * @return Abstract content if id exists
	 */
	public AbstractContent findById(int id) {
		AbstractContent abstractContent;
		try {
			abstractContent = mAbstractContentDao.findById(id);
		} catch (SQLException e) {
			abstractContent = null;
			LOGGER.error("Error during find. Id " + id + " not found. Cause  : " + e.getMessage());
		}
		return abstractContent;
	}
}
