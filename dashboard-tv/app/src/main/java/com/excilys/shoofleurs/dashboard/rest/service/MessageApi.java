package com.excilys.shoofleurs.dashboard.rest.service;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MessageApi {
    @GET("message")
    Call<ServerResponse> getMessages();
}
