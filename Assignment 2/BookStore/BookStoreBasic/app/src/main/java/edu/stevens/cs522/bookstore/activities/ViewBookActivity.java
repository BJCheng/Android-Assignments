package edu.stevens.cs522.bookstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Book;

public class ViewBookActivity extends AppCompatActivity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_KEY = "book";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_book);

		// TODO get book as parcelable intent extra and populate the UI with book details.
        Intent intent = getIntent();
        Book book = intent.getParcelableExtra("book");
        TextView title = (TextView) findViewById(R.id.view_title);
        TextView author = (TextView) findViewById(R.id.view_author);
        TextView isbn = (TextView) findViewById(R.id.view_isbn);
        title.setText(book.title);
        author.setText(book.authors[0].toString());
        isbn.setText(book.isbn);
	}

}