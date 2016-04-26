package com.excilys.shoofleurs.dashboard.webapp.rest;

import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("flashs")
public class FlashResource {

	@GET
	@Path("images/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImage(@PathParam("id") int id) {
		return new Response("", 200);
	}
}
