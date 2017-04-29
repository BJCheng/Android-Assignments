package edu.stevens.cs522.bookstoredatabase.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.DeniedByServerException;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.stevens.cs522.bookstoredatabase.R;
import edu.stevens.cs522.bookstoredatabase.databases.CartDbAdapter;
import edu.stevens.cs522.bookstoredatabase.entities.Book;

public class MainActivity extends Activity {
	
	// Use this when logging errors and warnings.
	@SuppressWarnings("unused")
	private static final String TAG = MainActivity.class.getCanonicalName();
	// These are request codes for subactivity request calls
	static final private int ADD_REQUEST = 1;
    public static final String ADD_BOOK_KEY = "book";
	@SuppressWarnings("unused")
	static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;
	// The database adapter
	private CartDbAdapter dba;

    ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //this.deleteDatabase("books.db");
		// TODO check if there is saved UI state, and if so, restore it (i.e. the cart contents)

		// TODO Set the layout (use cart.xml layout)
        setContentView(R.layout.cart);

		// TODO open the database using the database adapter
        dba = new CartDbAdapter(this);
        dba.open();

        // TODO query the database using the database adapter, and manage the cursor on the main thread
        final Cursor booksCursor = dba.fetchAllBooks();
        startManagingCursor(booksCursor);

        // TODO use SimpleCursorAdapter to display the cart contents.
        final SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, booksCursor, new String[]{"title", "authors"}, new int[] {android.R.id.text1, android.R.id.text2}, 0);

        listView = (ListView)findViewById(R.id.booksListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = new Book(booksCursor);
                Intent intent = new Intent(getApplicationContext(), ViewBookActivity.class);
                intent.putExtra(ViewBookActivity.BOOK_KEY, book);
                startActivity(intent);
            }
        });
        listView.setAdapter(cursorAdapter);

        //CAB
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if(checked){
                    Book book = new Book(booksCursor);
                    book.id = id;
                    dba.addSelectedBook(book);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.cab_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cabDelete:
                        for(Book book: dba.selectedBook){
                            dba.delete(book);
                        }
                        mode.finish();
                        break;
                    case R.id.cabCancel:
                        mode.finish();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                dba.selectedBook.clear();
                SimpleCursorAdapter adapter = (SimpleCursorAdapter)listView.getAdapter();
                adapter.getCursor().requery();
                adapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
	}

    public void onDestroy() {
        super.onDestroy();
        dba.close();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO inflate a menu with ADD and CHECKOUT options
        getMenuInflater().inflate(R.menu.bookstore_menu, menu);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            // TODO ADD provide the UI for adding a book
            case R.id.add:
                 Intent addIntent = new Intent(this, AddBookActivity.class);
                 startActivityForResult(addIntent, ADD_REQUEST);
                break;

            // TODO CHECKOUT provide the UI for checking out
            case R.id.checkout:
                Intent intent = new Intent(this, CheckoutActivity.class);
                intent.putExtra(CheckoutActivity.CHECKOUT_KEY, listView.getAdapter().getCount());
                startActivity(intent);
                break;

            default:
        }
        return false;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// TODO Handle results from the Search and Checkout activities.

        // Use ADD_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
        SimpleCursorAdapter adapter = (SimpleCursorAdapter)listView.getAdapter();
        switch(requestCode) {
            case ADD_REQUEST:
                // ADD: add the book that is returned to the shopping cart.
                if(resultCode == Activity.RESULT_OK){
                    Book book = intent.getParcelableExtra(ADD_BOOK_KEY);
                    dba.persist(book);
                    adapter.getCursor().requery();
                }
                break;
            case CHECKOUT_REQUEST:
                // CHECKOUT: empty the shopping cart.
                if(resultCode==Activity.RESULT_OK){
                    dba.deleteAll();
                    adapter.getCursor().requery();
                }
                break;
        }
        adapter.notifyDataSetChanged();

	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO save the shopping cart contents (which should be a list of parcelables).
		
	}
	
}