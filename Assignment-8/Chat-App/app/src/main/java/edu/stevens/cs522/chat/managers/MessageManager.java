package edu.stevens.cs522.chat.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import edu.stevens.cs522.chat.async.AsyncContentResolver;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.chat.contracts.ChatroomContract;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.ChatRoom;


/**
 * Created by dduggan.
 */

public class MessageManager extends Manager<ChatMessage> {

    private static final int LOADER_ID = 1;

    private static final IEntityCreator<ChatMessage> creator = new IEntityCreator<ChatMessage>() {
        @Override
        public ChatMessage create(Cursor cursor) {
            return new ChatMessage(cursor);
        }
    };

    final static Uri CONTENT_URI = MessageContract.CONTENT_URI;
    private AsyncContentResolver contentResolver;

    public MessageManager(Context context) {
        super(context, creator, LOADER_ID);
    }

    public void getAllMessagesAsync(ChatRoom chatroom, IQueryListener<ChatMessage> listener) {
        // TODO use QueryBuilder to complete this
//        getAsyncResolver().queryAsync(CONTENT_URI, null, MessageContract.CHAT_ROOM+"=?",
//                new String[]{chatroom.name}, null, listener);
        executeQuery(CONTENT_URI, listener, chatroom.name);
    }

    public void getAllMessagesSync(ChatRoom chatroom){
        getSyncResolver().query(CONTENT_URI, null, MessageContract.CHAT_ROOM + "=?", new String[]{chatroom.name}, null);
    }

    public void persistAsync(ChatMessage Message, IContinue<Uri> callback) {
        // TODO
        ContentValues values = new ContentValues();
        Message.writeToProvider(values);
        contentResolver.insertAsync(CONTENT_URI, values, callback);
    }

}
