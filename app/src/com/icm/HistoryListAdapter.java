package com.icm;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.icm.bean.LoanBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HistoryListAdapter extends ArrayAdapter<LoanBean> {

    private final LayoutInflater layoutInflator;
    private final List<LoanBean> beans;
    private final int resource = R.layout.history_list_item;

    public HistoryListAdapter(Activity context, int resource, List<LoanBean> objects) {
        super(context, resource, objects);
        this.layoutInflator = context.getLayoutInflater();
        this.beans = objects;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if (row == null){
            row = layoutInflator.inflate(resource, null);
        }
        
        LoanBean bean = getItem(position);
        
        ImageView imageView = (ImageView) row.findViewById(R.id.history_row_imageView);
        final int maxWidth = (int) (((float)parent.getWidth()) * 0.2);
        imageView.setMaxWidth(maxWidth);
        ImageLoader.getInstance().displayImage(bean.imageURL, imageView);
        
        
        TextView bookNameTextView = (TextView) row.findViewById(R.id.history_row_bookNameView);
        bookNameTextView.setText(bean.name);
        
        TextView dueDateTextView = (TextView) row.findViewById(R.id.history_row_dateView);
        dueDateTextView.setText(bean.dueDate);
        
        TextView personNameTextView = (TextView) row.findViewById(R.id.history_row_personView);
        personNameTextView.setText(bean.person);
        
        return row;
    }
    
    
    
}
