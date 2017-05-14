package edu.stevens.cs522.chat.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.net.URI;

import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.contracts.ChatroomContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.ChatRoom;
import edu.stevens.cs522.chat.async.QueryBuilder.IQueryListener;

/**
 * Created by dduggan.
 */

public class ChatroomManager extends Manager<ChatRoom> {

    private static final int LOADER_ID = 3;

    private static final IEntityCreator<ChatRoom> creator = new IEntityCreator<ChatRoom>() {
        @Override
        public ChatRoom create(Cursor cursor) {
            return new ChatRoom(cursor);
        }
    };

    private Uri contentUri = ChatroomContract.CONTENT_URI;

    public ChatroomManager(Context context) {
        super(context, creator, LOADER_ID);
    }

    public void getAllChatroomsAsync(IQueryListener<ChatRoom> listener) {
        executeQuery(ChatroomContract.CONTENT_URI, listener, null);
    }

    public void persistAsync(String chatRoomName, IContinue<Uri> callback){
        ContentValues values = new ContentValues();
        ChatroomContract.putName(values, chatRoomName);
        getAsyncResolver().insertAsync(contentUri, values, callback);
    }


}
