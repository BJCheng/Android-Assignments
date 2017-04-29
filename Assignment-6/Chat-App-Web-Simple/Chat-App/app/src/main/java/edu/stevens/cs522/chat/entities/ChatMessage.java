package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.stevens.cs522.chat.contracts.MessageContract;

/**
 * Created by dduggan.
 */

public class ChatMessage {

    public long id;
    public String messageText;
    public Date timestamp;
    public Double longitude;
    public Double latitude;
    public String sender;
    public long senderId;
    public int messageSequenceNumber;

    public ChatMessage() {
        longitude = 0.0;
        latitude = 0.0;
        messageSequenceNumber = 0;
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues
    public ChatMessage(Parcel in) {
        this.id = in.readLong();
        this.messageText = in.readString();
        this.timestamp = (Date) in.readSerializable();
        this.sender = in.readString();
        this.senderId = in.readLong();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.messageSequenceNumber = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.id);
        out.writeString(this.messageText);
        out.writeSerializable(this.timestamp);
        out.writeString(this.sender);
        out.writeLong(this.senderId);
        out.writeDouble(this.longitude);
        out.writeDouble(this.latitude);
        out.writeInt(this.messageSequenceNumber);
    }

    public static final Parcelable.Creator<ChatMessage> CREATOR = new Parcelable.Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public ChatMessage(Cursor cursor) {
        // TODO
        SimpleDateFormat format = new SimpleDateFormat();
        this.messageText = MessageContract.getMessageText(cursor);
        this.timestamp = MessageContract.getTimeStamp(cursor);
        this.sender = MessageContract.getSender(cursor);
        this.senderId = MessageContract.getSenderId(cursor);
        this.longitude = MessageContract.getLongitude(cursor);
        this.latitude = MessageContract.getLatitude(cursor);
        this.messageSequenceNumber = MessageContract.getMessageSequenceNumber(cursor);
    }

    public void writeToProvider(ContentValues values) {
        MessageContract.putMessageText(values, this.messageText);
        MessageContract.putTimestamp(values, this.timestamp);
        MessageContract.putSender(values, this.sender);
        MessageContract.putSenderId(values, this.senderId);
        MessageContract.putLongitude(values, this.longitude);
        MessageContract.putLatitude(values, this.latitude);
        MessageContract.putMessageSequenceNumber(values, this.messageSequenceNumber);
    }

}
