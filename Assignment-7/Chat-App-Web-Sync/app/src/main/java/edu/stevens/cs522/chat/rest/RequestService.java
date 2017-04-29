package edu.stevens.cs522.chat.rest;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.Toast;

import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.managers.RequestManager;
import edu.stevens.cs522.chat.util.ResultReceiverWrapper;

import static android.content.Intent.ACTION_SEND;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RequestService extends IntentService {

    public static final String SERVICE_REQUEST_KEY = "edu.stevens.cs522.chat.rest.extra.REQUEST";
    public static final String RESULT_RECEIVER_KEY = "edu.stevens.cs522.chat.rest.extra.RECEIVER";
    public static final String MESSAGE_KEY = "MESSAGE_KEY";

    private RequestProcessor processor;

    public RequestService() {
        super("RequestService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        processor = new RequestProcessor(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Request request = intent.getParcelableExtra(SERVICE_REQUEST_KEY);
        ResultReceiver receiver = intent.getParcelableExtra(RESULT_RECEIVER_KEY);

        if (request == null) return;

        Response response = processor.process(request);

        if (receiver != null) {
            // TODO UI should display a toast message on completion of the operation
            if (response.getClass().getName().equals("edu.stevens.cs522.chat.rest.PostMessageResponse")) {
//                PostMessageResponse postMessageResponse = (PostMessageResponse) response;
//                Bundle bundle = new Bundle();
//                bundle.putInt(ResultReceiverWrapper.S, postMessageResponse.messageSequenceNumber);
                receiver.send(Activity.RESULT_OK, null);
                return;
            }
            receiver.send(Activity.RESULT_OK, null);
            //According to TA, I should not display toast on client side.
            Toast.makeText(this, "Request Successfully Handled.", Toast.LENGTH_LONG).show();
        }
    }

}
