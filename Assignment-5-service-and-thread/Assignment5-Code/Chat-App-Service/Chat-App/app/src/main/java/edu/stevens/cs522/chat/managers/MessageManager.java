package edu.stevens.cs522.chat.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import edu.stevens.cs522.chat.async.AsyncContentResolver;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;


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

    public MessageManager(Context context) {
        super(context, creator, LOADER_ID);
//        asyncResolver = new AsyncContentResolver(context.getContentResolver());
    }

    public void getAllMessagesAsync(IQueryListener<ChatMessage> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(CONTENT_URI, listener);
    }

    public void persistAsync(ChatMessage message, IContinue<Long> callback) {
        // TODO
        ContentValues values = new ContentValues();
        message.writeToProvider(values);
//        asyncResolver.insertAsync(CONTENT_URI, values, new IContinue<Uri>() {
        getAsyncResolver().insertAsync(CONTENT_URI, values, callback);
    }

}
