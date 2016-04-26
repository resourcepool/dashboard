package com.excilys.shoofleurs.dashboard.webapp.rest;

import com.excilys.shoofleurs.dashboard.business.service.FlashService;
import com.excilys.shoofleurs.dashboard.business.service.NotificationService;
import com.excilys.shoofleurs.dashboard.entities.flash.Flash;
import com.excilys.shoofleurs.dashboard.entities.flash.ImageFlash;
import com.excilys.shoofleurs.dashboard.entities.notif.Notification;
import com.excilys.shoofleurs.dashboard.entities.notif.enums.ObjectType;
import com.excilys.shoofleurs.dashboard.json.Views;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.webapp.rest.utils.SaveFile;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Stateless
@Path("flashs")
public class FlashResource {

	@EJB
	private FlashService mFlashService;

	@EJB
	private NotificationService mNotificationService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessage(@PathParam("id") int id) {
		Flash flash = mFlashService.findById(id);
		if (flash == null) {
			return new Response("Message flash not found", 404);
		}
		mFlashService.delete(id);
		return new Response(JsonMapper.objectAsJson(flash, null), 200);
	}


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFlash(Flash flash) {
		flash = mFlashService.create(flash);
		if (flash == null) {
			return new Response("Error during object creation", 500);
		}
		mNotificationService.create(new Notification(ObjectType.MESSAGE_FLASH, flash.getFlashId()));
		return new Response(JsonMapper.objectAsJson(flash, null), 200);
	}


	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContent(@FormDataParam("file") InputStream uploadedInputStream,
							   @FormDataParam("file") FormDataContentDisposition fileDetail,
							   @HeaderParam("content") String flashAsJson) {
		if (flashAsJson != null && uploadedInputStream != null) {
			Flash flash = JsonMapper.jsonAsFlash(flashAsJson);
			if (flash == null) {
				return new Response("JSON malformed", 500);
			}

			flash = mFlashService.create(flash);
			if (flash == null) {
				return new Response("Error during persistence. See logs for more info", 500);
			}

			String name = SaveFile.saveFile(uploadedInputStream, fileDetail, flash.getFlashId());
			if (name == null) {
				return new Response("Error during the upload", 500);
			}

			ImageFlash imageFlash = (ImageFlash) flash;
			imageFlash.setUrl("http://vps229493.ovh.net:8080/dashboard/img/" + name);
			flash = mFlashService.merge(imageFlash);

			mNotificationService.create(new Notification(ObjectType.IMAGE_FLASH, flash.getFlashId()));

			return new Response(JsonMapper.objectAsJson(flash, Views.FullContent.class), 200);
		}
		return new Response("File or json missing. Request malformed.", 500);
	}
}
