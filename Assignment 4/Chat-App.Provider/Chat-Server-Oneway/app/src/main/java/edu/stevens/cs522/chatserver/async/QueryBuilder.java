package edu.stevens.cs522.chatserver.async;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import edu.stevens.cs522.chatserver.managers.TypedCursor;

/**
 * Created by dduggan.
 */

public class QueryBuilder<T> implements LoaderManager.LoaderCallbacks {

    private int loaderID;
    private IEntityCreator<T> creator;
    private IQueryListener<T> listener;
    private Context context;
    private Uri uri;

    public static interface IQueryListener<T> {

        public void handleResults(TypedCursor<T> results);

        public void closeResults();

    }

    private QueryBuilder(String tag, Context context, Uri uri, int loaderID, IEntityCreator<T> creator, IQueryListener<T> listener) {
        this.loaderID = loaderID;
        this.creator = creator;
        this.listener = listener;
        this.context = context;
        this.uri = uri;
    }

    public static <T> void executeQuery(String tag, Activity context, Uri uri, int loaderID, IEntityCreator<T> creator, IQueryListener<T> listener) {
        QueryBuilder<T> qb = new QueryBuilder<T>(tag, context, uri, loaderID, creator, listener);
        LoaderManager lm = context.getLoaderManager();
        lm.initLoader(loaderID, null, qb);
    }

    // TODO complete the implementation of this

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == loaderID) {
            return new CursorLoader(context, uri, null, null, null, null);
        }
        else{
            throw new IllegalStateException("Unexpected loader callback");
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader.getId() == loaderID) {
            listener.handleResults(new TypedCursor<T>((Cursor)data, creator));
        }
        else{
            throw new IllegalStateException("Unexpected loader callback");
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        if (loader.getId() == loaderID) {
            listener.closeResults();
        } else {
            throw new IllegalStateException("Unexpected loader callback");
        }
    }
}
