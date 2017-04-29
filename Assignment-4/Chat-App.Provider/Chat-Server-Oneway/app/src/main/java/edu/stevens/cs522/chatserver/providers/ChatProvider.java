package edu.stevens.cs522.chatserver.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import edu.stevens.cs522.chatserver.contracts.BaseContract;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;

public class ChatProvider extends ContentProvider {

    public ChatProvider() {
    }

    private static final String AUTHORITY = BaseContract.AUTHORITY;

    private static final String MESSAGE_CONTENT_PATH = MessageContract.CONTENT_PATH;

    private static final String MESSAGE_CONTENT_PATH_ITEM = MessageContract.CONTENT_PATH_ITEM;

    private static final String PEER_CONTENT_PATH = PeerContract.CONTENT_PATH;

    private static final String PEER_CONTENT_PATH_ITEM = PeerContract.CONTENT_PATH_ITEM;


    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;
    private static final String MESSAGES_TABLE = "messages";
    private static final String PEERS_TABLE = "view_peers";

    // Create the constants used to differentiate between the different URI  requests.
    private static final int MESSAGES_ALL_ROWS = 1;
    private static final int MESSAGES_SINGLE_ROW = 2;
    private static final int PEERS_ALL_ROWS = 3;
    private static final int PEERS_SINGLE_ROW = 4;

    public static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context, String name, CursorFactory factory, int version) {
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
            // TODO initialize database tables
            db.execSQL(peerCreateString);
            db.execSQL(messageCreateString);
            db.execSQL(peerIndex);
            db.execSQL(messageIndex);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO upgrade database if necessary
            db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PEERS_TABLE);
            db.execSQL(peerCreateString);
            db.execSQL(messageCreateString);
        }
    }

    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        // Initialize your content provider on startup.
        dbHelper = new DbHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return true;
    }

    // Used to dispatch operation based on URI
    private static final UriMatcher uriMatcher;

    // uriMatcher.addURI(AUTHORITY, CONTENT_PATH, OPCODE)
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MESSAGE_CONTENT_PATH, MESSAGES_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, MESSAGE_CONTENT_PATH_ITEM, MESSAGES_SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, PEER_CONTENT_PATH, PEERS_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PEER_CONTENT_PATH_ITEM, PEERS_SINGLE_ROW);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                return "vnd.android.cursor.dir/vnd." + MessageContract.AUTHORITY + "." + MESSAGES_TABLE;
            case PEERS_ALL_ROWS:
                return "vnd.android.cursor.item/vnd." + PeerContract.AUTHORITY + "." + PEERS_TABLE;
            case MESSAGES_SINGLE_ROW:
                return "vnd.android.cursor.dir/vnd." + MessageContract.AUTHORITY + "." + MESSAGES_TABLE;
            case PEERS_SINGLE_ROW:
                return "vnd.android.cursor.item/vnd." + PeerContract.AUTHORITY + "." + PEERS_TABLE;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new message.
                // Make sure to notify any observers
                long messageId = db.insert(MESSAGES_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, messageId);
            case PEERS_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new peer.
                // Make sure to notify any observers
                long id = db.insert(PEERS_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case MESSAGES_SINGLE_ROW:
                long messageSingleId = db.insert(MESSAGES_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, messageSingleId);
            default:
                throw new IllegalStateException("insert: bad case");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                // TODO: Implement this to handle query of all messages.
                Cursor cursor1 = db.query(MESSAGES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor1.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor1;
            case PEERS_ALL_ROWS:
                // TODO: Implement this to handle query of all peers.
                Cursor cursor2 = db.query(PEERS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor2.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor2;
            case MESSAGES_SINGLE_ROW:
                // TODO: Implement this to handle query of a specific message.
                Cursor cursor3 = db.query(MESSAGES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor3.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor3;
            case PEERS_SINGLE_ROW:
                // TODO: Implement this to handle query of a specific peer.
                Cursor cursor4 = db.query(PEERS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor4.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor4;
            default:
                throw new IllegalStateException("insert: bad case");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to update one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = -1;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                throw new IllegalStateException("Update of messages not supported");
            case PEERS_ALL_ROWS:
                throw new IllegalStateException("Update of peers not supported");
            case MESSAGES_SINGLE_ROW:
                rowsUpdated = db.update(MESSAGES_TABLE, values, selection, selectionArgs);
            case PEERS_SINGLE_ROW:
                rowsUpdated = db.update(PEERS_TABLE, values, selection, selectionArgs);
            default:
                //throw new IllegalStateException("insert: bad case");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to delete one or more rows.SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = -1;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                rowsDeleted = db.delete(MESSAGES_TABLE, null, null);
            case PEERS_ALL_ROWS:
                rowsDeleted = db.delete(PEERS_TABLE, null, null);
            case MESSAGES_SINGLE_ROW:
                rowsDeleted = db.delete(MESSAGES_TABLE, selection, selectionArgs);
            case PEERS_SINGLE_ROW:
                rowsDeleted = db.delete(PEERS_TABLE, selection, selectionArgs);
            default:
                //throw new IllegalStateException("insert: bad case");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

}
