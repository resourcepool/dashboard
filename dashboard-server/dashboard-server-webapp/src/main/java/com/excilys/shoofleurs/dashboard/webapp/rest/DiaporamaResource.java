package com.excilys.shoofleurs.dashboard.webapp.rest;

import static com.excilys.shoofleurs.dashboard.webapp.rest.utils.ParamValidator.*;

import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.business.service.DiaporamaService;
import com.excilys.shoofleurs.dashboard.entities.Diaporama;
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
@Path("diaporamas")
public class DiaporamaResource {

	@EJB
	private DiaporamaService mDiaporamaService;

	/**
	 * Get All diaporamas paginated.
	 * @param type Type of Json view (light, full or tv)
	 * @param start Limit start >= 0
	 * @param offset Limit end  <= 50
	 * @return Response containing the list of diaporamas or an error message
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDiaporamas(@QueryParam("json") String type,
									 @QueryParam("start") int start,
									 @QueryParam("offset") int offset) {
		offset = inferiorOrEqualsAs(offset, 50, 50);
		offset = superiorOrEqualsAs(offset, 0, 10);

		List<Diaporama> diaporamas = mDiaporamaService.findAll(inferiorOrEqualsAs(start, 0, 0), offset);
		if (diaporamas == null || diaporamas.isEmpty()) {
			return new Response("Empty list", 404);
		}
		return new Response(JsonMapper.objectAsJson(diaporamas, getJsonView(type)), 200);
	}


	/**
	 * Get diaporama by its id.
	 * @param id ID diaporamas
	 * @param type Type of Json view (light, full or tv)
	 * @return Diaporama found wrapped into a response or an error message.
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiaporamaById(@PathParam("id") int id, @QueryParam("json") String type) {
		Diaporama diaporama = mDiaporamaService.getById(id);
		if (diaporama == null) {
			return new Response("Not found", 404);
		}
		return new Response(JsonMapper.objectAsJson(diaporama, getJsonView(type)), 200);
	}

	/**
	 * Create a new diaporama.
	 * @param diaporama Diaporama to create.
	 * @return Diapora created with updated id or an error message
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newDiaporama(Diaporama diaporama) {
		diaporama = mDiaporamaService.createDiaporama(diaporama);
		if (diaporama == null) {
			return new Response("Object creation failed", 500);
		}
		return new Response(JsonMapper.objectAsJson(diaporama, Views.LightContent.class), 200);
	}
}
