package com.excilys.shoofleurs.dashboard.webapp.rest;

import com.excilys.shoofleurs.dashboard.business.dao.AbstractContentDao;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.business.service.DiaporamaService;
import com.excilys.shoofleurs.dashboard.json.Views;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("contents")
public class ContentResource {

	@EJB
	private AbstractContentDao mContentDao;

	@EJB
	private DiaporamaService mDiaporamaService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllContents(@QueryParam("start") int start, @QueryParam("offset") int offset) {
		return new Response(JsonMapper.objectAsJson(mContentDao.findAll(), Views.LightContent.class), 200);
	}

	/*@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContent(@FormDataParam("file") InputStream uploadedInputStream) {
		if (uploadedInputStream != null) {
			try {
				OutputStream out;
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(new File("/main/webapp/img/image.jpg"));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (Exception e) {
				new Response("endexception", 400);
			}
			return new Response("end", 200);
		}
		return new Response("endNull", 500);
	}*/
}
