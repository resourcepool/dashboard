package com.excilys.shoofleurs.dashboard.business.dao;

import com.excilys.shoofleurs.dashboard.business.crud.CrudService;
import com.excilys.shoofleurs.dashboard.entities.Diaporama;

import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;

/**
 * Access CRUD operations for Diaporama type.
 */
@Stateless
public class DiaporamaDao extends CrudService<Diaporama> {

	/**
	 * Find a diaporama by its ID.
	 * @param id ID to find
	 * @return Diaporama found or null if it doesn't exist.
	 * @throws SQLException Test if something went wrong with the database
	 */
	public Diaporama findById(int id) throws SQLException {
		return findById(Diaporama.class, id);
	}

	/**
	 * Find all diaporamas. Must be paginated.
	 * @param start Start limit
	 * @param offset End limit
	 * @return List of diaporama or an empty list
	 * @throws SQLException Test if something went wrong with the database
	 */
	public List<Diaporama> findAll(int start, int offset) throws SQLException {
		return findWithNamedQuery("diaporamas.findAll", null, start, offset);
	}
}
