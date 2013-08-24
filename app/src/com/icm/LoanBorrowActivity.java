package com.icm;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

@ContentView(R.layout.activity_loan_borrow)
public class LoanBorrowActivity extends RoboSherlockActivity {
    
    public static final String TITLE_INTENT = "title"; 
    
    @InjectExtra(TITLE_INTENT)              String title;
    @InjectView(R.id.contact_name_input)    EditText contactTextView;
    @InjectView(R.id.due_date_edit_text)    EditText dueDateTextView;
    @InjectView(R.id.scan_button)           Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        contactTextView.setOnClickListener(contactClickListener);
        scanButton.setOnClickListener(scanButtonClickListener);
    }
    
    private final View.OnClickListener contactClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // fire something that gets the contact info.
        }
    };
    
    private final View.OnClickListener scanButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // fire something that scans a barcode
        }
    };
}
