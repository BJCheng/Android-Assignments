package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

public class AddBookActivity extends AppCompatActivity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_RESULT_KEY = "book_result";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addbook_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()){
            case R.id.addAdd:
                EditText titleET = (EditText)findViewById(R.id.search_title);
                EditText authorET = (EditText)findViewById(R.id.search_author);
                EditText isbnET = (EditText)findViewById(R.id.search_isbn);
                Author[] authors = new Author[]{new Author(authorET.getText().toString())};
                Book book = new Book(1, titleET.getText().toString(), authors, isbnET.getText().toString(), "$123");
                Intent intent = new Intent();
                intent.putExtra("book", book);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.addCancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
		return false;
	}

	public Book addBook(String title, Author[] authors, String isbn, String price){
		// TODO Just build a Book object with the search criteria and return that.

		return null;
	}

}