package edu.stevens.cs522.chat.util;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.contracts.MessageContract;

/**
 * Created by bj on 05/04/2017.
 */

public class ThreeTextviewCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    public ThreeTextviewCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.three_textview_layout, parent, false);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView message = (TextView) view.findViewById(R.id.adapter_textview);
        TextView sender = (TextView) view.findViewById(R.id.adapter_sender);
        TextView sequenceNumber = (TextView) view.findViewById(R.id.adapter_sequence_number);

        message.setText(cursor.getString(cursor.getColumnIndex(MessageContract.MESSAGE_TEXT)));
        sender.setText(cursor.getString(cursor.getColumnIndex(MessageContract.SENDER)));
        sequenceNumber.setText(cursor.getString(cursor.getColumnIndex(MessageContract.MESSAGE_SEQUENCE_NUMBER)));
    }

}
