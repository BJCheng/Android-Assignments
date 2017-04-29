package edu.stevens.cs522.chat.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.chat.util.InetAddressUtils;

/**
 * Created by dduggan.
 */

public class PeerContract extends BaseContract {

    public static final Uri CONTENT_URI = CONTENT_URI(AUTHORITY, "Peer");

    public static final Uri CONTENT_URI(long id) {
        return CONTENT_URI(Long.toString(id));
    }

    public static final Uri CONTENT_URI(String id) {
        return withExtendedPath(CONTENT_URI, id);
    }

    public static final String CONTENT_PATH = CONTENT_PATH(CONTENT_URI);

    public static final String CONTENT_PATH_ITEM = CONTENT_PATH(CONTENT_URI("#"));


    // TODO define column names, getters for cursors, setters for contentvalues

    public static final String NAME = "name";
    public static final String TIMESTAMP = "timestamp";
    public static final String ADDRESS = "address";
    public static final String PORT = "port";


    public static int getId(Cursor cursor){
        return cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
    }

    public static String getName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(NAME));
    }

    public static void putName(ContentValues values, String name) {
        values.put(NAME, name);
    }

    public static Date getTimeStamp(Cursor cursor) {
        return new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TIMESTAMP)));
    }

    public static void putTimeStamp(ContentValues values, Date timeStamp) {
        values.put(TIMESTAMP, timeStamp.getTime());
    }

    public static InetAddress getAddress(Cursor cursor) {
        return InetAddressUtils.getAddress(cursor, cursor.getColumnIndexOrThrow(ADDRESS));
    }

    public static void putAddress(ContentValues values, InetAddress address) {
        InetAddressUtils.putAddress(values, ADDRESS, address);
    }

    public static int getPort(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(PORT));
    }

    public static void putPort(ContentValues values, int port) {
        values.put(PORT, port);
    }

}
