package edu.lmu.cs.wutup.android.autofill;

import java.util.List;

import android.widget.ArrayAdapter;
import android.widget.Filter;

public class PassThrough<T> extends Filter {
	
	private List<T> items;
	private ArrayAdapter<T> adapter;
	
	public PassThrough (ArrayAdapter<T> adapter, List<T> items) {
		this.adapter = adapter;
		this.items = items;
	}
	
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
		
		FilterResults allItems = new FilterResults();
		allItems.values = items;
		
        return allItems;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
    	
        if (results != null && results.count > 0) {
            adapter.notifyDataSetChanged();
        }
        else {
            adapter.notifyDataSetInvalidated();
        }
        
    }

}
