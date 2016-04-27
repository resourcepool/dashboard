package com.excilys.shoofleurs.dashboard.webapp.rest;

import com.excilys.shoofleurs.dashboard.business.service.ContentService;
import com.excilys.shoofleurs.dashboard.business.service.NotificationService;
import com.excilys.shoofleurs.dashboard.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.entities.notif.Notification;
import com.excilys.shoofleurs.dashboard.entities.notif.enums.ObjectType;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.json.Views;
import com.excilys.shoofleurs.dashboard.webapp.rest.utils.SaveFile;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;


@Stateless
@Path("contents")
public class ContentResource {

	@EJB
	private ContentService mContentService;

	@EJB
	private NotificationService mNotificationService;


	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContentById(@PathParam("id") int id) {
		AbstractContent abstractContent = mContentService.findById(id);
		if (abstractContent == null) {
			return new Response("Content not found", 404);
		}
		return new Response(JsonMapper.objectAsJson(abstractContent, Views.FullContent.class), 200);
	}


	/**
	 * Method to create a new content. To create a content, the request must contain a file and the information
	 * about the content in the header. The parameter use in the header is "content". It contains the JSON
	 * of the content to persit in the database.
	 * @param uploadedInputStream File send with multipart form data
	 * @param fileDetail Details about de file (name, extensions...)
	 * @param contentAsJson AbstractContent as Json
	 * @return A response with an error or the content created if the request succeed
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContent(@FormDataParam("file") InputStream uploadedInputStream,
							   @FormDataParam("file") FormDataContentDisposition fileDetail,
							   @HeaderParam("content") String contentAsJson) {
		System.out.println(contentAsJson);
		if (contentAsJson != null && uploadedInputStream != null) {
			AbstractContent content = JsonMapper.jsonAsAbstractContent(contentAsJson);
			if (content == null) {
				return new Response("JSON malformed", 500);
			}

			content = mContentService.create(content);
			if (content == null) {
				return new Response("Error during persistence. See logs for more info", 500);
			}

			String name = SaveFile.saveFile(uploadedInputStream, fileDetail, content.getId());
			if (name == null) {
				return new Response("Error during the upload", 500);
			}

			content.setUrl("http://vps229493.ovh.net:8080/dashboard/img/" + name);
			content = mContentService.update(content);
			content.getSlideShow().addContent(content);

			if (mNotificationService.findBySlideShowId(content.getSlideShow().getId()) == null) {
				mNotificationService.create(new Notification(ObjectType.DIAPORAMA, content.getSlideShow().getId()));
			}

			return new Response(JsonMapper.objectAsJson(content, Views.FullContent.class), 200);
		}
		return new Response("File or json missing. Request malformed.", 500);
	}

	/**
	 * Post request to persit a content without uploading a file (for webcontent or videocontent from youtube).
	 * @param content Content to persit
	 * @return Content persisted with refreshed id
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContent(AbstractContent content) {
		content = mContentService.create(content);
		if (content == null) {
			return new Response("Error during persistence. See logs for more info", 500);
		}
		content.getSlideShow().addContent(content);
		if (mNotificationService.findBySlideShowId(content.getSlideShow().getId()) == null) {
			mNotificationService.create(new Notification(ObjectType.DIAPORAMA, content.getSlideShow().getId()));
		}
		return new Response(JsonMapper.objectAsJson(content, Views.FullContent.class), 200);
	}


	/**
	 * Update a content.
	 * @param content Content to update
	 * @return Updated content
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateContent(AbstractContent content) {
		content = mContentService.update(content);
		if (content == null) {
			return new Response("Updated failed", 500);
		}
		content.getSlideShow().getContents().remove(content);
		content.getSlideShow().addContent(content);
		if (mNotificationService.findBySlideShowId(content.getSlideShow().getId()) == null) {
			mNotificationService.create(new Notification(ObjectType.DIAPORAMA, content.getSlideShow().getId()));
		}
		return new Response(JsonMapper.objectAsJson(content, Views.FullContent.class), 200);
	}


	/**
	 * Delete a content from a slideshow.
	 * @param id Content id to delete
	 * @return Result of the operation
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteContent(@PathParam("id") int id) {
		AbstractContent content = mContentService.findById(id);
		if (mContentService.delete(id)) {
			if (mNotificationService.findBySlideShowId(id) == null) {
				mNotificationService.create(new Notification(ObjectType.DIAPORAMA, id));
			}
			SaveFile.deleteFile(content.getUrl());
			return new Response("Content with id " + id + " has been removed", 200);
		}
		return new Response("Content with id " + id + " can't be delet. Check the id", 500);
	}
}
