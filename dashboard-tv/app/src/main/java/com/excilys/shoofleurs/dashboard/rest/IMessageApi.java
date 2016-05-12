package com.excilys.shoofleurs.dashboard.rest;

import com.excilys.shoofleurs.dashboard.model.json.ServerResponse;
import com.excilys.shoofleurs.dashboard.utils.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IMessageApi {
    @GET(Data.GET_MESSAGES_URL)
    Call<ServerResponse> getMessages();
}
