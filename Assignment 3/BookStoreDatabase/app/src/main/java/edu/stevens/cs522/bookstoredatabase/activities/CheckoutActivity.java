package edu.stevens.cs522.bookstoredatabase.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.stevens.cs522.bookstoredatabase.R;
import edu.stevens.cs522.bookstoredatabase.databases.CartDbAdapter;


public class CheckoutActivity extends Activity {

    public static final String CHECKOUT_KEY = "howManyBooks";
    private int howManyBooks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);

        Intent intent = getIntent();
        howManyBooks = intent.getIntExtra(CHECKOUT_KEY, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO display ORDER and CANCEL options.
        getMenuInflater().inflate(R.menu.checkout_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
		switch (item.getItemId()){
            // ORDER: display a toast message of how many books have been ordered and return
            case R.id.checkoutOrder:
                CartDbAdapter dba = new CartDbAdapter(this);
                dba.open();
                Toast.makeText(this, "You've checked out "+String.valueOf(dba.deleteAll()) + " books", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
                break;
            // CANCEL: just return with REQUEST_CANCELED as the result code
            case R.id.checkoutCancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }

		return false;
	}
	
}