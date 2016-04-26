package com.excilys.shoofleurs.dashboard.business.dao;


import com.excilys.shoofleurs.dashboard.business.crud.CrudService;
import com.excilys.shoofleurs.dashboard.entities.notif.Notification;

import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class NotificationDao extends CrudService<Notification> {

	private Notification findById(int id) throws SQLException {
		return findById(Notification.class, id);
	}

	public Notification findBySlideShowId(int id) throws SQLException {
		Map<Object, Object> parameters = new HashMap<>();
		parameters.put("id", id);
		List<Notification> notifications = findWithNamedQuery("findBySlideShowId", parameters, 0, 0);
		return notifications == null || notifications.isEmpty() ? null : notifications.get(0);
	}

	public boolean delete(int id) throws SQLException {
		Notification notification = findById(id);
		if (notification == null) {
			return false;
		}
		delete(notification);
		return true;
	}

	public List<Notification> findAll() throws SQLException {
		return findWithNamedQuery("findAll.notifications", null, 0, 0);
	}
}
