package grischenkomaxim.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class MainActivity extends ActionBarActivity implements OnEditorActionListener{

    public static EditText enterValue;
    List<Converter> converters = new ArrayList<Converter>();
    ConverterAdapter adapter;
    int selectedPosition;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        enterValue = (EditText) findViewById(R.id.editTextEnterValue);
//        enterValue.setOnEditorActionListener(this);

        fillData();
		adapter =  new ConverterAdapter(this, converters);
		
		ListView lvMain = (ListView) findViewById(R.id.lv);
	    lvMain.setAdapter(adapter);
	    registerForContextMenu(lvMain);
	    
	    lvMain.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.d("!", "Position "+ position);
				selectedPosition = position;
				return false;
			}
	    });
    }


    void fillData(){
    	for (int i = 0; i < 3; i++){
    		converters.add(new Converter(new BigDecimal(i * 100F + 100), "Валюта " + (i + 1), BigDecimal.ZERO));
    	}
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
		adapter.notifyDataSetChanged();
		return false;
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		switch(v.getId()){
		case R.id.lv:
			inflater.inflate(R.menu.context_list, menu);
			break;
		case R.id.ll:
			inflater.inflate(R.menu.context_list, menu);
			break;
		}
	    
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.add:
			converters.add(new Converter(new BigDecimal(100F), "Новый", BigDecimal.ZERO));
			adapter.notifyDataSetChanged();
			break;
		case R.id.delete:
			converters.remove(selectedPosition);
			adapter.notifyDataSetChanged();
			break;
		}
		return super.onContextItemSelected(item);
	}
	
}
