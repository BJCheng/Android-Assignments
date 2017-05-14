package edu.stevens.cs522.chat.providers;

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
import android.util.Log;

import edu.stevens.cs522.chat.contracts.BaseContract;
import edu.stevens.cs522.chat.contracts.ChatroomContract;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.contracts.PeerContract;

public class ChatProvider extends ContentProvider {

    public ChatProvider() {
    }

    private static final String AUTHORITY = BaseContract.AUTHORITY;
    private static final String MESSAGE_CONTENT_PATH = MessageContract.CONTENT_PATH;
    private static final String MESSAGE_CONTENT_PATH_ITEM = MessageContract.CONTENT_PATH_ITEM;
    private static final String MESSAGE_CONTENT_PATH_SYNC = MessageContract.CONTENT_PATH_SYNC;
    private static final String PEER_CONTENT_PATH = PeerContract.CONTENT_PATH;
    private static final String PEER_CONTENT_PATH_ITEM = PeerContract.CONTENT_PATH_ITEM;
    private static final String CHATROOM_CONTENT_PATH = ChatroomContract.CONTENT_PATH;

    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CHATROOMS_TABLE = "chatrooms";
    private static final String MESSAGES_TABLE = "messages";
    private static final String PEERS_TABLE = "view_peers";
    private static final String CHATROOM_NAME_INDEX = "chatroom_name_index";

    // Create the constants used to differentiate between the different URI  requests.
    private static final int MESSAGES_ALL_ROWS = 1;
    private static final int MESSAGES_SINGLE_ROW = 2;
    private static final int MESSAGES_SYNC = 3;
    private static final int PEERS_ALL_ROWS = 4;
    private static final int PEERS_SINGLE_ROW = 5;
    private static final int CHATROOMS_ALL_ROWS = 6;

    public static class DbHelper extends SQLiteOpenHelper {

        public final String peerCreateString = "CREATE TABLE view_peers(" +
                "_id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "timestamp TEXT, " +
                "longitude REAL, " +
                "latitude REAL" +
                ");";
        public final String messageCreateString = "CREATE TABLE messages(" +
                "_id INTEGER PRIMARY KEY, " +
                "message_text TEXT, " +
                "chat_room TEXT, " +
                "timestamp TEXT, " +
                "sender text, " +
                "peer_fk INTEGER NOT NULL, " +
                "longitude REAL, " +
                "latitude REAL, " +
                "sequence_number INTEGER, " +
                "FOREIGN KEY(peer_fk) REFERENCES view_peers(_id) ON DELETE CASCADE, " +
                "FOREIGN KEY(chat_room) REFERENCES chatrooms(name) ON DELETE CASCADE" +
                ");";

        public DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + CHATROOMS_TABLE + " ("
                    + ChatroomContract.ID + " INTEGER PRIMARY KEY,"
                    + ChatroomContract.NAME + " TEXT NOT NULL"
                    + ");");
            ContentValues values = new ContentValues();
            values.put(ChatroomContract.NAME, "_default");
            db.insert(CHATROOMS_TABLE, null, values);
            // TODO other chatroom names

            // TODO initialize other database tables
            db.execSQL(peerCreateString);
            db.execSQL(messageCreateString);  //看chatroom外鍵那邊會不會出錯
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO upgrade database if necessary
            db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PEERS_TABLE);
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
        uriMatcher.addURI(AUTHORITY, MESSAGE_CONTENT_PATH_SYNC, MESSAGES_SYNC);
        uriMatcher.addURI(AUTHORITY, PEER_CONTENT_PATH, PEERS_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PEER_CONTENT_PATH_ITEM, PEERS_SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, CHATROOM_CONTENT_PATH, CHATROOMS_ALL_ROWS);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new message.
                // Make sure to notify any observers
                id = db.insertOrThrow(MESSAGES_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case PEERS_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new peer.
                // Make sure to notify any observers
                id = db.insertOrThrow(PEERS_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CHATROOMS_ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new chatroom.
                // Make sure to notify any observers
                id = db.insertOrThrow(CHATROOMS_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case MESSAGES_SINGLE_ROW:
                throw new IllegalArgumentException("insert expects a whole-table URI");
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
                Cursor cursor = db.query(MESSAGES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case PEERS_ALL_ROWS:
                // TODO: Implement this to handle query of all peers.
                Cursor peerCursor = db.query(PEERS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                peerCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return peerCursor;
            case CHATROOMS_ALL_ROWS:
                // TODO: Implement this to handle query of all chatrooms.
                Cursor chatRoomCursor = db.query(CHATROOMS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                chatRoomCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return chatRoomCursor;
            case MESSAGES_SINGLE_ROW:
                // TODO: Implement this to handle query of a specific message.
                throw new UnsupportedOperationException("Not yet implemented");
            case PEERS_SINGLE_ROW:
                // TODO: Implement this to handle query of a specific peer.
                throw new UnsupportedOperationException("Not yet implemented");
            default:
                throw new IllegalStateException("insert: bad case");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriMatcher.match(uri)){
            case MESSAGES_ALL_ROWS:
                rowsDeleted = db.delete(MESSAGES_TABLE, null, null);
                break;
            case PEERS_ALL_ROWS:
                rowsDeleted = db.delete(PEERS_TABLE, null, null);
                break;
        }

        return rowsDeleted;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] records) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int insertNums = 0;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_SYNC:
                /*
                 * Do all of this in a single transaction.
                 */
                db.beginTransaction();
                try {

                    /*
                     * Delete the first N messages with sequence number = 0, where N = records.length.
                     */
                    int numReplacedMessages = Integer.parseInt(uri.getLastPathSegment());

                    String[] columns = {MessageContract.ID};
                    String selection = MessageContract.SEQUENCE_NUMBER + "=0";
                    Cursor cursor = db.query(MESSAGES_TABLE, columns, selection, null, null, null, MessageContract.TIMESTAMP);
                    try {
                        if (numReplacedMessages > 0 && cursor.moveToFirst()) {
                            do {
                                String deleteSelection = MessageContract.ID + "=" + Long.toString(cursor.getLong(0));
                                int result = db.delete(MESSAGES_TABLE, deleteSelection, null);
                                Log.v("Chat Provider delete", String.valueOf(result));
                                numReplacedMessages--;
                            } while (numReplacedMessages > 0 && cursor.moveToNext());
                        }
                    } finally {
                        cursor.close();
                    }

                    /*
                     * Insert the messages downloaded from server, which will include replacements for deleted records.
                     */
                    for (ContentValues record : records) {
                        try {
                            db.insertOrThrow(MESSAGES_TABLE, null, record);
                            insertNums++;
                        } catch (Exception e) {
                            Log.e("Chat Provide Error", e.getMessage());
                        }
                    }

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                // TODO Make sure to notify any observers
                getContext().getContentResolver().notifyChange(uri, null);
                return insertNums;
            default:
                throw new IllegalStateException("insert: bad case");
        }
    }


}
