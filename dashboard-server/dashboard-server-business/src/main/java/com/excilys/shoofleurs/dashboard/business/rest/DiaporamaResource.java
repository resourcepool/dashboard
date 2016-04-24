package com.excilys.shoofleurs.dashboard.business.rest;

import com.excilys.shoofleurs.dashboard.business.json.Response;
import com.excilys.shoofleurs.dashboard.business.json.mapper.JsonMapper;
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

@Stateless
@Path("diaporamas")
public class DiaporamaResource {

	@EJB
	private DiaporamaService mDiaporamaService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDiaporamas(@QueryParam("json") boolean tv) {
		Class jsonType = tv ? Views.TvContent.class : Views.LightContent.class;
		return new Response(JsonMapper.objectAsJson(mDiaporamaService.findAll(), jsonType), 200);
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiaporamaById(@PathParam("id") int id) {
		return new Response(JsonMapper.objectAsJson(mDiaporamaService.getById(id), Views.FullContent.class), 200);
	}

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
