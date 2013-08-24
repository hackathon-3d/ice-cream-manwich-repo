package com.icm;

import roboguice.inject.InjectResource;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.icm.bean.PersonBean;
import com.icm.pojo.BeanLoader;
import com.icm.pojo.BeanPoster;

public class DashboardActivity extends RoboSherlockActivity implements LoaderCallbacks<QuickHistory>{

    @InjectResource(R.string.loan_title)        String LOAN_TITLE;
    @InjectResource(R.string.borrow_title)      String BORROW_TITLE;
    @InjectResource(R.string.submit_loan_text)  String LOAN_THIS_BOOK;
    @InjectResource(R.string.submit_borrow_text)String BORROW_THIS_BOOK;
    
	private static final int LOADER_QUICK_HISTORY = 1;
	private static final int LOADER_MY_USERNAME = 2;
	
	
	private String phonenumber;
	private String userid;
	
	QuickHistoryAdapter quickHistoryAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		
		// Get the username from the phone number
		phonenumber = Constants.getDevicePhoneNumber(DashboardActivity.this);
		String url = Constants.newUserPostUrl("555-555-1234");
		
		Bundle postBody = new Bundle();
		postBody.putString("name", "Big Mike");
		postBody.putString("phone", "555-555-1234");
		
		BeanPoster.postBean(PersonBean.class, url, postBody, new BeanPoster.Callback<PersonBean>() {
			@Override
			public void beanPosted(PersonBean bean) {
				
				DashboardActivity.this.userid = bean.user_id;
				
				getLoaderManager().initLoader(LOADER_QUICK_HISTORY, null, DashboardActivity.this);
			}
		});
		
		
		quickHistoryAdapter = new QuickHistoryAdapter(this);
		
		ListView historyView = (ListView) findViewById(R.id.quickHistory);
		
		historyView.setAdapter(quickHistoryAdapter);
		
		
		
	}

	public void onLoanButtonClick(View view) {
		Intent intent = new Intent(this, LoanBorrowActivity.class);
		intent.putExtra(LoanBorrowActivity.TITLE_INTENT_EXTRA, LOAN_TITLE);
		intent.putExtra(LoanBorrowActivity.SUBMIT_INTENT_EXTRA, LOAN_THIS_BOOK);
		startActivity(intent);
	}
	
	public void onBorrowButtonClick(View view) {
	    Intent intent = new Intent(this, LoanBorrowActivity.class);
        intent.putExtra(LoanBorrowActivity.TITLE_INTENT_EXTRA, BORROW_TITLE);
        intent.putExtra(LoanBorrowActivity.SUBMIT_INTENT_EXTRA, BORROW_THIS_BOOK);
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
		quickHistoryAdapter.notifyDataSetChanged();
		
	}

	@Override
	public void onLoaderReset(Loader<QuickHistory> loader) {

		quickHistoryAdapter.clear();
		quickHistoryAdapter.notifyDataSetChanged();
		
		
	}

}
