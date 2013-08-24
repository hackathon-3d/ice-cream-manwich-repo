package com.icm;

import roboguice.inject.InjectResource;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

public class DashboardActivity extends RoboSherlockActivity implements LoaderCallbacks<QuickHistory>{

    @InjectResource(R.string.loan_title)        String LOAN_TITLE;
    @InjectResource(R.string.borrow_title)      String BORROW_TITLE;
    
	private static final int LOADER_QUICK_HISTORY = 1;
	
	
	QuickHistoryAdapter quickHistoryAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		getLoaderManager().initLoader(LOADER_QUICK_HISTORY, null, this);
		
		quickHistoryAdapter = new QuickHistoryAdapter(this);
		
		ListView historyView = (ListView) findViewById(R.id.quickHistory);
		
		historyView.setAdapter(quickHistoryAdapter);
		
		
		
	}

	public void onLoanButtonClick(View view) {
		Intent intent = new Intent(this, LoanBorrowActivity.class);
		intent.putExtra(LoanBorrowActivity.TITLE_INTENT, LOAN_TITLE);
		startActivity(intent);
	}
	
	public void onBorrowButtonClick(View view) {
	    Intent intent = new Intent(this, LoanBorrowActivity.class);
        intent.putExtra(LoanBorrowActivity.TITLE_INTENT, BORROW_TITLE);
        startActivity(intent);
	}
	
	public void onHistoryButtonClick(View view) {
		
	}
	
	public void onLibraryButtonClick(View view) {
		
	}

	@Override
	public QuickHistoryLoader onCreateLoader(int id, Bundle args) {
		
		// Assume id  == LOADER_QUICK_HISTORY
		
		return new QuickHistoryLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<QuickHistory> loader, QuickHistory data) {

		quickHistoryAdapter.clear();
		quickHistoryAdapter.addAll(data.getCollection());
		
	}

	@Override
	public void onLoaderReset(Loader<QuickHistory> loader) {

		quickHistoryAdapter.clear();
		
		
	}

}
