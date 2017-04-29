package edu.stevens.cs522.chatserver.managers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Set;

import edu.stevens.cs522.chatserver.async.AsyncContentResolver;
import edu.stevens.cs522.chatserver.async.IContinue;
import edu.stevens.cs522.chatserver.async.IEntityCreator;
import edu.stevens.cs522.chatserver.async.QueryBuilder;
import edu.stevens.cs522.chatserver.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;


/**
 * Created by dduggan.
 */

public class MessageManager extends Manager<Message> {

    private static final int LOADER_ID = 1;

    private static final IEntityCreator<Message> creator = new IEntityCreator<Message>() {
        @Override
        public Message create(Cursor cursor) {
            return new Message(cursor);
        }
    };

    private AsyncContentResolver asyncResolver;
    final static Uri CONTENT_URI = MessageContract.CONTENT_URI;

    public MessageManager(Context context) {
        super(context, creator, LOADER_ID);
        asyncResolver = new AsyncContentResolver(context.getContentResolver());
    }

    public void getAllMessagesAsync(IQueryListener<Message> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(CONTENT_URI, listener);
    }

    public void persistAsync(final Message message, IContinue<Long> callback) {
        // TODO
        ContentValues values = new ContentValues();
        message.writeToProvider(values);
//        asyncResolver.insertAsync(CONTENT_URI, values, new IContinue<Uri>() {
        asyncResolver.insertAsync(CONTENT_URI, values, callback);
    }

}
