package com.excilys.shoofleurs.dashboard.business.service;

import com.excilys.shoofleurs.dashboard.business.dao.DiaporamaDao;
import com.excilys.shoofleurs.dashboard.entities.Diaporama;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class DiaporamaService {

	private static final Logger LOGGER = Logger.getLogger(DiaporamaService.class.getSimpleName());

	@EJB
	private DiaporamaDao mDiaporamaDao;


	public Diaporama createDiaporama(Diaporama diaporama) {
		try {
			diaporama = mDiaporamaDao.create(diaporama);
		} catch (SQLException e) {
			diaporama = null;
			LOGGER.info("Creation failed, caused by : " + e.getCause());
		}
		return diaporama;
	}


	public List<Diaporama> findAll() {
		return mDiaporamaDao.findAll();
	}

	public Diaporama getById(int id) {
		return mDiaporamaDao.findById(id);
	}


}
