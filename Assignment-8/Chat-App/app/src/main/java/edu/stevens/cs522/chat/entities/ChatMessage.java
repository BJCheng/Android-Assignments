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

public class ChatMessage implements Parcelable {

    public long id;
    public long seqNum;
    public String messageText;
    public String chatRoom;
    public Date timestamp;
    public Double longitude;
    public Double latitude;
    public String sender;  // Sender username and FK (in local database)
    public long senderId;

    public ChatMessage() {
    }

    public ChatMessage(Cursor cursor) {
        // TODO
        SimpleDateFormat format = new SimpleDateFormat();
        this.messageText = MessageContract.getMessageText(cursor);
        this.chatRoom = MessageContract.getChatRoom(cursor);
        this.timestamp = MessageContract.getTimeStamp(cursor);
        this.sender = MessageContract.getSender(cursor);
//        this.senderId = MessageContract.getSenderId(cursor);
        this.longitude = MessageContract.getLongitude(cursor);
        this.latitude = MessageContract.getLatitude(cursor);
        this.seqNum = MessageContract.getSeqNum(cursor);
    }

    public void writeToProvider(ContentValues values) {
        // TODO
        MessageContract.putMessageText(values, this.messageText);
        MessageContract.putChatRoom(values, this.chatRoom);
        MessageContract.putTimestamp(values, this.timestamp);
        MessageContract.putSender(values, this.sender);
        MessageContract.putSenderId(values, this.senderId);
        MessageContract.putLongitude(values, this.longitude);
        MessageContract.putLatitude(values, this.latitude);
        MessageContract.putSeqNum(values, this.seqNum);
    }


    // TODO add operations for parcels (Parcelable), cursors and contentvalues
    public ChatMessage(Parcel in) {
        this.id = in.readLong();
        this.messageText = in.readString();
        this.chatRoom = in.readString();
        this.timestamp = (Date) in.readSerializable();
        this.sender = in.readString();
//        this.senderId = in.readLong();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.seqNum = in.readLong();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.id);
        out.writeString(this.messageText);
        out.writeString(this.chatRoom);
        out.writeSerializable(this.timestamp);
        out.writeString(this.sender);
//        out.writeLong(this.senderId);
        out.writeDouble(this.longitude);
        out.writeDouble(this.latitude);
        out.writeLong(this.seqNum);
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

}
