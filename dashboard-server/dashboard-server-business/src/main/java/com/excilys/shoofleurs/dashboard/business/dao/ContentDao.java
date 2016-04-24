package com.excilys.shoofleurs.dashboard.business.dao;

import com.excilys.shoofleurs.dashboard.business.crud.CrudService;
import com.excilys.shoofleurs.dashboard.entities.AbstractContent;

import javax.ejb.Stateless;

/**
 * Access CRUD operations for AbstractContent type.
 */
@Stateless
public class ContentDao extends CrudService<AbstractContent> {

}
