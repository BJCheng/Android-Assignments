package edu.stevens.cs522.chatserver.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.stevens.cs522.chatserver.contracts.MessageContract;

/**
 * Created by dduggan.
 */

public class Message implements Parcelable {

    public long id;
    public String messageText;
    public Date timestamp;
    public String sender;
    public long senderId;

    public Message(){}

    // TODO add operations for parcels (Parcelable), cursors and contentvalues

    public Message(Parcel in) {
        this.id = in.readLong();
        this.messageText = in.readString();
        this.timestamp = (Date) in.readSerializable();
        this.sender = in.readString();
        this.senderId = in.readLong();
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
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public Message(Cursor cursor) {
        SimpleDateFormat format = new SimpleDateFormat();
        this.messageText = MessageContract.getMessageText(cursor);
        this.timestamp = MessageContract.getTimeStamp(cursor);
        this.sender = MessageContract.getSender(cursor);
        this.senderId = MessageContract.getSenderId(cursor);
    }

    public void writeToProvider(ContentValues values) {
        MessageContract.putMessageText(values, this.messageText);
        MessageContract.putTimestamp(values, this.timestamp);
        MessageContract.putSender(values, this.sender);
        MessageContract.putSenderId(values, this.senderId);
    }

}
