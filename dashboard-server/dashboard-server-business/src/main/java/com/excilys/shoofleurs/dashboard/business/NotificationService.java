package com.excilys.shoofleurs.dashboard.business;

import com.excilys.shoofleurs.dashboard.entities.notif.Notification;
import com.excilys.shoofleurs.dashboard.persistence.NotificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    public Notification save(Notification notification) {
        return notificationDao.save(notification);
    }

    public List<Notification> findAll() {
        return notificationDao.findAll();
    }

    public void delete(int id) {
        notificationDao.delete(id);
    }
}
