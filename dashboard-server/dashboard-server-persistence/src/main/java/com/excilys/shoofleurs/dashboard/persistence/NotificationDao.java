package com.excilys.shoofleurs.dashboard.persistence;

import com.excilys.shoofleurs.dashboard.entities.notif.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationDao extends JpaRepository<Notification, Integer> {
}
