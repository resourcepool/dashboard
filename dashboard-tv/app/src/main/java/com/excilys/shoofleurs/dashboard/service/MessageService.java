package com.excilys.shoofleurs.dashboard.service;

import android.util.Log;

import com.android.volley.TimeoutError;
import com.excilys.shoofleurs.dashboard.model.json.ServerResponse;
import com.excilys.shoofleurs.dashboard.rest.IMessageApi;
import com.excilys.shoofleurs.dashboard.rest.JsonMapperUtils;
import com.excilys.shoofleurs.dashboard.rest.ServiceGenerator;
import com.excilys.shoofleurs.dashboard.ui.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.model.entities.Message;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class performs all messages requests to the server.W
 */
public class MessageService {
    private static MessageService S_INSTANCE;

    private DashboardActivity mDashboardActivity;

    private IMessageApi mMessageApi;

    public static MessageService getInstance(DashboardActivity dashboardActivity){
        if (S_INSTANCE == null) S_INSTANCE = new MessageService(dashboardActivity);
        return S_INSTANCE;
    }

    private MessageService(DashboardActivity dashboardActivity) {
        this.mDashboardActivity = dashboardActivity;
        mMessageApi = ServiceGenerator.createService(IMessageApi.class);
    }


    public void checkUpdates() {
        Call<ServerResponse> call = mMessageApi.getMessages();

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    List<Message> messages = JsonMapperUtils.getServerResponseContent(serverResponse, new TypeReference<List<Message>>() {});

                    Log.i(MessageService.class.getSimpleName(), "onResponse: "+ messages);
                    if (messages.size() != 0) {
                        mDashboardActivity.getMessageController().addMessages(messages);
                    }
                    else {
                        Log.i(MessageService.class.getSimpleName(), "checkUpdate onResponse: empty");
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(MessageService.class.getSimpleName(), "checkUpdate onErrorResponse: "+t.toString());
                if (t instanceof TimeoutError) {
                    Log.i(MessageService.class.getSimpleName(), "TimoutError: trying to resend the request");
                    checkUpdates();
                }
            }
        });

        /* Simulate response from the server */
        Message message1 = new Message("Java News", "Oracle is seeking as much as US $9.3 billion in damages in a long-running copyright lawsuit against Google over its use of Java in Android, court filings show.");
        Message message2 = new Message("Google News", "Google’s powerful Cloud Vision API now available");
        Message message3 = new Message("Microsoft News", "Microsoft and Canonical partner to bring Ubuntu to Windows 10 \"You'll soon be able to run Ubuntu on Windows 10.\"");
        mDashboardActivity.getMessageController().addMessages(Arrays.asList(message1, message2, message3));
    }
}
