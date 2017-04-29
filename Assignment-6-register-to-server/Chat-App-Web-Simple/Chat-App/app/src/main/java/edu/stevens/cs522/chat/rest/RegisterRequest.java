package edu.stevens.cs522.chat.rest;

import android.net.Uri;
import android.os.Parcel;
import android.util.JsonReader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.stevens.cs522.chat.util.EnumUtils;

/**
 * Created by dduggan.
 */

public class RegisterRequest extends Request {

    public RegisterRequest(String chatName, UUID clientID) {
        super(chatName, clientID);
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        // TODO
        headers.put("X-Chat-Name", this.chatName);
        headers.put("X-Request-ID", String.valueOf(this.id));
        headers.put("X-Client-ID", this.clientID.toString());
        headers.put("X-Timestamp", String.valueOf(this.timestamp.getTime()));
        headers.put("X-Latitude", String.valueOf(this.latitude));
        headers.put("X-Longitude", String.valueOf(this.longitude));
        return headers;
    }

    @Override
    public String getRequestEntity() throws IOException {
        return null;
    }

    @Override
    public Response getResponse(HttpURLConnection connection, JsonReader rd) throws IOException {
        return new RegisterResponse(connection);
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
        EnumUtils.writeEnum(dest, this.status);
        dest.writeString(this.chatName);
        dest.writeSerializable(this.clientID);
        dest.writeLong(this.version);
        dest.writeSerializable(this.timestamp);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }

    public RegisterRequest(Parcel in) {
        super(in);
        // TODO
        this.status = EnumUtils.readEnum(StatusType.class, in);
        this.chatName = in.readString();
        this.clientID = (UUID) in.readSerializable();
        this.version = in.readLong();
        this.timestamp = (Date) in.readSerializable();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public static Creator<RegisterRequest> CREATOR = new Creator<RegisterRequest>() {
        @Override
        public RegisterRequest createFromParcel(Parcel source) {
            return new RegisterRequest(source);
        }

        @Override
        public RegisterRequest[] newArray(int size) {
            return new RegisterRequest[size];
        }
    };

}
