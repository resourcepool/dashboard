package com.excilys.shoofleurs.dashboard.business.dao;

import com.excilys.shoofleurs.dashboard.business.crud.CrudService;
import com.excilys.shoofleurs.dashboard.entities.SlideShow;

import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;

/**
 * Access CRUD operations for SlideShow type.
 */
@Stateless
public class SlideShowDao extends CrudService<SlideShow> {

	/**
	 * Find a slideshow by its ID.
	 * @param id ID to find
	 * @return SlideShow found or null if it doesn't exist.
	 * @throws SQLException Test if something went wrong with the database
	 */
	public SlideShow findById(int id) throws SQLException {
		return findById(SlideShow.class, id);
	}

	/**
	 * Find all slideshows. Must be paginated.
	 * @param start Start limit
	 * @param offset End limit
	 * @return List of slideshow or an empty list
	 * @throws SQLException Test if something went wrong with the database
	 */
	public List<SlideShow> findAll(int start, int offset) throws SQLException {
		return findWithNamedQuery("slideshow.findAll", null, start, offset);
	}
}
