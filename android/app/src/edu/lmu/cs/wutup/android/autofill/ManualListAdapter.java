package edu.lmu.cs.wutup.android.autofill;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import edu.lmu.cs.wutup.android.manager.LogTags;

public class ManualListAdapter<T> extends ArrayAdapter<T> implements Filterable {
    
/**********************************************************************************************************************
 * Member Variables BEGIN
 **********************************************************************************************************************/
    
    private ArrayList<T> items = new ArrayList<T>();
    
/**********************************************************************************************************************
 * Member Variables END & Constructors BEGIN
 **********************************************************************************************************************/

    public ManualListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    
/**********************************************************************************************************************
 * Constructors END & Public Methods BEGIN
 **********************************************************************************************************************/    
    
    @Override
    public void clear() {
        items.clear();
        Log.i(LogTags.AUTO_COMPLETE, "Manual list adapter cleared.");
    }
    
    @Override
    public void addAll(Collection<? extends T> collection) {
        items.addAll(collection);
        Log.i(LogTags.AUTO_COMPLETE, "List of " + collection.size() + " items added to manual list adapter.");
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
    
/**********************************************************************************************************************
 * Public Methods END
 **********************************************************************************************************************/
    
}