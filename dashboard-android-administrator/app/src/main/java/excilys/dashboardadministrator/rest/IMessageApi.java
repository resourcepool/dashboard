package excilys.dashboardadministrator.rest;

import excilys.dashboardadministrator.model.json.ServerResponse;
import excilys.dashboardadministrator.utils.Data;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IMessageApi {
    @GET(Data.GET_MESSAGES_URL)
    Call<ServerResponse> getMessages();
}
