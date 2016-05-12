package com.excilys.shoofleurs.dashboard.business.service;

import com.excilys.shoofleurs.dashboard.business.dao.FlashDao;
import com.excilys.shoofleurs.dashboard.entities.flash.Flash;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;

@Stateless
public class FlashService {

	@EJB
	private FlashDao mFlashDao;

	public Flash create(Flash flash) {
		try {
			flash = mFlashDao.create(flash);
		} catch (SQLException e) {
			flash = null;
		}
		return flash;
	}

	public Flash findById(int id) {
		Flash flash;
		try {
			flash = mFlashDao.findById(id);
		} catch (SQLException e) {
			flash = null;
		}
		return flash;
	}

	public boolean delete(int id) {
		boolean result;
		try {
			result = mFlashDao.delete(id);
		} catch (SQLException e) {
			result = false;
		}
		return result;
	}

	public Flash merge(Flash flash) {
		try {
			flash = mFlashDao.update(flash);
		} catch (SQLException e) {
			flash = null;
		}
		return flash;
	}
}
