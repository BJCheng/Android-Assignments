package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.chat.contracts.PeerContract;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable {

    public long id;
    public String name;
    // Last time we heard from this peer.
    public Date timestamp;
    public Double longitude;
    public Double latitude;

    public Peer() {
        longitude = 0.0;
        latitude = 0.0;
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues

    public Peer(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.timestamp = (Date) in.readSerializable();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public Peer(Cursor cursor) {
        // TODO
        this.id = PeerContract.getId(cursor);
        this.name = PeerContract.getName(cursor);
        this.timestamp = PeerContract.getTimeStamp(cursor);
        this.longitude = PeerContract.getLongitude(cursor);
        this.latitude = PeerContract.getLatitude(cursor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeSerializable(this.timestamp);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }

    public void writeToProvider(ContentValues values){
        PeerContract.putName(values, this.name);
        PeerContract.putTimeStamp(values, this.timestamp);
        PeerContract.putLongitude(values, this.longitude);
        PeerContract.putLatitude(values, this.latitude);
    }

    public static final Parcelable.Creator<Peer> CREATOR = new Parcelable.Creator<Peer>() {
        @Override
        public Peer createFromParcel(Parcel in) {
            return new Peer(in);
        }

        public Peer[] newArray(int size) {
            return new Peer[size];
        }
    };
}
