package com.icm;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import android.os.Bundle;
import android.widget.ListAdapter;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockListActivity;
import com.icm.bean.LoanBean;

@ContentView(R.layout.activity_history)
public class HistoryActivity extends RoboSherlockListActivity {

    @InjectResource(R.array.default_history_item_titles)    String[] itemTitles;
    @InjectResource(R.array.default_history_images)         String[] images;
    @InjectResource(R.array.default_history_dueDates)       String[] dueDates;
    @InjectResource(R.array.default_history_people)         String[] peopleNames; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        List<LoanBean> beans = new ArrayList<LoanBean>();
        for(int i = 0; i < itemTitles.length; i++){
            LoanBean bean = new LoanBean();
            bean.name = itemTitles[i];
            bean.dueDate = dueDates[i];
            bean.imageURL = images[i];
            bean.person = peopleNames[i];
            beans.add(bean);
        }
        
        
        ListAdapter adapter = new HistoryListAdapter(this, R.layout.history_list_item, beans);
        setListAdapter(adapter);
    }
    
    

}
