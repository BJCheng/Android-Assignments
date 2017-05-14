/*********************************************************************

 Chat server: accept chat messages from clients.

 Sender chatName and GPS coordinates are encoded
 in the messages, and stripped off upon receipt.

 Copyright (c) 2017 Stevens Institute of Technology

 **********************************************************************/
package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.ChatRoom;
import edu.stevens.cs522.chat.managers.MessageManager;
import edu.stevens.cs522.chat.managers.TypedCursor;

public class ChatFragment extends Fragment implements OnClickListener, QueryBuilder.IQueryListener<ChatMessage> {

    private final static String TAG = ChatFragment.class.getCanonicalName();
    public final static String CHATROOM_KEY = "chatroom";

    public interface IChatListener {

        public void getMessages(ChatRoom chatroom);

        public void addMessageDialog(ChatRoom chatroom);

    }

    private IChatListener listener;
    private Context context;
    private ChatRoom chatroom;
    private ListView messageList;
    private View addButton;
    private SimpleCursorAdapter messagesAdapter;
    private MessageManager messageManager;


    public ChatFragment() {
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof IChatListener) {
            listener = (IChatListener) context;
        } else {
            throw new IllegalStateException("Activity must implement IChatListener!");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messagesAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, null,
                new String[]{MessageContract.MESSAGE_TEXT, MessageContract.SENDER},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        messageManager = new MessageManager(context);

        if (getArguments() != null) {
            setChatroom((ChatRoom) getArguments().getParcelable(CHATROOM_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.messages, container, false);

        messageList = (ListView)rootView.findViewById(R.id.message_list);
        addButton = rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        // TODO use SimpleCursorAdapter to display the messages received.
        messageList.setAdapter(messagesAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void onResume() {
        super.onResume();

        if (chatroom != null) {
            // TODO initiate a query for all messages in the activity
            messageManager.getAllMessagesAsync(chatroom, this);
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }


    public void setChatroom(ChatRoom chatroom) {
        this.chatroom = chatroom;

        // TODO initiate a query for all messages in the activity
        messageManager.getAllMessagesAsync(chatroom, this);
    }

    @Override
    public void handleResults(TypedCursor<ChatMessage> results) {
        // TODO
        Cursor cursor = results.getCursor();
        messagesAdapter.swapCursor(cursor);
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeResults() {
        // TODO
    }

    /*
     * Callback for the SEND button.
     */
    public void onClick(View v) {

        if (chatroom != null) {
            listener.addMessageDialog(chatroom);
        }

    }

}