package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_KEY = "peer";
    TextView view_user_name;
    TextView view_timestamp;
    TextView view_address;
    TextView view_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        Peer peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }

        // TODO init the UI
        view_user_name = (TextView) findViewById(R.id.view_user_name);
        view_timestamp = (TextView) findViewById(R.id.view_timestamp);
        view_address = (TextView) findViewById(R.id.view_address);
        view_port = (TextView) findViewById(R.id.view_port);

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestampString = formatter.format(peer.timestamp);

        view_user_name.setText(peer.name);
        view_timestamp.setText(timestampString);
        view_address.setText(peer.address.getHostAddress());
        view_port.setText(String.valueOf(peer.port));
    }

}
