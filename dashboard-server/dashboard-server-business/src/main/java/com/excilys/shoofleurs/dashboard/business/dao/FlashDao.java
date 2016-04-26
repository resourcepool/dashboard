package com.excilys.shoofleurs.dashboard.business.dao;

import com.excilys.shoofleurs.dashboard.business.crud.CrudService;
import com.excilys.shoofleurs.dashboard.entities.flash.Flash;

import javax.ejb.Stateless;
import java.sql.SQLException;

@Stateless
public class FlashDao extends CrudService<Flash> {

	public Flash findById(int id) throws SQLException {
		return findById(Flash.class, id);
	}

	public boolean delete(int id) throws SQLException {
		Flash flash = findById(id);
		if (flash == null) {
			return false;
		}
		delete(flash);
		return true;
	}
}
