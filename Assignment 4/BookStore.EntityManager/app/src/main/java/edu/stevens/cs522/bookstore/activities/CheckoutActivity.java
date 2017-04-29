package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.stevens.cs522.bookstore.R;


public class CheckoutActivity extends Activity {

    public static String CHECKOUT_RESULT = "howManyBooks";
    String howManyBooks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO display ORDER and CANCEL options.
        getMenuInflater().inflate(R.menu.checkout_menu, menu);
        howManyBooks = String.valueOf(getIntent().getIntExtra(CHECKOUT_RESULT, 0));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
        switch (item.getItemId()){
            case R.id.checkout:
                Toast.makeText(this, "Checked out " + howManyBooks + " books.", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.checkout_cancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
		// ORDER: display a toast message of how many books have been ordered and return
		
		// CANCEL: just return with REQUEST_CANCELED as the result code

		return false;
	}
	
}