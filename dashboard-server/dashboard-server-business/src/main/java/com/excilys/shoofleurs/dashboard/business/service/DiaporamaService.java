package com.excilys.shoofleurs.dashboard.business.service;

import com.excilys.shoofleurs.dashboard.business.dao.DiaporamaDao;
import com.excilys.shoofleurs.dashboard.entities.Diaporama;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;

/**
 * Service for Diaporama.
 */
@Stateless
public class DiaporamaService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiaporamaService.class.getSimpleName());

	@EJB
	private DiaporamaDao mDiaporamaDao;


	/**
	 * Create a new diaporama.
	 * @param diaporama Diaporama to persist.
	 * @return Diaporama created with refreshed id or null if something went wrong
	 */
	public Diaporama createDiaporama(Diaporama diaporama) {
		try {
			diaporama = mDiaporamaDao.create(diaporama);
		} catch (SQLException e) {
			diaporama = null;
			LOGGER.info("Creation failed, caused by : " + e.getCause());
		}
		return diaporama;
	}


	/**
	 * Get diaporamas paginated
	 * @param start Limit start (must be 0)
	 * @param offset Limit end (can't be > 50)
	 * @return List of diaporamas or null if something went wrong
	 */
	public List<Diaporama> findAll(int start, int offset) {
		if (start < 0) {
			start = 0;
		}

		if (start > 50) {
			start = 50;
		}

		List<Diaporama> diaporamas;
		try {
			diaporamas = mDiaporamaDao.findAll(start, offset);
		} catch (SQLException e) {
			diaporamas = null;
			LOGGER.info("FindAll diaporamas failed, caused by : " + e.getCause());
		}
		return diaporamas;
	}

	/**
	 * Get diaporama by id.
	 * @param id ID to find
	 * @return Diaporama found or null if someting went wrong or diaporama was not found.
	 */
	public Diaporama getById(int id) {
		Diaporama diaporama;
		try {
			diaporama = mDiaporamaDao.findById(id);
		} catch (Exception e) {
			diaporama = null;
			LOGGER.info("Find diaporamas with id : " + id + " failed, caused by : " + e.getCause());
		}
		return diaporama;
	}

	/**
	 * Update diaporama.
	 * @param diaporama Diaporama to updated
	 * @return Diaporama updated or null if someting went wrong.
	 */
	public Diaporama update(Diaporama diaporama) {
		try {
			diaporama = mDiaporamaDao.update(diaporama);
		} catch (Exception e) {
			diaporama = null;
			LOGGER.info("Update diaporama failed, caused by : " + e.getCause());
		}
		return diaporama;
	}
}
