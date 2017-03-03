package io.resourcepool.dashboard.rest.service;

import io.resourcepool.dashboard.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    public static final String API_KEY_HEADER = "x-api-key";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder requestBuilder = request.newBuilder()
                        .addHeader(API_KEY_HEADER, BuildConfig.API_KEY)
                        .method(request.method(), request.body());

                return chain.proceed(requestBuilder.build());
            }
        });

        OkHttpClient client = clientBuilder.build();

        return builder.client(client).build().create(serviceClass);
    }
}
