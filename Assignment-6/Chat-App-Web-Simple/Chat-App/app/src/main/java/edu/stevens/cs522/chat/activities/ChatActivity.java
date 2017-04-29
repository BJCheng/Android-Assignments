/*********************************************************************

 Chat server: accept chat messages from clients.

 Sender chatName and GPS coordinates are encoded
 in the messages, and stripped off upon receipt.

 Copyright (c) 2017 Stevens Institute of Technology

 **********************************************************************/
package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.Peer;
import edu.stevens.cs522.chat.managers.MessageManager;
import edu.stevens.cs522.chat.managers.PeerManager;
import edu.stevens.cs522.chat.managers.TypedCursor;
import edu.stevens.cs522.chat.rest.ChatHelper;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.InetAddressUtils;
import edu.stevens.cs522.chat.util.ResultReceiverWrapper;
import edu.stevens.cs522.chat.util.ThreeTextviewCursorAdapter;

public class ChatActivity extends Activity implements OnClickListener, QueryBuilder.IQueryListener<ChatMessage>,
        ResultReceiverWrapper.IReceive {

    final static public String TAG = ChatActivity.class.getCanonicalName();

    /*
     * UI for displaying received messages
     */
    private ListView messageList;
    private SimpleCursorAdapter messagesAdapter;
    private ThreeTextviewCursorAdapter customAdapter;
    private MessageManager messageManager;
    private PeerManager peerManager;
    private ChatMessage message;

    /*
     * Widgets for dest address, message text, send button.
     */
    private EditText chatRoomName;
    private EditText messageTextEditText;
    private Button sendButton;
    private ChatHelper helper;  //Helper for Web service
    private ResultReceiverWrapper sendResultReceiver;  //For receiving ack when message is sent.


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.deleteDatabase("chat.db");

        // TODO initialize sendResultReceiver
        sendResultReceiver = new ResultReceiverWrapper(new Handler());

        /**
         * Initialize settings to default values.
         */
        if (!Settings.isRegistered(this)) {
            // TODO launch registration activity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
//            return;
        }

        setContentView(R.layout.messages);
        chatRoomName = (EditText) findViewById(R.id.chat_room);
        messageTextEditText = (EditText) findViewById(R.id.message_text);
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);
        messageList = (ListView) findViewById(R.id.message_list);

        // TODO use SimpleCursorAdapter to display the messages received.
        messagesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, null,
                new String[]{MessageContract.MESSAGE_TEXT, MessageContract.SENDER, MessageContract.MESSAGE_SEQUENCE_NUMBER},
                new int[]{android.R.id.text1, android.R.id.text2, android.R.id.text2}, 0);
        customAdapter = new ThreeTextviewCursorAdapter(this, null, 0);
        messageList.setAdapter(customAdapter);
//        messageList.setAdapter(messagesAdapter);

        // TODO create the message and peer managers, and initiate a query for all messages
        messageManager = new MessageManager(this);
        peerManager = new PeerManager(this);
        messageManager.getAllMessagesAsync(this);  //LoaderManager

        // TODO instantiate helper for service
        helper = new ChatHelper(this);
    }

    public void onResume() {
        super.onResume();
        sendResultReceiver.setReceiver(this);
    }

    public void onPause() {
        super.onPause();
        sendResultReceiver.setReceiver(null);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO inflate a menu with PEERS and SETTINGS options
        getMenuInflater().inflate(R.menu.chatserver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            // TODO PEERS provide the UI for viewing list of peers
            case R.id.peers:
                Intent peersIntent = new Intent(this, ViewPeersActivity.class);
                startActivity(peersIntent);
                break;

            // TODO SETTINGS provide the UI for settings
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            default:
        }
        return false;
    }


    /*
     * Callback for the SEND button.
     */
    public void onClick(View v) {
        if (helper != null) {

            final String chatRoom;

            final String messageText = messageTextEditText.getText().toString();

            //helper.register(Settings.getChatName(this), sendResultReceiver);

            // TODO get chatRoom and message from UI, and use helper to post a message
            // TODO add the message to the database
            chatRoom = chatRoomName.getText().toString();

            message = new ChatMessage();
            message.sender = Settings.getChatName(this);
            message.messageText = messageText;
            message.timestamp = new Date();
            final Peer sender = new Peer();
            sender.name = Settings.getChatName(this);
            sender.timestamp = new Date();
            peerManager.getPeerAsync(sender.name, new IContinue<Cursor>() {
                @Override
                public void kontinue(Cursor value) {
                    boolean isExist = value.getCount() != 0;
                    if (isExist) {
                        value.moveToFirst();
                        final long peerId = value.getLong(value.getColumnIndex(PeerContract._ID));
                        peerManager.updateAsync(sender.timestamp, PeerContract.NAME + "=?", new String[]{sender.name}, new IContinue<Long>() {
                            @Override
                            public void kontinue(Long value) {
                                message.senderId = peerId;
                                messageManager.persistAsync(message, new IContinue<Long>() {
                                    @Override
                                    public void kontinue(Long value) {
                                        message.id = value;
                                    }
                                });

                                helper.postMessage(chatRoom, messageText, sendResultReceiver);
                            }
                        });
                    } else {
                        peerManager.persistAsync(sender, new IContinue<Long>() {
                            @Override
                            public void kontinue(Long value) {
                                message.senderId = value;
                                messageManager.persistAsync(message, new IContinue<Long>() {
                                    @Override
                                    public void kontinue(Long value) {
                                        message.id = value;
                                    }
                                });

                                helper.postMessage(chatRoom, messageText, sendResultReceiver);
                            }
                        });
                    }
                }
            });

            // End todo

            Log.i(TAG, "Sent message: " + messageText);
            //messageText.setText("");
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case RESULT_OK:
                // TODO show a success toast message
                Toast.makeText(this, "Message Sent", Toast.LENGTH_LONG);
                //TODO get the id from bundle and update the message id to non-zero value
                int messageSequenceNumber = data.getInt(ResultReceiverWrapper.MESSAGE_SEQUENCE_NUMBER);
                messageManager.updateAsync(messageSequenceNumber, MessageContract.ID + "=?", new String[]{String.valueOf(message.id)}, null);
                break;
            default:
                // TODO show a failure toast message
                Toast.makeText(this, "Message Sent FAILED", Toast.LENGTH_LONG);
                break;
        }
    }

    @Override
    public void handleResults(TypedCursor<ChatMessage> results) {
        // TODO
        Cursor cursor = results.getCursor();
        customAdapter.swapCursor(cursor);
        //customAdapter.notifyDataSetChanged();
//        messagesAdapter.swapCursor(cursor);
//        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeResults() {
        // TODO
    }

}