package grischenkomaxim.converter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ConverterAdapter extends BaseAdapter {

	List<Converter> converters;
	Context ctx;
	LayoutInflater lInflater;
	  
	ConverterAdapter(Context context, List<Converter> converterList) {
	    ctx = context;
	    converters = converterList;
	    lInflater = (LayoutInflater) ctx
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return converters.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return converters.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
	    if (view == null) {
	      view = lInflater.inflate(R.layout.item, parent, false);
	    }

	    Converter c = getConverter(position);

	    // заполняем View в пункте списка данными из товаров: наименование, цена
	    // и картинка
	    ((TextView) view.findViewById(R.id.description)).setText(c.description);
	    if (MainActivity.enterValue.getText().length() != 0 && (0 != c.course.compareTo(BigDecimal.ZERO)) && c.course != null){
	    	((TextView) view.findViewById(R.id.summ)).setText(c.course.multiply(new BigDecimal(MainActivity.enterValue.getText().toString()), MathContext.UNLIMITED).toString());
	    }
	    else{
	    	((TextView) view.findViewById(R.id.summ)).setText("0");
	    }
	    ((EditText) view.findViewById(R.id.editTextCourse)).setText(c.course.toString());

	    ((EditText) view.findViewById(R.id.editTextCourse)).setOnEditorActionListener(listner);
	    ((EditText) view.findViewById(R.id.editTextCourse)).setTag(position);

	    return view;
	}
	
	Converter getConverter(int position) {
	    return ((Converter) getItem(position));
	  }

	OnEditorActionListener listner = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			Converter c = getConverter((Integer) v.getTag());
			if (v.getText().length() != 0){
				c.course = new BigDecimal(v.getText().toString()); 
			}
			else{
				c.course = BigDecimal.ZERO;
			}
			updateSumm(c);
			
			return false;
		}
	};
	
	void updateSumm(Converter c){
		if (MainActivity.enterValue.getText().length() != 0){
				c.summ = c.course.multiply(new BigDecimal(MainActivity.enterValue.getText().toString()), MathContext.UNLIMITED);
			}
			else{
		    	c.summ = BigDecimal.ZERO;
		    }
	}
}
