package com.excilys.shoofleurs.dashboard.business.dao;

import com.excilys.shoofleurs.dashboard.business.crud.CrudService;
import com.excilys.shoofleurs.dashboard.entities.Diaporama;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class DiaporamaDao extends CrudService<Diaporama> {

	public Diaporama findById(int id) {
		return findById(Diaporama.class, id);
	}

	public List<Diaporama> findAll() {
		return findWithNamedQuery("diaporamas.findAll", null, 0, 0);
	}
}
