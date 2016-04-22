package com.excilys.shoofleurs.dashboard.webapp.rest;

import static com.excilys.shoofleurs.dashboard.webapp.rest.utils.ParamValidator.*;

import com.excilys.shoofleurs.dashboard.entities.SlideShow;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.business.service.SlideShowService;
import com.excilys.shoofleurs.dashboard.json.Views;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
	public Response getSlideshowById(@PathParam("id") int id, @QueryParam("json") String type) {
		SlideShow slideShow = mSlideShowService.getById(id);
		if (slideShow == null) {
			return new Response("Not found", 404);
		}
		return new Response(JsonMapper.objectAsJson(slideShow, getJsonView(type)), 200);
	}

	/**
	 * Create a new slideshow.
	 * @param slideShow SlideShow to create.
	 * @return Diapora created with updated id or an error message
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newSlideshow(SlideShow slideShow) {
		slideShow = mSlideShowService.createSlideShow(slideShow);
		if (slideShow == null) {
			return new Response("Object creation failed", 500);
		}
		return new Response(JsonMapper.objectAsJson(slideShow, Views.LightContent.class), 200);
	}
}
