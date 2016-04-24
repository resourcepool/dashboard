package excilys.dashboardadministrator.examples;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import excilys.dashboardadministrator.R;
import excilys.dashboardadministrator.singletons.VolleySingleton;
import excilys.dashboardadministrator.utils.MultiPartRequest;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * Created by Mickael on 24/04/2016.
 */
public class MultiPartRequestSample {

	private static final String TAG = "MultiPartRequestSample";

	@Test
	public void testName() throws Exception {
		MultiPartRequest.Builder builder = new MultiPartRequest.Builder();
		builder.url("http://192.168.1.20:8080/webapp/api/contents")
				.responseListener(new Response.Listener<NetworkResponse>() {
					@Override
					public void onResponse(NetworkResponse response) {
						Log.d(TAG, "#onResponse() called with " + "response = [" + String.valueOf(response) + "]");
					}
				})
				.errorListener(new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "#onErrorResponse() called with " + "error = [" + String.valueOf(error) + "]");
						if (error instanceof TimeoutError) {
							TimeoutError timeoutError = (TimeoutError) error;
							Log.d(TAG, "timeoutError. : "+String.valueOf(timeoutError.getNetworkTimeMs()));
							Log.d(TAG, "timeoutError. : "+String.valueOf(timeoutError.networkResponse));
						}
					}
				});

		// C'est en commentaire, car si non, cela ne compile car car les objets Diaporama ne sont pas encore implanté.

//		Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher);
//		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
//		builder.addPart("file", byteArrayOutputStream.toByteArray(), "icon.png");
//		Diaporama diaporama = new Diaporama();
//		diaporama.setId(98);
//		diaporama.setEndDateTime("02/02/2006");
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			builder.addPart("text", objectMapper.writeValueAsString(diaporama));
//		} catch (JsonProcessingException pE) {
//			throw new RuntimeException();
//		}
//
//		VolleySingleton.getInstance(this).addToRequestQueue(builder.build());
	}
}
