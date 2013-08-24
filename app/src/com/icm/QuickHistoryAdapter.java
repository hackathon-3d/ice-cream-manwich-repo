package com.icm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class QuickHistoryAdapter extends ArrayAdapter<QuickHistory> {

	private QuickHistory history;
	
	
	public QuickHistoryAdapter(Context context) {
		super(context, R.layout.history_list_item);
	}
	
	
	public void swapContent(QuickHistory history)
	{
		this.history = history;
		
		this.notifyDataSetChanged();
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}
	
	
	


}
