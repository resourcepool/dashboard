package com.excilys.shoofleurs.dashboard.business.dao;

import com.excilys.shoofleurs.dashboard.business.crud.CrudService;
import com.excilys.shoofleurs.dashboard.entities.AbstractContent;

import javax.ejb.Stateless;
import java.sql.SQLException;

/**
 * Access CRUD operations for AbstractContent type.
 */
@Stateless
public class ContentDao extends CrudService<AbstractContent> {

	/**
	 * Find a content by id.
	 * @param id ID to find
	 * @return Content found
	 * @throws SQLException Test if something went wrong with the database
	 */
	public AbstractContent findById(int id) throws SQLException {
		return findById(AbstractContent.class, id);
	}


	/**
	 * Delete a content by id.
	 * @param id ID to delete
	 * @return Result of the operation
	 * @throws SQLException Test if something went wrong with the database
	 */
	public boolean delete(int id) throws SQLException {
		AbstractContent abstractContent = findById(id);
		if (abstractContent == null) {
			return false;
		}
		abstractContent.getSlideShow().getContents().remove(abstractContent);
		delete(abstractContent);
		return true;
	}
}
