package edu.stevens.cs522.chat.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.async.IContinue;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.ChatRoom;
import edu.stevens.cs522.chat.managers.ChatroomManager;
import edu.stevens.cs522.chat.managers.TypedCursor;

/**
 * Created by bj on 10/05/2017.
 */

public class CreateChatRoom extends DialogFragment {

    private Button createChatRoom;
    private Button cancelButton;
    private EditText chatRoom;
    private ChatroomManager chatroomManager;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatroomManager = new ChatroomManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_chat_room, container);
        createChatRoom = (Button) rootView.findViewById(R.id.create);
        cancelButton = (Button) rootView.findViewById(R.id.cancel_chat);
        chatRoom = (EditText) rootView.findViewById(R.id.chat_room_text);

        createChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatroomManager.persistAsync(chatRoom.getText().toString(), new ChatRoomCreateCallback());
                CreateChatRoom.this.getDialog().cancel();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChatRoom.this.getDialog().cancel();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public static void launch(Activity context, String tag) {
        CreateChatRoom createChatRoomDialog = new CreateChatRoom();
        createChatRoomDialog.show(context.getFragmentManager(), tag);
    }


    public class ChatRoomCreateCallback implements  IContinue<Uri>{
        public void kontinue(Uri value){
            Toast.makeText(context, "Chat Room Created", Toast.LENGTH_LONG).show();
        }
    }
}
