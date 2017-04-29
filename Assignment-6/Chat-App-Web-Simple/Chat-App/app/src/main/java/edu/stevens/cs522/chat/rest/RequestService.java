package edu.stevens.cs522.chat.rest;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;

import edu.stevens.cs522.chat.util.ResultReceiverWrapper;

import static android.content.Intent.ACTION_SEND;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RequestService extends IntentService {

    public static final String SERVICE_REQUEST_KEY = "edu.stevens.cs522.chat.rest.extra.REQUEST";
    public static final String RESULT_RECEIVER_KEY = "edu.stevens.cs522.chat.rest.extra.RECEIVER";
    private RequestProcessor processor;
    private RestMethod restMethod;

    public RequestService() {
        super("RequestService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        processor = new RequestProcessor(this);
        restMethod = new RestMethod(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Request request = intent.getParcelableExtra(SERVICE_REQUEST_KEY);
        ResultReceiver receiver = intent.getParcelableExtra(RESULT_RECEIVER_KEY);

        Response response = processor.process(request);  //將「依照不同的request使用不同種的restMethod」封裝起來

        if (receiver != null) {
            // TODO UI should display a toast message on completion of the operation
            if(response.getClass().getName().equals("edu.stevens.cs522.chat.rest.PostMessageResponse")){
                PostMessageResponse postMessageResponse = (PostMessageResponse) response;
                Bundle bundle = new Bundle();
                bundle.putInt(ResultReceiverWrapper.MESSAGE_SEQUENCE_NUMBER, postMessageResponse.messageSequenceNumber);
                receiver.send(Activity.RESULT_OK, bundle);
                return;
            }
            receiver.send(Activity.RESULT_OK, null);
        }
    }
}
