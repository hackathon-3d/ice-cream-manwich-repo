package com.icm;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockListActivity;

@ContentView(R.layout.activity_history)
public class HistoryActivity extends RoboSherlockListActivity {

    @InjectResource(R.array.default_history_items) String[] historyItemTitles;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historyItemTitles));
    }
    
    

}
