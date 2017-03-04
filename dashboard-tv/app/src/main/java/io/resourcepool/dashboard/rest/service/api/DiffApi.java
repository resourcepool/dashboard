package io.resourcepool.dashboard.rest.service.api;

import io.resourcepool.dashboard.rest.dtos.ChangesetWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DiffApi {

    @GET("/revision")
    Call<Long> getLatestRevision();

    @GET("/revision/{revision}/feed/{feedId}")
    Call<ChangesetWrapper> loadDiff(@Path("revision") Long revision, @Path("feedId") String feedId);

}
