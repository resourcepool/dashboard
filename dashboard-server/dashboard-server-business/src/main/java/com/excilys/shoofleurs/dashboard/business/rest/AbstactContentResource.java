package com.excilys.shoofleurs.dashboard.business.rest;

import com.excilys.shoofleurs.dashboard.business.dao.AbstractContentDao;
import com.excilys.shoofleurs.dashboard.business.json.Response;
import com.excilys.shoofleurs.dashboard.business.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.json.Views;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("contents")
public class AbstactContentResource {

	@EJB
	private AbstractContentDao mContentDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllContents(@QueryParam("start") int start, @QueryParam("offset") int offset) {
		return new Response(JsonMapper.objectAsJson(mContentDao.findAll(), Views.LightContent.class), 200);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addContent(AbstractContent abstractContent) {
		mContentDao.create(abstractContent);
	}
}
