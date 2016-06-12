package com.excilys.shoofleurs.dashboard.rest.service.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DashboardApi {
    @GET("/dashboard/revision")
    Call<Long> getRevision();
}
