package com.excilys.shoofleurs.dashboard.webapp.rest.utils;

import com.sun.jersey.core.header.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Hugo Bernardi
 */
public class SaveFile {

	private static final Logger LOGGER = LoggerFactory.getLogger(SaveFile.class.getSimpleName());

	/**
	 * Create a new file in the app folder accessible via url. All the data are provided by REST request.
	 * @param file File to save on the server
	 * @param data Informations about the image
	 * @param idContent Content Id associate with the file
	 * @return The path of the created file
	 */
	public static String saveFile(InputStream file, FormDataContentDisposition data, int idContent) {
		try {
			OutputStream out;
			int read;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File("/root/payara41/glassfish/domains/domain1/applications/dashboard/img/"
					+ idContent + data.getFileName().substring(data.getFileName().lastIndexOf("."))));
			while ((read = file.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			LOGGER.error("Error during the upload of the file. Cause : " + e.getMessage());
			return null;
		}
		return idContent + data.getFileName().substring(data.getFileName().lastIndexOf("."));
	}
}
