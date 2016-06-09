package com.excilys.shoofleurs.dashboard.rest.service;

import com.excilys.shoofleurs.dashboard.model.entities.Message;
import com.excilys.shoofleurs.dashboard.rest.events.MessageUpdatesEvent;
import com.excilys.shoofleurs.dashboard.rest.events.MessageUpdatesResponseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;

/**
 * This class performs all messages requests to the server.W
 */
public class NewsService {
    private MessageApi mMessageApi;
    private EventBus mEventBus;

    public NewsService(EventBus eventBus) {
        this.mEventBus = eventBus;
        mEventBus.register(this);
        mMessageApi = ServiceGenerator.createService(MessageApi.class);
    }

    @Subscribe
    public void onMessageUpdatesEvent(MessageUpdatesEvent messageUpdatesEvent) {
//        Call<ServerResponse> call = mMessageApi.getMessages();
//
//        call.enqueue(new Callback<ServerResponse>() {
//            @Override
//            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
//                ServerResponse serverResponse = response.body();
//                if (serverResponse != null) {
//                    List<Message> messages = JsonMapperUtils.getServerResponseContent(serverResponse, new TypeReference<List<Message>>() {
//                    });
//
//                    Log.i(NewsService.class.getSimpleName(), "onResponse: " + messages);
//                    if (messages.size() != 0) {
//                        mEventBus.post(new MessageUpdatesResponseEvent(messages));
//                    } else {
//                        Log.i(NewsService.class.getSimpleName(), "checkUpdate onResponse: empty");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ServerResponse> call, Throwable t) {
//                Log.e(NewsService.class.getSimpleName(), "checkUpdate onErrorResponse: " + t.toString());
//                /**TODO renvoyer la requete si le réseau est disponible à nouveau **/
//            }
//        });

        /* Simulate response from the server */
        Message message1 = new Message("Java News", "Oracle is seeking as much as US $9.3 billion in damages in a long-running copyright lawsuit against Google over its use of Java in Android, court filings show.");
        Message message2 = new Message("Google News", "Google’s powerful Cloud Vision API now available");
        Message message3 = new Message("Microsoft News", "Microsoft and Canonical partner to bring Ubuntu to Windows 10 \"You'll soon be able to run Ubuntu on Windows 10.\"");
        mEventBus.post(new MessageUpdatesResponseEvent(Arrays.asList(message1, message2, message3)));
    }
}
