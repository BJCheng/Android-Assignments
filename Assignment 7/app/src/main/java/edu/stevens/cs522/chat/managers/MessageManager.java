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
            return new ChatMessage(cursor, true);
        }
    };

    final static Uri CONTENT_URI = MessageContract.CONTENT_URI;
    private AsyncContentResolver contentResolver;

    public MessageManager(Context context) {
        super(context, creator, LOADER_ID);
        contentResolver = getAsyncResolver();
    }

    public void getAllMessagesAsync(IQueryListener<ChatMessage> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(CONTENT_URI, listener);
    }

    public void persistAsync(ChatMessage Message, IContinue<Long> callback) {
        // TODO
        ContentValues values = new ContentValues();
        Message.writeToProvider(values);
        contentResolver.insertAsync(CONTENT_URI, values, callback);
    }

    public void updateAsync(int messageSequenceNumber, String selection, String[] args, IContinue<Long> callback){
        ContentValues values = new ContentValues();
        MessageContract.putSeqNum(values, messageSequenceNumber);
        contentResolver.updateAsync(CONTENT_URI, values, selection, args, callback);
    }

}
