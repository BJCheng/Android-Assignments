package edu.stevens.cs522.chat.rest;

import android.net.Uri;
import android.os.Parcel;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.DateUtils;
import edu.stevens.cs522.chat.util.EnumUtils;

/**
 * Created by dduggan.
 */

public class PostMessageRequest extends Request {

    public String chatRoom;

    public String message;

    public PostMessageRequest(String chatName, UUID clientID, String chatRoom, String message) {
        super(chatName, clientID);
        //this.chatName = chatRoom;
        this.chatRoom = chatRoom;
        this.message = message;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String,String> headers = new HashMap<>();
        // TODO
        headers.put("X-Chat-Name", this.chatName);
        headers.put(REQUEST_ID_HEADER, String.valueOf(this.id));
//        headers.put("X-Client-ID", this.clientID.toString());
        headers.put(TIMESTAMP_HEADER, String.valueOf(this.timestamp.getTime()));
        headers.put(LATITUDE_HEADER, String.valueOf(this.latitude));
        headers.put(LONGITUDE_HEADER, String.valueOf(this.longitude));
        return headers;
    }

    @Override
    public String getRequestEntity() throws IOException {
        StringWriter wr = new StringWriter();
        JsonWriter jw = new JsonWriter(wr);
        // TODO write a JSON message of the form:
        // { "room" : <chat-room-name>, "message" : <message-text> }
        jw.beginObject();
        jw.name("chatroom").value(this.chatRoom);
        jw.name("text").value(this.message);
        jw.endObject();
        return wr.toString();
    }

    @Override
    public Response getResponse(HttpURLConnection connection, JsonReader rd) throws IOException{
        return new PostMessageResponse(connection, rd);
    }

    @Override
    public Response process(RequestProcessor processor) {
        return processor.perform(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO
        dest.writeLong(this.id);
        dest.writeString(this.chatRoom);
        dest.writeString(this.message);
        dest.writeSerializable(this.timestamp);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    public PostMessageRequest(String chatName, UUID clientID) {
        super(chatName, clientID);
    }

    public PostMessageRequest(Parcel in) {
        super(in);
        // TODO
        this.id = in.readLong();
        this.chatRoom = in.readString();
        this.message = in.readString();
        this.timestamp = (Date) in.readSerializable();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static Creator<PostMessageRequest> CREATOR = new Creator<PostMessageRequest>() {
        @Override
        public PostMessageRequest createFromParcel(Parcel source) {
            return new PostMessageRequest(source);
        }

        @Override
        public PostMessageRequest[] newArray(int size) {
            return new PostMessageRequest[size];
        }
    };

}
