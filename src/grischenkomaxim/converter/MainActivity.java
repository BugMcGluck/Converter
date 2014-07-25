package grischenkomaxim.converter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class MainActivity extends ActionBarActivity implements OnEditorActionListener{

    EditText enterValue, course1, course2;
    TextView convertedValue1, convertedValue2;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        enterValue = (EditText) findViewById(R.id.editTextEnterValue);
        convertedValue1 = (TextView) findViewById(R.id.textViewConvertedValue1);
        convertedValue2 = (TextView) findViewById(R.id.textViewConvertedValue2);
        course1 = (EditText) findViewById(R.id.editTextCourse1);
        course2 = (EditText) findViewById(R.id.editTextCourse2);
        
        enterValue.setOnEditorActionListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		if (course1.getText().length() != 0 && course2.getText().length() != 0 && enterValue.getText().length() != 0) {
		convertedValue1.setText(String.valueOf(Float.valueOf(course1.getText().toString()) * 
					Float.valueOf(enterValue.getText().toString())));
		convertedValue2.setText(String.valueOf(Float.valueOf(course2.getText().toString()) * 
					Float.valueOf(enterValue.getText().toString())));
		return true;
		}
		else{
			convertedValue1.setText("0");
			convertedValue2.setText("0");
			return false;
		}
	}
}
