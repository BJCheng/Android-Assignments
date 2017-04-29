package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.R;


public class ViewBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_KEY = "book";

    private ArrayAdapter<String> authorsAdapter;
    TextView titleTextView;
    TextView isbnTextView;
    ListView authorsListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_book);

        titleTextView = (TextView)findViewById(R.id.view_title);
        isbnTextView = (TextView)findViewById(R.id.view_isbn);
        authorsListView = (ListView)findViewById(R.id.view_authors);

        // TODO get book as parcelable intent extra and populate the UI with book details.
        Book book = getIntent().getParcelableExtra(BOOK_KEY);

        List<String> authors = new ArrayList<>();
        for(Author author: book.authors){
            authors.add(author.toString());
        }

        authorsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, authors);

        authorsListView.setAdapter(authorsAdapter);
        titleTextView.setText(book.title);
        isbnTextView.setText(book.isbn);
	}

}