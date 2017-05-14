package edu.stevens.cs522.chat.async;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.managers.TypedCursor;

/**
 * Created by dduggan.
 */

public class QueryBuilder<T> implements LoaderManager.LoaderCallbacks {

    public static interface IQueryListener<T> {

        public void handleResults(TypedCursor<T> results);

        public void closeResults();

    }

    // TODO complete the implementation of this

    private IEntityCreator<T> creator;
    private IQueryListener<T> listener;
    private Context context;
    private Uri uri;
    private static final String CHATROOM_KEY = "CHATROOM_KEY";

    private QueryBuilder(String tag, Context context, Uri uri, IEntityCreator<T> creator, IQueryListener<T> listener) {
        this.creator = creator;
        this.listener = listener;
        this.context = context;
        this.uri = uri;
    }

    public static <T> void executeQuery(String tag, Activity context, Uri uri, int loaderID,
                                        IEntityCreator<T> creator, IQueryListener<T> listener, String chatRoomName) {
        QueryBuilder<T> qb = new QueryBuilder<T>(tag, context, uri, creator, listener);
        LoaderManager lm = context.getLoaderManager();
        if (chatRoomName != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CHATROOM_KEY, chatRoomName);
            if(lm.getLoader(1) != null){
                lm.destroyLoader(1); //
                lm.initLoader(loaderID, bundle, qb);
            } else
                lm.initLoader(loaderID, bundle, qb);
        } else{
            lm.initLoader(loaderID, null, qb);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if(args!=null){
            String chatRoomName = args.getString(CHATROOM_KEY);
            return new CursorLoader(context, uri, null, MessageContract.CHAT_ROOM+"=?", new String[]{chatRoomName}, null);
        } else
            return new CursorLoader(context, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        listener.handleResults(new TypedCursor<T>((Cursor) data, creator));
    }

    @Override
    public void onLoaderReset(Loader loader) {
        listener.closeResults();
    }
}
