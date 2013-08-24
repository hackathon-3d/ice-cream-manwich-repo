package com.icm;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import android.os.Bundle;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

@ContentView(R.layout.activity_loan_borrow)
public class LoanBorrowActivity extends RoboSherlockActivity {
    
    @InjectExtra("title") private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(title);
    }
}
