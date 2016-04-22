package com.excilys.shoofleurs.dashboard.webapp.rest;

import com.excilys.shoofleurs.dashboard.business.service.ContentService;
import com.excilys.shoofleurs.dashboard.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.Response;
import com.excilys.shoofleurs.dashboard.webapp.rest.json.mapper.JsonMapper;
import com.excilys.shoofleurs.dashboard.business.service.DiaporamaService;
import com.excilys.shoofleurs.dashboard.json.Views;
import com.excilys.shoofleurs.dashboard.webapp.rest.utils.SaveFile;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;


@Stateless
@Path("contents")
public class ContentResource {

	@EJB
	private ContentService mContentService;

	@EJB
	private DiaporamaService mDiaporamaService;


	/**
	 * Metho to create a new content. To create a content, the request must contain a file and the information
	 * about the content in the header. The parameter use in the header is "content". It contains the JSON
	 * of the content to persit in the database.
	 * @param uploadedInputStream File send with multipart form data
	 * @param fileDetail Details about de file (name, extensions...)
	 * @param contentAsJson AbstractContent as Json
	 * @return A response with an error or the content created if the request succeed
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContent(@FormDataParam("file") InputStream uploadedInputStream,
							   @FormDataParam("file") FormDataContentDisposition fileDetail,
							   @HeaderParam("content") String contentAsJson) {
		if (contentAsJson != null && uploadedInputStream != null) {
			AbstractContent abstractContent = JsonMapper.jsonAsAbstractContent(contentAsJson);
			if (abstractContent == null) {
				return new Response("JSON malformed", 500);
			}

			abstractContent = mContentService.create(abstractContent);
			if (abstractContent == null) {
				return new Response("Error during persistence. See logs for more info", 500);
			}

			String name = SaveFile.saveFile(uploadedInputStream, fileDetail, abstractContent.getId());
			if (name == null) {
				return new Response("Error during the upload", 500);
			}

			abstractContent.setUrl("http://vps229493.ovh.net:8080/dashboard/img/" + name);
			abstractContent = mContentService.update(abstractContent);
			abstractContent.getDiaporama().addContent(abstractContent);
			mDiaporamaService.update(abstractContent.getDiaporama());

			return new Response(JsonMapper.objectAsJson(abstractContent, Views.FullContent.class), 200);
		}
		return new Response("File or json missing. Request malformed.", 500);
	}
}
