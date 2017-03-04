package io.resourcepool.dashboard.rest.service.api;

import io.resourcepool.dashboard.rest.dtos.Device;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DiscoveryApi {
    @POST("/discovery/{deviceId}")
    Call<Device> registerDevice(@Path("deviceId") String deviceId);

}
