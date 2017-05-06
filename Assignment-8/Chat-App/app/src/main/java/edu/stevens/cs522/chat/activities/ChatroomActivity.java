/*********************************************************************

    Chat server: accept chat messages from clients.
    
    Sender chatName and GPS coordinates are encoded
    in the messages, and stripped off upon receipt.

    Copyright (c) 2017 Stevens Institute of Technology

**********************************************************************/
package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Date;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.contracts.ChatroomContract;
import edu.stevens.cs522.chat.dialog.SendMessage;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.ChatRoom;
import edu.stevens.cs522.chat.managers.ChatroomManager;
import edu.stevens.cs522.chat.managers.MessageManager;
import edu.stevens.cs522.chat.managers.PeerManager;
import edu.stevens.cs522.chat.managers.TypedCursor;
import edu.stevens.cs522.chat.rest.ChatHelper;
import edu.stevens.cs522.chat.rest.ServiceManager;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.ResultReceiverWrapper;

public class ChatroomActivity extends Activity implements IIndexManager<ChatRoom>, ChatFragment.IChatListener,
        SendMessage.IMessageSender, ResultReceiverWrapper.IReceive {

    private final static String TAG = ChatroomActivity.class.getCanonicalName();
    private final static String SHOWING_CHATROOMS_TAG = "INDEX-FRAGMENT";
    private final static String SHOWING_MESSAGES_TAG = "CHAT-FRAGMENT";
    private final static String ADDING_MESSAGE_TAG = "ADD-MESSAGE-DIALOG";
    /*
     * Managers
     */
    private ChatroomManager chatroomManager;
    private MessageManager messageManager;
    private PeerManager peerManager;
    private ServiceManager serviceManager;
    /**
     * Fragments
     */
    private boolean isTwoPane;
    private IIndexManager.Callback<ChatRoom> indexFragment;
    private ChatFragment chatFragment;

    /*
     * Helper for Web service
     */
    private ChatHelper helper;

    /*
     * For receiving ack when message is sent.
     */
    private ResultReceiverWrapper sendResultReceiver;

    /*
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatrooms);

        isTwoPane = getResources().getBoolean(R.bool.is_two_pane);

        if (!isTwoPane) {
            // TODO add an index fragment as the fragment in the frame layout


        }

        // TODO create the message and peer and chatroom managers
        chatroomManager = new ChatroomManager(this);
        messageManager = new MessageManager(this);
        peerManager = new PeerManager(this);

        // TODO instantiate helper for service
        helper = new ChatHelper(this);

        // TODO initialize sendResultReceiver and serviceManager
        sendResultReceiver = new ResultReceiverWrapper(new Handler());

        /**
         * Initialize settings to default values.
         */
        if (!Settings.isRegistered(this)) {
            Settings.getClientId(this);
            // TODO launch registration activity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

    }

    public void onResume() {
        super.onResume();
        sendResultReceiver.setReceiver(this);
        serviceManager.scheduleBackgroundOperations();
    }

    public void onPause() {
        super.onPause();
        sendResultReceiver.setReceiver(null);
        serviceManager.cancelBackgroundOperations();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO inflate a menu with PEERS and SETTINGS options


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent;
        switch (item.getItemId()) {

            // TODO PEERS provide the UI for viewing list of peers
            case R.id.peers:
                intent = new Intent(this, ViewPeersActivity.class);
                startActivity(intent);
                break;

            // TODO SETTINGS provide the UI for settings
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case RESULT_OK:
                // TODO show a success toast message
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                break;
            default:
                // TODO show a failure toast message
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Callbacks for index fragment
     */

    @Override
    public SimpleCursorAdapter getIndexTitles(Callback<ChatRoom> callback) {
        indexFragment = callback;
        indexFragment.setIndexTitle(getString(R.string.chat_rooms_title));
        chatroomManager.getAllChatroomsAsync(chatroomQueryListener);

        String[] from = {ChatroomContract.NAME};
        int[] to = { android.R.id.text1 };
        return new SimpleCursorAdapter(this, android.R.layout.simple_list_item_activated_1, null, from, to, 0);
    }

    @Override
    public void onItemSelected(ChatRoom chatroom) {
        if (isTwoPane) {
            // For two pane, push selection of chatroom to chat fragment, which will then
            // ask the parent activity to query the database.
            chatFragment = (ChatFragment) getFragmentManager().findFragmentById(R.id.chat_fragment);
            chatFragment.setChatroom(chatroom);
        } else {
            // For single pane, replace index fragment with messages fragment
            chatFragment = new ChatFragment();
            Bundle args = new Bundle();
            args.putString(ChatFragment.CHATROOM_KEY, chatroom.name);
            chatFragment.setArguments(args);
            // TODO replace index fragment

        }
    }

    /**
     * Callbacks for querying database for chatrooms
     */

    private QueryBuilder.IQueryListener<ChatRoom> chatroomQueryListener = new QueryBuilder.IQueryListener<ChatRoom>() {

        @Override
        public void handleResults(TypedCursor<ChatRoom> results) {
            indexFragment.setTitles(results);
        }

        @Override
        public void closeResults() {
            indexFragment.clearTitles();
        }

    };

    /**
     * Callbacks for chat fragment
     */

    @Override
    public void getMessages(ChatRoom chatroom) {
        messageManager.getAllMessagesAsync(chatroom, messageQueryListener);
    }

    @Override
    public void addMessageDialog(ChatRoom chatroom) {
        SendMessage.launch(this, ADDING_MESSAGE_TAG, chatroom);
    }

    /**
     * Callbacks for querying for messages
     */

    private QueryBuilder.IQueryListener<ChatMessage> messageQueryListener = new QueryBuilder.IQueryListener<ChatMessage>() {

        @Override
        public void handleResults(TypedCursor<ChatMessage> results) {
            if (chatFragment != null) {
                chatFragment.handleResults(results);
            }
        }

        @Override
        public void closeResults() {
            if (chatFragment != null) {
                chatFragment.closeResults();
            }
        }

    };

    /**
     * Callbacks for message posting dialog
     */

    @Override
    public void send(ChatRoom chatroom, String message, Double latitude, Double longitude, Date timestamp) {
        helper.postMessage(chatroom.name, message);
    }
}