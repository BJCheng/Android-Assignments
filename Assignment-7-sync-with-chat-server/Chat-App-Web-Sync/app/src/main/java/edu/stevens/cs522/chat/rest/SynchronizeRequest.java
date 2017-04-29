package edu.stevens.cs522.chat.rest;

import android.os.Parcel;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import edu.stevens.cs522.chat.entities.ChatMessage;

/**
 * Created by dduggan.
 */

public class SynchronizeRequest extends Request {

    public ChatMessage message;

    // Added by request processor
    public long lastSequenceNumber;

    public SynchronizeRequest(ChatMessage message) {
        super();
        this.message = message;
    }

    @Override
    public String getRequestEntity() throws IOException {
        // We stream output for SYNC, so this always returns null
        return null;
    }

    @Override
    public Response getResponse(HttpURLConnection connection, JsonReader rd) throws IOException {
        assert rd == null;
        return new SynchronizeResponse(connection);
    }

    @Override
    public Response process(RequestProcessor processor) {
        return processor.perform(this);
    }

//    @Override
//    public Map<String,String> getRequestHeaders() {
//        Map<String,String> headers = new HashMap<>();
//        headers.put(REQUEST_ID_HEADER, Long.toString(id));
//        headers.put(TIMESTAMP_HEADER, Long.toString(message.timestamp.getTime()));
//        headers.put(LONGITUDE_HEADER, Double.toString(message.longitude));
//        headers.put(LATITUDE_HEADER, Double.toString(message.latitude));
//        return headers;
//    }

    /*
    Parcelable
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO
        dest.writeParcelable(this.message, flags);
        dest.writeLong(this.lastSequenceNumber);
    }

    public SynchronizeRequest() {
        super();
    }

    public SynchronizeRequest(Parcel in) {
        super(in);
        // TODO
        this.message = (ChatMessage) in.readParcelable(ChatMessage.class.getClassLoader());
        this.lastSequenceNumber = in.readLong();
    }

    public static Creator<SynchronizeRequest> CREATOR = new Creator<SynchronizeRequest>() {
        @Override
        public SynchronizeRequest createFromParcel(Parcel source) {
            return new SynchronizeRequest(source);
        }

        @Override
        public SynchronizeRequest[] newArray(int size) {
            return new SynchronizeRequest[size];
        }
    };

}
