package com.icm;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.icm.bean.LoanBean;

public class HistoryListAdapter extends ArrayAdapter<LoanBean> {

    private final LayoutInflater layoutInflator;
    private final List<LoanBean> beans;
    private final int resource = R.layout.history_row_item;

    public HistoryListAdapter(Activity context, int resource, List<LoanBean> objects) {
        super(context, resource, objects);
        this.layoutInflator = context.getLayoutInflater();
        this.beans = objects;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if (row == null){
            row = layoutInflator.inflate(R.layout.history_row_item, null);
        }
    }
    
    
    
}
