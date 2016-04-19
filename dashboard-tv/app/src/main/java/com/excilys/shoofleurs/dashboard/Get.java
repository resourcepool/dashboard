package com.excilys.shoofleurs.dashboard;


import android.os.AsyncTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 * La requête vers le serveur REST est faite via Spring (REST template) pour Android.
 * Le type T permet de déterminer le type d'élément renvoyé par l'API afin de permettre le
 * mapping JSON - T. L'objet Class passé en paramètre permet de faire cela également dans
 * l'appel de la méthode exchange (T ne pouvant être passé en tant que paramètre).
 *
 * Le code passé en paramètre n'est utilisé que par l'interface ICallback. L'AsyncTask ne fait
 * que transmettre l'information qu'elle a reçue.
 */
public class Get<T> extends AsyncTask<Void, Void, T> {


    private static final String BASE_URL = "http://192.168.43.57:8080/rest/";

    private ICallback mCallback;

    private Class mClass;

    private int mCode;


    public Get(ICallback callback, Class type, int code) {
        mCallback = callback;
        mClass = type;
        mCode = code;
    }


    @Override
    @SuppressWarnings("all")
    protected T doInBackground(Void... params) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ResponseEntity<T> responseEntity = restTemplate.exchange(BASE_URL + "diaporamas/1/contents",
                HttpMethod.GET, null, mClass);
        return responseEntity.getBody();
    }


    @Override
    protected void onPostExecute(T result) {
        mCallback.asyncTaskFinish(result, mCode);
    }
}
