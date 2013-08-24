package com.icm;

import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class DashboardActivity extends Activity implements LoaderCallbacks<QuickHistory>{

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}
	
	public void onLoanButtonClick(View view) {
		
	}
	
	public void onBorrowButtonClick(View view) {
		
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

		quickHistoryAdapter.swapContent(data);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<QuickHistory> loader) {
		// TODO Auto-generated method stub
		
		quickHistoryAdapter.swapContent(null);
		
		
	}

}
