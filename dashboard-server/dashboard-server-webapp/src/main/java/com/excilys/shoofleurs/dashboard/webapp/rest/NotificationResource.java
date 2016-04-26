package com.excilys.shoofleurs.dashboard.webapp.rest;

import com.excilys.shoofleurs.dashboard.business.service.NotificationService;
import com.excilys.shoofleurs.dashboard.entities.notif.Notification;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("notifications")
public class NotificationResource {

	@EJB
	private NotificationService mNotificationService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllNotifications() {
		List<Notification> notifications = mNotificationService.findAll();
		if (notifications == null || notifications.isEmpty()) {
			return new Response("You are up to date ! Nothing to download", 304);
		}
		notifications.forEach(notification -> mNotificationService.delete(notification.getId()));
		return new Response(JsonMapper.objectAsJson(notifications, null), 200);
	}
}
