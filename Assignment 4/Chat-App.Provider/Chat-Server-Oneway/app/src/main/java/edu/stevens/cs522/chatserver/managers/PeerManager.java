package edu.stevens.cs522.chatserver.managers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.CursorAdapter;

import edu.stevens.cs522.chatserver.async.AsyncContentResolver;
import edu.stevens.cs522.chatserver.async.IContinue;
import edu.stevens.cs522.chatserver.async.IEntityCreator;
import edu.stevens.cs522.chatserver.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.entities.Peer;


/**
 * Created by dduggan.
 */

public class PeerManager extends Manager<Peer> {

    private static final int LOADER_ID = 2;

    private static final IEntityCreator<Peer> creator = new IEntityCreator<Peer>() {
        @Override
        public Peer create(Cursor cursor) {
            return new Peer(cursor);
        }
    };

    private AsyncContentResolver asyncResolver;
    final static Uri CONTENT_URI = PeerContract.CONTENT_URI;

    public PeerManager(Context context) {
        super(context, creator, LOADER_ID);
        asyncResolver = new AsyncContentResolver(context.getContentResolver());
    }

    public void getAllPeersAsync(IQueryListener<Peer> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(CONTENT_URI, listener);
    }

//    public void getPeerAsync(long id, IContinue<Peer> callback) {
    public void getPeerAsync(long id, IContinue<Cursor> callback) {
        // TODO need to check that peer is not null (not in database)
        asyncResolver.queryAsync(CONTENT_URI, null, "_id=?", new String[]{String.valueOf(id)}, null, callback);
    }

    public void persistAsync(Peer peer, IContinue<Long> callback) {
        // TODO need to ensure the peer is not already in the database
        ContentValues values = new ContentValues();
        peer.writeToProvider(values);
        asyncResolver.insertAsync(CONTENT_URI, values, callback);
    }

}
