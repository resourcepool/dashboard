package io.resourcepool.dashboard.rest.service.api;

import io.resourcepool.dashboard.rest.dtos.BundleDto;
import io.resourcepool.dashboard.rest.dtos.MediaDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BundleApi {
    @GET("/bundle")
    Call<List<BundleDto>> getBundles();

    @GET("/media/{bundleUuid}")
    Call<List<MediaDto>> getMedias(@Path("bundleUuid") String bundleUuid);
}
