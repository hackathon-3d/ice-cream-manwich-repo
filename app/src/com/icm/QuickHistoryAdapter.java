package com.icm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class QuickHistoryAdapter extends ArrayAdapter<QuickHistory.Item> {

	
	
	public QuickHistoryAdapter(Context context) {
		super(context, R.layout.history_list_item);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		QuickHistory.Item item = getItem(position);
		
		
		View view = convertView;
		if(view == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			view = layoutInflater.inflate(R.layout.history_list_item, parent);
		}
		
		ImageView imageView = (ImageView) view.findViewById(R.id.history_row_imageView);
		ImageLoader.getInstance().displayImage(item.image.toString(), imageView);
		
		TextView nameView = (TextView) view.findViewById(R.id.history_row_bookNameView);
		nameView.setText(item.name);
		
		TextView personView = (TextView) view.findViewById(R.id.history_row_personView);
		personView.setText(item.person);
		
		TextView dateView = (TextView) view.findViewById(R.id.history_row_dateView);
		dateView.setText(item.date);
		
		return view;
	}
	
	
	


}
