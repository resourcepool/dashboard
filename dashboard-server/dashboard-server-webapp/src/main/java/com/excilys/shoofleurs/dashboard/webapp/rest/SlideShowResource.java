package com.excilys.shoofleurs.dashboard.webapp.rest;

import static com.excilys.shoofleurs.dashboard.webapp.rest.utils.ParamValidator.*;

import com.excilys.shoofleurs.dashboard.business.service.NotificationService;
import com.excilys.shoofleurs.dashboard.entities.SlideShow;
import com.excilys.shoofleurs.dashboard.entities.notif.Notification;
import com.excilys.shoofleurs.dashboard.entities.notif.enums.ObjectType;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.business.service.SlideShowService;
import com.excilys.shoofleurs.dashboard.json.Views;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("slideshows")
public class SlideShowResource {

	@EJB
	private SlideShowService mSlideShowService;

	@EJB
	private NotificationService mNotificationService;

	/**
	 * Get All slideshows paginated.
	 * @param type Type of Json view (light, full or tv)
	 * @param start Limit start >= 0
	 * @param offset Limit end  <= 50
	 * @return Response containing the list of slideshows or an error message
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSlideShows(@QueryParam("json") String type,
									 @QueryParam("start") int start,
									 @QueryParam("offset") int offset) {
		offset = inferiorOrEqualsAs(offset, 50, 50);
		offset = superiorOrEqualsAs(offset, 0, 10);

		List<SlideShow> slideShows = mSlideShowService.findAll(inferiorOrEqualsAs(start, 0, 0), offset);
		if (slideShows == null || slideShows.isEmpty()) {
			return new Response("Empty list", 404);
		}
		return new Response(JsonMapper.objectAsJson(slideShows, getJsonView(type)), 200);
	}


	/**
	 * Get slideshow by its id.
	 * @param id ID slideshows
	 * @param type Type of Json view (light, full or tv)
	 * @return SlideShow found wrapped into a response or an error message.
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSlideShowById(@PathParam("id") int id, @QueryParam("json") String type) {
		SlideShow slideShow = mSlideShowService.findById(id);
		if (slideShow == null) {
			return new Response("Not found", 404);
		}
		return new Response(JsonMapper.objectAsJson(slideShow, getJsonView(type)), 200);
	}

	/**
	 * Create a new slideshow.
	 * @param slideShow SlideShow to create.
	 * @return Slideshow created with updated id or an error message
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newSlideShow(SlideShow slideShow) {
		slideShow = mSlideShowService.create(slideShow);
		if (slideShow == null) {
			return new Response("Object creation failed", 500);
		}
		mNotificationService.create(new Notification(ObjectType.DIAPORAMA, slideShow.getId()));
		return new Response(JsonMapper.objectAsJson(slideShow, Views.FullContent.class), 200);
	}

	/**
	 * Update a slideshow.
	 * @param slideShow Slideshow to updated
	 * @return Updated slideshow
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSlideShow(SlideShow slideShow) {
		slideShow = mSlideShowService.update(slideShow);
		if (slideShow == null) {
			return new Response("Updated failed", 500);
		}

		if (mNotificationService.findBySlideShowId(slideShow.getId()) == null) {
			mNotificationService.create(new Notification(ObjectType.DIAPORAMA, slideShow.getId()));
		}
		return new Response(JsonMapper.objectAsJson(slideShow, Views.FullContent.class), 200);
	}

	/**
	 * Delete an existing slideshow
	 * @param slideShowId Slideshow id to delete
	 * @return Result of the operation wrapped in a Response JSON
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSlideShow(@PathParam("id") int slideShowId) {
		if (mSlideShowService.delete(slideShowId)) {
			return new Response("Slideshow with id " + slideShowId + " has been removed", 200);
		}
		if (mNotificationService.findBySlideShowId(slideShowId) == null) {
			mNotificationService.create(new Notification(ObjectType.DIAPORAMA, slideShowId));
		}
		return new Response("Slideshow with id " + slideShowId + " can't be delet. Check the id", 500);
	}
}
