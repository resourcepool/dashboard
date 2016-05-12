package com.excilys.shoofleurs.dashboard.rest;

import com.excilys.shoofleurs.dashboard.model.json.ServerResponse;
import com.excilys.shoofleurs.dashboard.utils.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISlideShowApi {
    public static final String TYPE_TV = "tv";
    @GET(Data.GET_SLIDESHOWS_URL)
    Call<ServerResponse> getSlideShows(@Query("json") String type);
}