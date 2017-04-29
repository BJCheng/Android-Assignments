package edu.stevens.cs522.bookstore.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.util.BooksAdapter;

//TODO use context menu for listview or contextual action bar to fulfill the delete feature.
//search remain unclear

public class MainActivity extends AppCompatActivity {

    // Use this when logging errors and warnings.
    private static final String TAG = MainActivity.class.getCanonicalName();

    // These are request codes for subactivity request calls
    static final private int ADD_REQUEST = 1;

    static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;

    // There is a reason this must be an ArrayList instead of a List.
    private ArrayList<Book> shoppingCart;
    private BooksAdapter booksAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        if (savedInstanceState != null)
            shoppingCart = savedInstanceState.getParcelableArrayList("shoppingCart");
        else
            shoppingCart = new ArrayList<>();
        booksAdapter = new BooksAdapter(this, shoppingCart);

        ListView listView = (ListView) findViewById(R.id.mainListView);
        listView.setAdapter(booksAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewBookActivity.class);
                Book book = shoppingCart.get(position);
                intent.putExtra("book", book);
                startActivity(intent);
            }
        });

        //CAB
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Book book = shoppingCart.get(position);
                if (checked)
                    booksAdapter.addNewSelectedBooks(book);
                else
                    booksAdapter.removeSelectedBooks(book);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.main_cab_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainCabCancel:
                        mode.finish();
                        break;
                    case R.id.mainCabDelete:
                        shoppingCart.removeAll(booksAdapter.getSelectedBooks());
                        booksAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                booksAdapter.clearSelection();
                booksAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
//                listView.setItemChecked(position, !booksAdapter.isPositionChecked(position));
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bookstore_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.add:
                Intent addIntent = new Intent(this, AddBookActivity.class);
                startActivityForResult(addIntent, ADD_REQUEST);
                break;

            case R.id.checkout:
                Intent intent = new Intent(this, CheckoutActivity.class);
                intent.putExtra("howManyBooks", shoppingCart.size());
                startActivityForResult(intent, CHECKOUT_REQUEST);
                break;

            default:
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Use ADD_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
        switch (requestCode) {
            case ADD_REQUEST:
                if (resultCode == Activity.RESULT_CANCELED) break;
                Book book = intent.getParcelableExtra("book");
                shoppingCart.add(book);
                booksAdapter.notifyDataSetChanged();
                break;
            case CHECKOUT_REQUEST:
                if (resultCode == RESULT_CANCELED) break;
                shoppingCart.clear();
                booksAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("shoppingCart", shoppingCart);
    }

}