package excilys.dashboardadministrator.rest;

import excilys.dashboardadministrator.model.json.ServerResponse;
import excilys.dashboardadministrator.utils.Data;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ContentApi {

    @POST(Data.POST_CONTENT_URL)
    @Multipart
    Call<ServerResponse> postContents(@Header("content") String content,
                                      @Part MultipartBody.Part image);
}
