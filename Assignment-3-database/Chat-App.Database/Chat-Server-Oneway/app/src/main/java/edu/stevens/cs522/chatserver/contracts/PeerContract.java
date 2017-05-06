package edu.stevens.cs522.chatserver.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.stevens.cs522.chatserver.util.InetAddressUtils;

/**
 * Created by dduggan.
 */

public class PeerContract implements BaseColumns {

    public static final String NAME = "name";
    public static final String TIME_STAMP = "time_stamp";
    public static final String ADDRESS = "address";
    public static final String PORT = "port";

    // TODO define column names, getters for cursors, setters for contentvalues

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
        return new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TIME_STAMP)));
    }

    public static void putTimeStamp(ContentValues values, Date timeStamp) {
        values.put(TIME_STAMP, timeStamp.getTime());
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
