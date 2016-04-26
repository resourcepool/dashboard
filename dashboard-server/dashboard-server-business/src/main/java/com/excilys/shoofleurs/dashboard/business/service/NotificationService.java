package com.excilys.shoofleurs.dashboard.business.service;

import com.excilys.shoofleurs.dashboard.business.dao.NotificationDao;
import com.excilys.shoofleurs.dashboard.entities.notif.Notification;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;

@Stateless
public class NotificationService {

	@EJB
	private NotificationDao mNotificationDao;

	public Notification create(Notification notification) {
		try {
			notification = mNotificationDao.create(notification);
		} catch (SQLException e) {
			notification = null;
		}
		return notification;
	}

	public Notification findBySlideShowId(int id) {
		Notification notification;
		try {
			notification = mNotificationDao.findBySlideShowId(id);
		} catch (SQLException e) {
			notification = null;
		}
		return notification;
	}

	public boolean delete(int id) {
		boolean result;
		try {
			result = mNotificationDao.delete(id);
		} catch (SQLException e) {
			result = false;
		}
		return result;
	}

	public List<Notification> findAll() {
		List<Notification> notifications;
		try {
			notifications = mNotificationDao.findAll();
		} catch (SQLException e) {
			notifications = null;
		}
		return notifications;
	}


}
