package edu.stevens.cs522.chat.rest;

import android.content.Context;
import android.database.Cursor;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.Peer;
import edu.stevens.cs522.chat.managers.MessageManager;
import edu.stevens.cs522.chat.managers.PeerManager;
import edu.stevens.cs522.chat.managers.RequestManager;
import edu.stevens.cs522.chat.managers.TypedCursor;
import edu.stevens.cs522.chat.util.StringUtils;

/**
 * Created by dduggan.
 */

public class RequestProcessor {

    private Context context;

    private RestMethod restMethod;

    private RequestManager requestManager;

    public RequestProcessor(Context context) {
        this.context = context;
        this.restMethod = new RestMethod(context);
        this.requestManager = new RequestManager(context);
    }

    public Response process(Request request) {
        return request.process(this);
    }

    public Response perform(RegisterRequest request) {
        return restMethod.perform(request);
    }

    public Response perform(PostMessageRequest request) {
        // We will just insert the message into the database, and rely on background sync to upload
        // return restMethod.perform(request)
        requestManager.persist(request.message);
        return request.getDummyResponse();
    }

    public Response perform(final SynchronizeRequest request) {
        RestMethod.StreamingResponse response = null;
        final TypedCursor<ChatMessage> messages = requestManager.getUnsentMessages();
        request.lastSequenceNumber = requestManager.getLastSequenceNumber();
        request.timestamp = new Date();
        request.latitude = 0.0;
        request.longitude = 0.0;
        int numMessagesReplaced = messages.getCount();
        if (numMessagesReplaced < 1)
            return new DummyResponse(0);
        try {

//                RequestManager requestManager = new RequestManager(this);
//                long lastSequence = requestManager.getLastSequenceNumber();
//                Cursor cursor = this.getContentResolver().query(MessageContract.CONTENT_URI, null,
//                        MessageContract.SEQUENCE_NUMBER + "=?", new String[]{String.valueOf(lastSequence)}, null);
//                if(cursor.moveToFirst()){
//                    ChatMessage message = new ChatMessage(cursor, true);
//                    syncRequest = new SynchronizeRequest(message);
//                    syncRequest.lastSequenceNumber = requestManager.getLastSequenceNumber();
//                }

            RestMethod.StreamingOutput out = new RestMethod.StreamingOutput() {
                @Override
                public void write(final OutputStream os) throws IOException {
                    try {
                        JsonWriter wr = new JsonWriter(new OutputStreamWriter(new BufferedOutputStream(os)));
                        wr.beginArray();
                        /*
                         * TODO stream unread messages to the server:
                         * {
                         *   chatroom : ...,
                         *   timestamp : ...,
                         *   latitude : ...,
                         *   longitude : ....,
                         *   text : ...
                         * }
                         */
                        while (messages.moveToNext()) {
                            ChatMessage message = new ChatMessage(messages.getCursor(), false);
                            wr.beginObject();
                            wr.name("chatroom");
                            wr.value(message.chatRoom);
                            wr.name("timestamp");
                            wr.value(message.timestamp.getTime());
                            wr.name("latitude");
                            wr.value(message.latitude);
                            wr.name("longitude");
                            wr.value(message.longitude);
                            wr.name("text");
                            wr.value(message.messageText);
                            wr.endObject();
                        }
                        wr.endArray();
                        wr.flush();
                    } finally {
                        messages.close();
                    }
                }
            };
            response = restMethod.perform(request, out);

            JsonReader rd = new JsonReader(new InputStreamReader(new BufferedInputStream(response.getInputStream()), StringUtils.CHARSET));
            // TODO parse data from server (messages and peers) and update database
            // See RequestManager for operations to help with this.
            List<ChatMessage> downloadedMessages = new LinkedList<>();
            List<Peer> downloadedPeers = new ArrayList<>();
            rd.beginObject();
            while (rd.hasNext()) {
                String nextName = rd.nextName();
                if (nextName.equals("clients")){
                    rd.beginArray();
                    while(rd.hasNext()){
                        Peer peer = new Peer();
                        rd.beginObject();
                        while(rd.hasNext()){
                            switch (rd.nextName()){
                                case "username":
                                    peer.name = rd.nextString();
                                    break;
                                case "timestamp":
                                    peer.timestamp = new Date(rd.nextLong());
                                    break;
                                case "latitude":
                                    peer.latitude = rd.nextDouble();
                                    break;
                                case "longitude":
                                    peer.longitude = rd.nextDouble();
                                    break;
                            }
                        }
                        rd.endObject();
                        downloadedPeers.add(peer);
                    }
                    rd.endArray();
                }
                else if (nextName.equals("messages")) {
                    rd.beginArray();
                    while (rd.hasNext()) {
                        ChatMessage message = new ChatMessage();
                        rd.beginObject();
                        while (rd.hasNext()) {
                            switch (rd.nextName()) {
                                case "chatroom":
                                    message.chatRoom = rd.nextString();
                                    break;
                                case "timestamp":
                                    message.timestamp = new Date(rd.nextLong());
                                    break;
                                case "latitude":
                                    message.latitude = rd.nextDouble();
                                    break;
                                case "longitude":
                                    message.longitude = rd.nextDouble();
                                    break;
                                case "seqnum":
                                    message.seqNum = rd.nextInt();
                                    break;
                                case "sender":
                                    message.sender = rd.nextString();
                                    break;
                                case "text":
                                    message.messageText = rd.nextString();
                                    break;
                            }
                        }
                        rd.endObject();
                        downloadedMessages.add(message);
                    }
                    rd.endArray();
                } else
                    rd.skipValue();
            }
            rd.endObject();
            requestManager.syncMessages(downloadedMessages.size(), downloadedMessages);
            requestManager.deletePeers();
            for(Peer peer: downloadedPeers){
                requestManager.persist(peer);
            }

            return response.getResponse();

        } catch (IOException e) {
            return new ErrorResponse(request.id, e);

        } finally {
            if (response != null) {
                response.disconnect();
            }
        }
    }

}
