package edu.stevens.cs522.bookstoredatabase.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.stevens.cs522.bookstoredatabase.R;
import edu.stevens.cs522.bookstoredatabase.contracts.BookContract;
import edu.stevens.cs522.bookstoredatabase.databases.CartDbAdapter;
import edu.stevens.cs522.bookstoredatabase.entities.Author;
import edu.stevens.cs522.bookstoredatabase.entities.Book;


public class AddBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_RESULT_KEY = "book_result";

    EditText titleEditText;
    EditText authorEditText;
    EditText isbnEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book);

        titleEditText = (EditText)findViewById(R.id.search_title);
        authorEditText = (EditText) findViewById(R.id.search_author);
        isbnEditText = (EditText)findViewById(R.id.search_isbn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        // TODO provide ADD and CANCEL options
        getMenuInflater().inflate(R.menu.addbook_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
		switch (item.getItemId()){
            // ADD: return the book details to the BookStore activity
            case R.id.addBook:
                Book book = addBook();
                Intent intent = new Intent();
                intent.putExtra(MainActivity.ADD_BOOK_KEY, book);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            // CANCEL: cancel the request
            case R.id.addBookCancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }

		return false;
	}
	
	public Book addBook(){
		// TODO Just build a Book object with the search criteria and return that.
        Book book = new Book();
        book.title = titleEditText.getText().toString();
        book.isbn = isbnEditText.getText().toString();
        List<Author> authors = new ArrayList<>();
        for(String authorString: authorEditText.getText().toString().split("\\|")){
            authors.add(new Author(authorString));
        }
        book.authors = authors.toArray(new Author[0]);
		return book;
	}

}