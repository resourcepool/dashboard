package com.excilys.shoofleurs.dashboard.webapp.rest;

import com.excilys.shoofleurs.dashboard.business.dao.AbstractContentDao;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.business.service.DiaporamaService;
import com.excilys.shoofleurs.dashboard.json.Views;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;

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

	@GET
	@Path("test")
	public javax.ws.rs.core.Response test() {
		return javax.ws.rs.core.Response.ok("Salut").build();
	}

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public javax.ws.rs.core.Response setImageProfielPicture(FormDataMultiPart form) {

		FormDataBodyPart filePart = form.getField("file");

		ContentDisposition headerOfFilePart =  filePart.getContentDisposition();

		InputStream fileInputStream = filePart.getValueAs(InputStream.class);

		String nameFile = headerOfFilePart.getFileName().substring(headerOfFilePart.getFileName().lastIndexOf("."));
		String filePath = "images/" + nameFile;

		// save the file to the server
		saveFile(fileInputStream, filePath);

		String output = "File saved to server location : " + filePath;
//		CDAO.getEntityManager().refresh(account);
		//noinspection deprecation
//		account.setProfilPicture(URLEncoder.encode(nameFile));
//		CDAO.getAccountDAO().update(account);
		return javax.ws.rs.core.Response.created(URI.create("")).build();
	}

	private void saveFile(InputStream uploadedInputStream,
						  String serverLocation) {

		try {
			OutputStream outpuStream;
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
