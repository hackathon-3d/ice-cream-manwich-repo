package com.icm;

import roboguice.inject.InjectResource;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

public class DashboardActivity extends RoboSherlockActivity {

    @InjectResource(R.string.loan_title)        String lOAN_TITLE;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
	}

	public void onLoanButtonClick(View view) {
		Intent intent = new Intent(this, LoanBorrowActivity.class);
		intent.putExtra(LoanBorrowActivity.TITLE_INTENT, lOAN_TITLE);
		startActivity(intent);
	}
	
	public void onBorrowButtonClick(View view) {
		
	}
	
	public void onHistoryButtonClick(View view) {
		
	}
	
	public void onLibraryButtonClick(View view) {
		
	}

}
