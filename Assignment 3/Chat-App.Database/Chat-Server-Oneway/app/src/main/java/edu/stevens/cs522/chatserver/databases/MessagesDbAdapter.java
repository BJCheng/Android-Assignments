package edu.stevens.cs522.chatserver.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class MessagesDbAdapter {

    private static final String DATABASE_NAME = "messages.db";
    private static final String MESSAGE_TABLE = "messages";
    private static final String PEER_TABLE = "view_peers";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;


    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_CREATE = null; // TODO

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public final String peerCreateString = "CREATE TABLE view_peers(" +
                "_id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "time_stamp TEXT, " +
                "address TEXT, " +
                "port INTEGER" +
                ");";
        public final String messageCreateString = "CREATE TABLE messages(" +
                "_id INTEGER PRIMARY KEY, " +
                "message_text TEXT, " +
                "time_stamp TEXT, " +
                "sender text, " +
                "peer_fk INTEGER NOT NULL, " +
                "FOREIGN KEY(peer_fk) REFERENCES view_peers(_id) ON DELETE CASCADE" +
                ");";
        public final String peerIndex = "CREATE INDEX PeerNameIndex ON view_peers(name);";
        public final String messageIndex = "CREATE INDEX MessagesPeerIndex ON Messages(peer_fk);";

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO
            db.execSQL(peerCreateString);
            db.execSQL(messageCreateString);
            db.execSQL(peerIndex);
            db.execSQL(messageIndex);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
            db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PEER_TABLE);
            db.execSQL(peerCreateString);
            db.execSQL(messageCreateString);
        }
    }


    public MessagesDbAdapter(Context _context) {
        dbHelper = new DatabaseHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        // TODO
        db = dbHelper.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    public Cursor fetchAllMessages() {
        // TODO
        return db.query(MESSAGE_TABLE, new String[]{"_id", "message_text", "time_stamp", "sender"}, null, null, null, null, null);
    }

    public Cursor fetchAllPeers() {
        // TODO
        return db.query(PEER_TABLE, null, null, null, null, null, null);
    }

    public Peer fetchPeer(long peerId) throws CursorIndexOutOfBoundsException {
        // TODO
        Cursor cursor = db.query(PEER_TABLE, null, "_id=?", new String[]{String.valueOf(peerId)}, null, null, null);
        cursor.moveToFirst();
        Peer peer = new Peer(cursor);
        cursor.close();
        return peer;
    }

    public Cursor fetchMessagesFromPeer(Peer peer) {
        // TODO
        return null;
    }

    public void persist(Message message) throws SQLException {
        // TODO
        ContentValues values = new ContentValues();
        message.writeToProvider(values);
        long id = db.insert(MESSAGE_TABLE, null, values);
    }

    public long persist(Peer peer) throws SQLException {
        // TODO
        ContentValues values = new ContentValues();
        peer.writeToProvider(values);
        try {
            Peer peerFetched = this.fetchPeer(peer.name);
            db.update(PEER_TABLE, values, "_id=?", new String[]{String.valueOf(peerFetched.id)});
            return peerFetched.id;
        } catch (Exception e) {
            peer.id = db.insert(PEER_TABLE, null, values);
            return peer.id;
        }
    }

    public Peer fetchPeer(String peerName) throws CursorIndexOutOfBoundsException {
        // TODO
        Cursor cursor = db.query(PEER_TABLE, null, "name=?", new String[]{String.valueOf(peerName)}, null, null, null);
        cursor.moveToFirst();
        Peer peer = new Peer(cursor);
        cursor.close();
        return peer;
    }

    public void close() {
        // TODO
        db.close();
    }
}