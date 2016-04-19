package com.excilys.shoofleurs.dashboard.requests;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class Post<T> extends AsyncTask<Void, Void, T> {
    private static final String BASE_URL = "http://192.168.99.100:8080/dashboard-server-webapp-1.0-SNAPSHOT/api/";

    private ICallback mCallback;

    private Class mClass;

    private T mObject;

    private int mCode;


    public Post(ICallback callback, Class type, int code, T object) {
        mCallback = callback;
        mClass = type;
        mCode = code;
        mObject = object;
    }


    @Override
    @SuppressWarnings("all")
    protected T doInBackground(Void... params) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpEntity<T> entity = new HttpEntity<T>(mObject);
        ResponseEntity<T> responseEntity = restTemplate.exchange(BASE_URL + "diaporamas",
                HttpMethod.POST, entity, mClass);
        return responseEntity.getBody();
    }


    @Override
    protected void onPostExecute(T result) {
        System.out.println("fini");
    }
}
