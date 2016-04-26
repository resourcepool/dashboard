package excilys.dashboardadministrator.service;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.Arrays;
import java.util.List;

import excilys.dashboardadministrator.model.entities.Message;
import excilys.dashboardadministrator.rest.JsonRequest;
import excilys.dashboardadministrator.utils.Data;

/**
 * This class performs all messages requests to the server.
 */
public class MessageService {
    private static MessageService S_INSTANCE;

    private OnMessageServiceResponse mListener;

    public static MessageService getInstance(OnMessageServiceResponse listener){
        if (S_INSTANCE == null) S_INSTANCE = new MessageService(listener);
        return S_INSTANCE;
    }

    private MessageService(OnMessageServiceResponse listener) {
        this.mListener = listener;
    }



    public void checkUpdates() {
        @SuppressWarnings("unchecked")
        JsonRequest<Message[]> request = new JsonRequest<>(Data.GET_MESSAGES_URL, Message[].class, null, new Response.Listener<Message[]>() {
            @Override
            public void onResponse(Message[] response) {
                Log.i(MessageService.class.getSimpleName(), "onResponse: "+ Arrays.asList(response));
                if (response.length != 0) {
                    mListener.onCheckUpdatesResponse(Arrays.asList(response));
                }
                else {
                    Log.i(MessageService.class.getSimpleName(), "checkUpdate onResponse: empty");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(MessageService.class.getSimpleName(), "checkUpdate onErrorResponse: "+error);
                if (error instanceof TimeoutError) {
                    Log.i(MessageService.class.getSimpleName(), "TimoutError: trying to resend the request");
                    checkUpdates();
                }
            }
        });
    }

    public interface OnMessageServiceResponse {
        void onCheckUpdatesResponse(List<Message> messages);
    }
}
