package io.resourcepool.dashboard.rest.service.api;

import io.resourcepool.dashboard.rest.dtos.BundleDto;
import io.resourcepool.dashboard.rest.dtos.MediaDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BundleApi {
    @GET("/bundle/{tag}")
    Call<BundleDto> getBundle(@Path("tag") String tag);

    @GET("/media/{uuid}")
    Call<MediaDto> getMedia(@Path("uuid") String mediaUuid);
}
