package edu.stevens.cs522.bookstore.util;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Book;

/**
 * Created by dduggan.
 */

public class BookAdapter extends ResourceCursorAdapter {

    protected final static int ROW_LAYOUT = android.R.layout.simple_list_item_2;
    private LayoutInflater layoutInflater;

    public BookAdapter(Context context, Cursor cursor) {
        super(context, ROW_LAYOUT, cursor, 0);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return layoutInflater.inflate(ROW_LAYOUT, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO
        TextView titleTextView = (TextView)view.findViewById(android.R.id.text1);
        TextView authorTextView = (TextView)view.findViewById(android.R.id.text2);

        Book book = new Book(cursor);
        titleTextView.setText(book.title);
        authorTextView.setText(book.getFirstAuthor());
    }
}