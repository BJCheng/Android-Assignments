package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.databases.MessagesDbAdapter;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_ID_KEY = "peer_id";
    private TextView userNameTextView;
    private TextView timeStampTextView;
    private TextView addressTextView;
    private TextView portTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        userNameTextView = (TextView)findViewById(R.id.view_user_name);
        timeStampTextView = (TextView)findViewById(R.id.view_timestamp);
        addressTextView = (TextView)findViewById(R.id.view_address);
        portTextView = (TextView)findViewById(R.id.view_port);

        long peerId = getIntent().getLongExtra(PEER_ID_KEY, 0);
        MessagesDbAdapter dbAdapter = new MessagesDbAdapter(this);
        dbAdapter.open();
        Peer peer = dbAdapter.fetchPeer(peerId);

        userNameTextView.setText(peer.name);
        timeStampTextView.setText(peer.timestamp.toString());
        addressTextView.setText(peer.address.getHostAddress());
        portTextView.setText(String.valueOf(peer.port));
    }

}
