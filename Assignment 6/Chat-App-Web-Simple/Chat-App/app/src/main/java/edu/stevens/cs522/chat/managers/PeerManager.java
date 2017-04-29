package edu.stevens.cs522.chat.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

import edu.stevens.cs522.chat.async.AsyncContentResolver;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.IEntityCreator;
import edu.stevens.cs522.chat.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.entities.Peer;


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

    private AsyncContentResolver contentResolver;
    final static Uri CONTENT_URI = PeerContract.CONTENT_URI;
    final static Uri CONTENT_URI_SINGLE = PeerContract.CONTENT_URI(0);

    public PeerManager(Context context) {
        super(context, creator, LOADER_ID);
        contentResolver = getAsyncResolver();
//        contentResolver = new AsyncContentResolver(context.getContentResolver());
    }

    public void getAllPeersAsync(IQueryListener<Peer> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(CONTENT_URI, listener);
    }

    //    public void getPeerAsync(String name, IContinue<Peer> callback) {
    public void getPeerAsync(String name, IContinue<Cursor> callback) {
        // TODO need to check that peer is not null (not in database)
        contentResolver.queryAsync(CONTENT_URI, null, PeerContract.NAME + "=?", new String[]{name}, null, callback);
    }

    public void persistAsync(final Peer peer, final IContinue<Long> callback) {
        ContentValues values = new ContentValues();
        peer.writeToProvider(values);
        contentResolver.insertAsync(CONTENT_URI, values, callback);
    }

    public void updateAsync(Date timestamp, String selection, String[] args, IContinue<Long> callback) {
        ContentValues values = new ContentValues();
        PeerContract.putTimeStamp(values, timestamp);
        contentResolver.updateAsync(CONTENT_URI, values, selection, args, callback);
    }

}
