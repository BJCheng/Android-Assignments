package edu.stevens.cs522.chat.managers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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

    private AsyncContentResolver contentResolver;
    final static Uri CONTENT_URI = PeerContract.CONTENT_URI;
    final static Uri CONTENT_URI_SINGLE = PeerContract.CONTENT_URI(0);

    private static final IEntityCreator<Peer> creator = new IEntityCreator<Peer>() {
        @Override
        public Peer create(Cursor cursor) {
            return new Peer(cursor);
        }
    };

    public PeerManager(Context context) {
        super(context, creator, LOADER_ID);
        contentResolver = getAsyncResolver();
    }

    public void getAllPeersAsync(IQueryListener<Peer> listener) {
        // TODO use QueryBuilder to complete this
    }

    public void getPeerAsync(long id, IContinue<Peer> callback) {
        // TODO need to check that peer is not null (not in database)
    }

    public void persistAsync(Peer peer, IContinue<Long> callback) {
        // TODO need to ensure the peer is not already in the database
    }

}
