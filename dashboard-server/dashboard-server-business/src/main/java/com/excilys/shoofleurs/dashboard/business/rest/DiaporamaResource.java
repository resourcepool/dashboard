package com.excilys.shoofleurs.dashboard.business.rest;

import com.excilys.shoofleurs.dashboard.business.dao.DiaporamaDao;
import com.excilys.shoofleurs.dashboard.business.json.Response;
import com.excilys.shoofleurs.dashboard.business.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.entities.Diaporama;
import com.excilys.shoofleurs.dashboard.json.Views;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Stateless
@Path("diaporamas")
public class DiaporamaResource {

	@EJB
	private DiaporamaDao mDiaporamaDao;

	@GET
	public Response getAllDiaporamas() {
		return null;
	}

	@POST
	public Response newDiaporama(Diaporama diaporama) {
		diaporama = mDiaporamaDao.create(diaporama);
		return new Response(JsonMapper.objectAsJson(diaporama, Views.LightContent.class), 200);
	}
}
