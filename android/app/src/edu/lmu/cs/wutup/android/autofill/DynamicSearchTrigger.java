package edu.lmu.cs.wutup.android.autofill;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import edu.lmu.cs.wutup.android.manager.LogTags;

public class DynamicSearchTrigger<T> implements TextWatcher {
	
/**********************************************************************************************************************
 * Member Variables BEGIN
 **********************************************************************************************************************/	
	
	public static final boolean MAY_INTERRUPT_SEARCH = false;
	
	private boolean textChanged = false;
	private AsyncTask<Object, Integer, Void> dynamicSearch = null;
	
	private Class<T> c;
	private ManualListAdapter<T> adpater;
	private String address;
	
/**********************************************************************************************************************
 * Member Variables END & Constructors BEGIN
 **********************************************************************************************************************/

	public DynamicSearchTrigger(Class<T> c, ManualListAdapter<T> adapter, String address) {
		this.c = c;
		this.adpater = adapter;
		this.address = address;
	}
	
/**********************************************************************************************************************
 * Constructors END & Public Methods BEGIN
 **********************************************************************************************************************/
	
	@Override
	public void afterTextChanged(Editable s) {
		
		String newText = s.toString();
		
		Log.i(LogTags.AUTO_COMPLETE, "Event text view changed. New text reads \"" + newText + "\".");
		
		if (textChanged & !newText.matches("")) {
			
			if (dynamicSearch != null) {
				dynamicSearch.cancel(MAY_INTERRUPT_SEARCH);
				Log.i(LogTags.AUTO_COMPLETE, "Canceled previous dynamic search for " + c.toString() + ".");
			}
			
			String queryParameters = "?name=" + newText;
			dynamicSearch = new DynamicSearch<T>().execute(c, adpater, address + queryParameters);
			Log.i(LogTags.AUTO_COMPLETE, "Executed new dynamic search for " + c.toString() + ", with text \"" + newText + "\".");
			
			textChanged = false;
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		textChanged = true;		
		Log.i(LogTags.AUTO_COMPLETE, "Auto complete text feild text changed.");
	}
	
/**********************************************************************************************************************
 * Public Methods END
 **********************************************************************************************************************/	

}
