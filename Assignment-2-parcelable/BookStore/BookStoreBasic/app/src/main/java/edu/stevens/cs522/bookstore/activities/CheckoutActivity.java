package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.stevens.cs522.bookstore.R;

public class CheckoutActivity extends AppCompatActivity {

    private int howManyBooks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);

        Intent intent = getIntent();
        howManyBooks = intent.getIntExtra("howManyBooks", 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkout_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()){
            case R.id.checkoutOrder:
                Toast.makeText(this, String.format("You've checked out %d book%s", howManyBooks, (howManyBooks>1)?"s":""), Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.checkoutCancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
		return false;
	}
	
}