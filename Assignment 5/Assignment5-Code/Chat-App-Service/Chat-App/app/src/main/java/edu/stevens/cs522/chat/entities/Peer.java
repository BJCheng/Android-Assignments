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
    public Date timestamp;  // Last time we heard from this peer.
    public InetAddress address;
    public int port;

    public Peer() {
    }

    // TODO add operations for parcels (Parcelable), cursors and contentvalues

    public Peer(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.timestamp = (Date) in.readSerializable();
        this.address = (InetAddress) in.readSerializable();
        this.port = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.id);
        out.writeString(this.name);
        out.writeSerializable(this.timestamp);
        out.writeSerializable(this.address);
        out.writeInt(this.port);
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

    public Peer(Cursor cursor) {
        // TODO
        this.id = PeerContract.getId(cursor);
        this.name = PeerContract.getName(cursor);
        this.timestamp = PeerContract.getTimeStamp(cursor);
        this.address = PeerContract.getAddress(cursor);
        this.port = PeerContract.getPort(cursor);
    }

    public void writeToProvider(ContentValues values) {
        PeerContract.putName(values, this.name);
        PeerContract.putTimeStamp(values, this.timestamp);
        PeerContract.putAddress(values, this.address);
        PeerContract.putPort(values, this.port);
    }
}
