package com.excilys.shoofleurs.dashboard.requests;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.excilys.shoofleurs.dashboard.model.json.ServerResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Cette classe permet d'éffectuer une requête et parser automatiquement la réponse Json
 * en un type spécifier en générique.
 * @param <T>
 */
public class JsonRequest<T> extends Request<T> {
    private final ObjectMapper mObjectMapper;
    private final Class<T> mClazz;
    private final Map<String, String> mHeaders;
    private final Response.Listener<T> mListener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param mClazz Relevant class object, for Gson's reflection
     * @param headers Map of request mHeaders
     */
    public JsonRequest(String url, Class<T> mClazz, Map<String, String> headers,
                       Response.Listener<T> mListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.mClazz = mClazz;
        this.mHeaders = headers;
        this.mListener = mListener;
        this.mObjectMapper = new ObjectMapper();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Log.i(getClass().getSimpleName(), "parseNetworkResponse: " + new String(response.data));
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            ServerResponse serverResponse =  mObjectMapper.readValue(json, ServerResponse.class);
            return Response.success(
                    mObjectMapper.readValue(serverResponse.getObjectAsJson(), mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonParseException e) {
            return Response.error(new ParseError(e));
        } catch (JsonMappingException e) {
            return Response.error(new ParseError(e));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}