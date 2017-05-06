package edu.stevens.cs522.bookstoredatabase.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.stevens.cs522.bookstoredatabase.R;
import edu.stevens.cs522.bookstoredatabase.entities.Book;


public class ViewBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_KEY = "book";

    private TextView titleTextView;
    private TextView authorsTextView;
    private TextView isbnTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_book);

        titleTextView = (TextView)findViewById(R.id.view_title);
        authorsTextView = (TextView)findViewById(R.id.view_author);
        isbnTextView = (TextView)findViewById(R.id.view_isbn);

		// TODO get book as parcelable intent extra and populate the UI with book details.
		Book book = getIntent().getParcelableExtra(BOOK_KEY);
        titleTextView.setText(book.title);
        authorsTextView.setText(book.getAuthors());
        isbnTextView.setText(book.isbn);
	}

}