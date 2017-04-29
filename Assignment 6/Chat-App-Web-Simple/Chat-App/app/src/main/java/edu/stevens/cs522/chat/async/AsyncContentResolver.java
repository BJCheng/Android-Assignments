package edu.stevens.cs522.chat.async;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

/**
 * Created by dduggan.
 */

public class AsyncContentResolver extends AsyncQueryHandler {

    public AsyncContentResolver(ContentResolver cr) {
        super(cr);
    }

    public void insertAsync(Uri uri, ContentValues values, IContinue<Long> callback) {
        this.startInsert(0, callback, uri, values);
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
        if (cookie != null) {
            @SuppressWarnings("unchecked")
            IContinue<Long> callback = (IContinue<Long>) cookie;
            callback.kontinue(ContentUris.parseId(uri));
        }
    }

    public void queryAsync(Uri uri, String[] columns, String select, String[] selectArgs, String order, IContinue<Cursor> callback) {
        // TODO
        this.startQuery(0, callback, uri, columns, select, selectArgs, order);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        // TODO
        if(cookie != null){
            IContinue<Cursor> callback = (IContinue<Cursor>) cookie;
            ((IContinue<Cursor>) cookie).kontinue(cursor);
        }
    }

    public void deleteAsync(Uri uri, String select, String[] selectArgs) {
        // TODO
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        super.onDeleteComplete(token, cookie, result);
        // TODO
    }

    public void updateAsync(Uri uri, ContentValues values, String selection, String[] selectionArgs, IContinue<Long> callback) {
        // TODO
        this.startUpdate(0, callback, uri, values, selection, selectionArgs);
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        super.onUpdateComplete(token, cookie, result);
        // TODO
        if(cookie != null){
            IContinue<Long> callback = (IContinue<Long>) cookie;
            callback.kontinue(null);
        }
    }

}