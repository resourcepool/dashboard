package com.excilys.shoofleurs.dashboard.webapp.rest;

import com.excilys.shoofleurs.dashboard.business.dao.AbstractContentDao;
import com.excilys.shoofleurs.dashboard.webapp.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.business.service.DiaporamaService;
import com.excilys.shoofleurs.dashboard.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.entities.ImageContent;
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
import java.sql.SQLException;

@Stateless
@Path("contents")
public class AbstactContentResource {

	@EJB
	private AbstractContentDao mContentDao;

	@EJB
	private DiaporamaService mDiaporamaService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllContents(@QueryParam("start") int start, @QueryParam("offset") int offset) {
		return new Response(JsonMapper.objectAsJson(mContentDao.findAll(), Views.LightContent.class), 200);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContent(AbstractContent abstractContent) throws SQLException {
		ImageContent imageContent = new ImageContent("title", "http://vps229493.ovh.net:8080/dashboard-server-webapp-1.0-SNAPSHOT/img/img.jpg");
		imageContent.setDurationInDiaporama(100);
		imageContent.setGlobalDuration(200);
		AbstractContent a = mContentDao.create(imageContent);
		return new Response(JsonMapper.objectAsJson(a, Views.FullContent.class), 200);
	}
}
