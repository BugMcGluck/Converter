package grischenkomaxim.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.text.TextUtils;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class MainActivity extends ActionBarActivity implements OnEditorActionListener{

	final int DIALOG_ADD = 1;
	final int DIALOG_EDIT = 2;
	
	public static EditText enterValue;
    List<Converter> converters = new ArrayList<Converter>();
    ConverterAdapter adapter;
    int selectedPosition;
    TableLayout dialogView;
    int dialogType;
    SharedPreferences sPref;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        enterValue = (EditText) findViewById(R.id.editTextEnterValue);
//        enterValue.setOnEditorActionListener(this);

        loadData();
		adapter =  new ConverterAdapter(this, converters);
		
		ListView lvMain = (ListView) findViewById(R.id.lv);
	    lvMain.setAdapter(adapter);
	    registerForContextMenu(lvMain);
	    registerForContextMenu(findViewById(R.id.ll));
	    
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
	    
	    enterValue.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				adapter.notifyDataSetChanged();
				return false;
			}
		});
    }


    @Override
	protected void onDestroy() {
		super.onDestroy();
		saveData();
	}


	private void saveData() {
		sPref = getPreferences(MODE_PRIVATE);
	    Editor ed = sPref.edit();
	    ed.putInt(getString(R.string.converters_count), converters.size());
	    StringBuilder str = new StringBuilder();
	    for (Converter conv : converters) {
			str.append(conv.description).append(",").append(conv.course.toString()).append(",").append(conv.summ.toString()).append(";");
		}
	    ed.putString(getString(R.string.converter), str.toString());
	    ed.putString(getString(R.string.value), enterValue.getText().toString());
	    ed.commit();
	}


	void loadData(){
		sPref = getPreferences(MODE_PRIVATE);
		int count = sPref.getInt(getString(R.string.converters_count), 0);
		BigDecimal course;
		String description;
		BigDecimal summ;
		String str = sPref.getString(getString(R.string.converter), null);

		if (str != null && !TextUtils.isEmpty(str)) {
            StringTokenizer st = new StringTokenizer(str, ";");
            while(st.hasMoreTokens()) {
                	StringTokenizer st2 =  new StringTokenizer(st.nextToken(), ",");
                	while(st2.hasMoreTokens()){
                		description = st2.nextToken();
                		Log.d("!", description.toString());
            			course = new BigDecimal(st2.nextToken());
            			Log.d("!!", course.toString());
            			summ = new BigDecimal(st2.nextToken());
            			converters.add(new Converter(course, description, summ));
                	}
            }
        }
		String value = sPref.getString(getString(R.string.value), "");
		enterValue.setText(value);
		if (count == 0){
			showDialog(DIALOG_ADD);
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
			inflater.inflate(R.menu.context, menu);
			break;
		}
	    
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.add:
			showDialog(DIALOG_ADD);
			adapter.notifyDataSetChanged();
			break;
		case R.id.delete:
			converters.remove(selectedPosition);
			adapter.notifyDataSetChanged();
			break;
		case R.id.edit:
			showDialog(DIALOG_EDIT);
			adapter.notifyDataSetChanged();
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	
	 @Override
	  protected Dialog onCreateDialog(int id) {
	    AlertDialog.Builder adb = new AlertDialog.Builder(this);
	    if (id == DIALOG_ADD){
	    	adb.setTitle("Добавить конвертер");
	    }
	    if (id == DIALOG_EDIT){
	    	adb.setTitle("Изменить конвертер");
	    }
	    // создаем view из dialog.xml
	    dialogView = (TableLayout) getLayoutInflater()
	        .inflate(R.layout.add_item, null);
	    // устанавливаем ее, как содержимое тела диалога
	    adb.setView(dialogView);
//	    // находим TexView для отображения кол-ва
//	    tvCount = (TextView) view.findViewById(R.id.tvCount);
	 // кнопка положительного ответа
        adb.setPositiveButton(R.string.yes, addDialogListener);
        // кнопка нейтрального ответа
        adb.setNeutralButton(R.string.cancel, addDialogListener); 
        dialogType = id;
	    return adb.create();
	  }
	 
	 @Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
		EditText description = (EditText) dialogView.findViewById(R.id.editText_description);
		EditText course = (EditText) dialogView.findViewById(R.id.editTextCourseAdd);
		if (id == DIALOG_EDIT){	    	
	    	description.setText(converters.get(selectedPosition).description);
	    	course.setText(converters.get(selectedPosition).course.toString());
	    }
		if (id == DIALOG_ADD){
			description.setText("");
			course.setText("");
		}
	}

	OnClickListener addDialogListener = new OnClickListener() {
		 @Override   
		 public void onClick(DialogInterface dialog, int which) {
		      switch (which) {
		      // положительная кнопка
		      case Dialog.BUTTON_POSITIVE:
		        if(dialogType == DIALOG_ADD){
		        	addConverter();
		        	adapter.notifyDataSetChanged();
		        }
		    	if (dialogType == DIALOG_EDIT)   {
		    		editConverter();
		    		adapter.notifyDataSetChanged();
		    	}
		        break;
		      // нейтральная кнопка  
		      case Dialog.BUTTON_NEUTRAL:
		        break;
		      }
	    }

	  };

	protected void addConverter() {
		// TODO Auto-generated method stub
		
		BigDecimal course;
		String description;

		course = new BigDecimal(((EditText) dialogView.findViewById(R.id.editTextCourseAdd)).getText().toString());
		description = ((EditText) dialogView.findViewById(R.id.editText_description)).getText().toString();
		
		converters.add(new Converter(course, description, BigDecimal.ZERO));
	}


	protected void editConverter() {
		// TODO Auto-generated method stub
		BigDecimal course;
		String description;

		course = new BigDecimal(((EditText) dialogView.findViewById(R.id.editTextCourseAdd)).getText().toString());
		description = ((EditText) dialogView.findViewById(R.id.editText_description)).getText().toString();
		
		converters.set(selectedPosition, new Converter(course, description, BigDecimal.ZERO));
	}


}
