package com.excilys.shoofleurs.dashboard.business.dao;

import com.excilys.shoofleurs.dashboard.business.crud.CrudService;
import com.excilys.shoofleurs.dashboard.entities.AbstractContent;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AbstractContentDao extends CrudService<AbstractContent> {

	public List<AbstractContent> findAll() {
		return findWithNamedQuery("findAll", null, 0, 0);
	}

}
