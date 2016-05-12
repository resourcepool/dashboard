package excilys.dashboardadministrator.service;

import android.util.Log;

import com.android.volley.TimeoutError;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.List;

import excilys.dashboardadministrator.model.entities.Message;
import excilys.dashboardadministrator.model.json.ServerResponse;
import excilys.dashboardadministrator.rest.IMessageApi;
import excilys.dashboardadministrator.utils.JsonMapperUtils;
import excilys.dashboardadministrator.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * This class performs all messages requests to the server.
 */
public class MessageService {
    private static MessageService S_INSTANCE;

    private OnMessageServiceResponse mListener;

    private IMessageApi mMessageApi;

    public static MessageService getInstance(OnMessageServiceResponse listener){
        if (S_INSTANCE == null) S_INSTANCE = new MessageService(listener);
        return S_INSTANCE;
    }

    private MessageService(OnMessageServiceResponse listener) {
        this.mListener = listener;
        mMessageApi = ServiceGenerator.createService(IMessageApi.class);
    }



    public void checkUpdates() {
        Call<ServerResponse> call = mMessageApi.getMessages();

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    List<Message> messages = JsonMapperUtils.getServerResponseContent(serverResponse, new TypeReference<List<Message>>() {});
                    Log.i(MessageService.class.getSimpleName(), "onResponse: "+ Arrays.asList(response));
                    if (messages.size() != 0) {
                        if (mListener != null) {
                            mListener.onCheckUpdatesResponse(messages);
                        }
                    }
                    else {
                        Log.i(MessageService.class.getSimpleName(), "checkUpdate onResponse: empty");
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(MessageService.class.getSimpleName(), "checkUpdate onErrorResponse: "+t);
                if (t instanceof TimeoutError) {
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
