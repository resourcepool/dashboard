package com.excilys.shoofleurs.dashboard.service;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.excilys.shoofleurs.dashboard.ui.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.model.entities.Message;
import com.excilys.shoofleurs.dashboard.rest.JsonRequest;
import com.excilys.shoofleurs.dashboard.utils.Data;

import java.util.Arrays;

/**
 * This class performs all messages requests to the server.
 */
public class MessageService {
    private static MessageService S_INSTANCE;

    private DashboardActivity mDashboardActivity;

    public static MessageService getInstance(DashboardActivity dashboardActivity){
        if (S_INSTANCE == null) S_INSTANCE = new MessageService(dashboardActivity);
        return S_INSTANCE;
    }

    private MessageService(DashboardActivity dashboardActivity) {
        this.mDashboardActivity = dashboardActivity;
    }



    public void checkUpdates() {
        @SuppressWarnings("unchecked")
        JsonRequest<Message[]> request = new JsonRequest<>(Data.GET_MESSAGES_URL, Message[].class, null, new Response.Listener<Message[]>() {
            @Override
            public void onResponse(Message[] response) {
                Log.i(MessageService.class.getSimpleName(), "onResponse: "+ Arrays.asList(response));
                if (response.length != 0) {
                    mDashboardActivity.getMessageController().addMessages(response);
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
        /* Simulate response from the server */
        Message message1 = new Message("Java News", "Oracle is seeking as much as US $9.3 billion in damages in a long-running copyright lawsuit against Google over its use of Java in Android, court filings show.");
        Message message2 = new Message("Google News", "Google’s powerful Cloud Vision API now available");
        Message message3 = new Message("Microsoft News", "Microsoft and Canonical partner to bring Ubuntu to Windows 10 \"You'll soon be able to run Ubuntu on Windows 10.\"");
        mDashboardActivity.getMessageController().addMessages(message1, message2, message3);
    }
}
