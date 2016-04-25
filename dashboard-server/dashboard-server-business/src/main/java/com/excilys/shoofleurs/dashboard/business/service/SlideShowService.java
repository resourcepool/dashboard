package com.excilys.shoofleurs.dashboard.business.service;

import com.excilys.shoofleurs.dashboard.business.dao.SlideShowDao;
import com.excilys.shoofleurs.dashboard.entities.SlideShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;

/**
 * Service for SlideShow.
 */
@Stateless
public class SlideShowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlideShowService.class.getSimpleName());

	@EJB
	private SlideShowDao mSlideShowDao;


	/**
	 * Create a new slideShow.
	 * @param slideShow SlideShow to persist.
	 * @return SlideShow created with refreshed id or null if something went wrong
	 */
	public SlideShow createSlideShow(SlideShow slideShow) {
		try {
			slideShow = mSlideShowDao.create(slideShow);
		} catch (SQLException e) {
			slideShow = null;
			LOGGER.info("Creation failed, caused by : " + e.getCause());
		}
		return slideShow;
	}


	/**
	 * Get slideshow paginated
	 * @param start Limit start (must be 0)
	 * @param offset Limit end (can't be > 50)
	 * @return List of slideshow or null if something went wrong
	 */
	public List<SlideShow> findAll(int start, int offset) {
		if (start < 0) {
			start = 0;
		}

		if (start > 50) {
			start = 50;
		}

		List<SlideShow> slideShows;
		try {
			slideShows = mSlideShowDao.findAll(start, offset);
		} catch (SQLException e) {
			slideShows = null;
			LOGGER.info("FindAll slideShows failed, caused by : " + e.getCause());
		}
		return slideShows;
	}

	/**
	 * Get slideshow by id.
	 * @param id ID to find
	 * @return SlideShow found or null if someting went wrong or slideshow was not found.
	 */
	public SlideShow getById(int id) {
		SlideShow slideShow;
		try {
			slideShow = mSlideShowDao.findById(id);
		} catch (Exception e) {
			slideShow = null;
			LOGGER.info("Find slideshow with id : " + id + " failed, caused by : " + e.getCause());
		}
		return slideShow;
	}

	/**
	 * Delete a slideshow by id.
	 * @param id Slideshow id to delete
	 * @return Result of the operation
	 */
	public boolean delete(int id) {
		boolean result;
		try {
			result = mSlideShowDao.delete(id);
		} catch (SQLException e) {
			result = false;
			LOGGER.info("Delete slideshow with id : " + id + " failed, caused by : " + e.getCause());
		}
		return result;
	}

	/**
	 * Update slideShow.
	 * @param slideShow SlideShow to updated
	 * @return SlideShow updated or null if someting went wrong.
	 */
	public SlideShow update(SlideShow slideShow) {
		try {
			slideShow = mSlideShowDao.update(slideShow);
		} catch (Exception e) {
			slideShow = null;
			LOGGER.info("Update slideShow failed, caused by : " + e.getCause());
		}
		return slideShow;
	}
}
