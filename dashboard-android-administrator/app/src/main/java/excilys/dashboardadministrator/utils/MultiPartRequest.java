package excilys.dashboardadministrator.utils;

import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Requete pour du MultiPart
 *
 * {@link http://stackoverflow.com/questions/32240177/working-post-multipart-request-with-volley-and-without-httpentity}
 *
 * Created by Mickael on 22/04/2016.
 */
public class MultiPartRequest extends Request<NetworkResponse> {

	// ============================================================
	//	Constantes
	// ============================================================

	/**
	 * Contante pour la syntaxe du multi part
	 */
	private final static String TWO_HYPHENS = "--";
	private final static String LINE_END = "\r\n";

	// ============================================================
	//	Attributes - private
	// ============================================================
	private final Response.Listener<NetworkResponse> mResponseListener;
	private final Response.ErrorListener mErrorListener;
	private final Map<String, String> mHeaders;
	private final String mMimeType;
	private byte[] mBody;

	// ============================================================
	//	Constructeur
	// ============================================================

	public MultiPartRequest(int pMethod, String pUrl, Map<String, String> pHeaders, String pMimeType, byte[] pBody, Response.Listener<NetworkResponse> pResponseListener, Response.ErrorListener pErrorListener) {
		super(pMethod, pUrl, pErrorListener);

		this.mResponseListener = pResponseListener;
		this.mErrorListener = pErrorListener;
		this.mHeaders = pHeaders;
		this.mMimeType = pMimeType;
		this.mBody = pBody;
	}

	// ============================================================
	//	@Overridre - Request
	// ============================================================
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return (mHeaders != null) ? mHeaders : super.getHeaders();
	}

	@Override
	public String getBodyContentType() {
		return mMimeType;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		return mBody;
	}

	@Override
	protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
		try {
			return Response.success(
					response,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(NetworkResponse response) {
		if (mResponseListener != null) {
			mResponseListener.onResponse(response);
		}
	}

	@Override
	public void deliverError(VolleyError error) {
		if (mErrorListener != null) {
			mErrorListener.onErrorResponse(error);
		}
	}

	// ============================================================
	//	Inner Class
	// ============================================================

	/**
	 * Builder de MultiPart Request
	 */
	public static class Builder {
		private String mUrl;
		private final String boundary = "apiclient-" + System.currentTimeMillis();
		private final String mimeType = "multipart/form-data;boundary=" + boundary;
		private Map<String, String> mHeaders;
		private Response.Listener<NetworkResponse> mResponseListener;
		private Response.ErrorListener mErrorListener;

		private ByteArrayOutputStream bos = new ByteArrayOutputStream();
		private DataOutputStream dos = new DataOutputStream(bos);

		public Builder() {

		}

		/**
		 * Soécifie l'URL
		 * @param pUrl
		 */
		public Builder url(String pUrl) {
			mUrl = pUrl;
			return this;
		}

		/**
		 * Spécifie un header
		 * @param pHeaders
		 * @return
		 */
		public Builder headers(Map<String, String> pHeaders) {
			mHeaders = pHeaders;
			return this;
		}

		/**
		 * AJoute un fichier à la requete
		 * @param key Nom du champ
		 * @param fileData Tableau de d'octets représentant e contenu du fichier
		 * @param fileName  Nom du fichier à envoyer
		 */
		public Builder addPart(String key, byte[] fileData, String fileName) {
			try {
				dos.writeBytes(TWO_HYPHENS + boundary + LINE_END);

				dos.writeBytes("Content-Disposition: form-data; name=\"");
				dos.writeBytes(key);
				dos.writeBytes("\"; filename=\"");
				dos.writeBytes(fileName);
				dos.writeBytes("\"");
				dos.writeBytes(LINE_END);
				dos.writeBytes(LINE_END);

				ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
				int bytesAvailable = fileInputStream.available();

				int maxBufferSize = 1024 * 1024;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];

				int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				dos.writeBytes(LINE_END);
				return this;
			} catch (IOException pE) {
				Log.e("Exception", "", pE);
				// TODO revoir si on ne peut pas trouver un autre moyer de mieux gérer les exceptions
				throw new RuntimeException("Erreur lors de la création d'un MultipartRequest");
			}
		}

		public Builder addPart(String key, String value) {
			try {
				dos.writeBytes(TWO_HYPHENS + boundary + LINE_END);
				dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + LINE_END);
				dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + LINE_END);
				dos.writeBytes(LINE_END);
				dos.writeBytes(value);
				dos.writeBytes(LINE_END);
			} catch (IOException pE) {
				Log.e("Exception", "", pE);
				throw new RuntimeException("Erreur lors de la création d'un MultipartRequest");
			}
			return this;
		}

		/**
		 * AJoute un Listener de la réponse
		 * @param pResponseListener
		 * @return
		 */
		public Builder responseListener(Response.Listener<NetworkResponse> pResponseListener) {
			mResponseListener = pResponseListener;
			return this;
		}

		/**
		 * Ajoute un listener
		 * @param pErrorListener
		 * @return
		 */
		public Builder errorListener(Response.ErrorListener pErrorListener) {
			mErrorListener = pErrorListener;
			return this;
		}

		/**
		 * Construction de la requête
		 * @return
		 */
		public MultiPartRequest build() {
			try {
				dos.writeBytes(TWO_HYPHENS + boundary + TWO_HYPHENS + LINE_END);
			} catch (IOException pE) {
				throw new RuntimeException("Erreur lors de la création d'un MultipartRequest");
			}
			return new MultiPartRequest(Method.POST, mUrl, mHeaders, mimeType,  bos.toByteArray(), mResponseListener, mErrorListener);
		}
	}

}

