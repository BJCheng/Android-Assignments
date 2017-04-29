package edu.stevens.cs522.bookstore.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Book;

/**
 * Created by dduggan
 */

public class BooksAdapter extends ArrayAdapter<Book> {

    ArrayList<Book> selectedBooks = new ArrayList<>();

    public BooksAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Book book = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_row, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.cart_row_title);
        TextView author = (TextView) convertView.findViewById(R.id.cart_row_author);
        // Populate the data into the template view using the data object
        title.setText(book.title);
        author.setText(book.getFirstAuthor());
        // Return the completed view to render on screen

        if (selectedBooks.contains(book)) {
            convertView.setBackgroundColor(Color.RED);// this is a selected position so make it red
        }
        else
            convertView.setBackgroundColor(Color.argb(0,238, 238, 238));// this is a selected position so make it red

        return convertView;
    }
    public void addNewSelectedBooks(Book book){
        selectedBooks.add(book);
        notifyDataSetChanged();
    }
    public void removeSelectedBooks(Book book){
        selectedBooks.remove(book);
        notifyDataSetChanged();
    }
    public void clearSelection(){
        selectedBooks.clear();
        notifyDataSetChanged();
    }
    public ArrayList<Book> getSelectedBooks(){
        return selectedBooks;
    }
}
