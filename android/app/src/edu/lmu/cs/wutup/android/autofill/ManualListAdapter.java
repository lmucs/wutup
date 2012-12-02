package edu.lmu.cs.wutup.android.autofill;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


public class ManualListAdapter<T> extends ArrayAdapter<T> implements Filterable {
	
    private ArrayList<T> items = new ArrayList<T>();
	

	public ManualListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	@Override
	public void addAll(Collection<? extends T> collection) {
		items.addAll(collection);		
	}
	
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int index) {
        return items.get(index);
    }
    
    @Override
    public Filter getFilter() {
        return new PassThrough<T>(this, items);
    }

}