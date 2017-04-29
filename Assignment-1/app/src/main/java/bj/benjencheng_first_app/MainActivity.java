package bj.benjencheng_first_app;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        Intent intent = new Intent(this, secondActivity.class);
        intent.putExtra("message", editText.getText().toString());
        startActivity(intent);
    }
}
