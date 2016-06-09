package com.excilys.shoofleurs.dashboard.rest.service;

import com.excilys.shoofleurs.dashboard.rest.dtos.BundleDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BundleApi {
    @GET("bundle")
    Call<List<BundleDto>> getBundles();
}
