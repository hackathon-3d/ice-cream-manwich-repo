package com.icm;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class QuickHistoryLoader extends AsyncTaskLoader<QuickHistory> {

	private final int userid;
	
	public QuickHistoryLoader(Context context, int userid) {
		super(context);
		this.userid = userid;
	}

	@Override
	public QuickHistory loadInBackground() {
		
		
		
		
		// TODO Auto-generated method stub
		return null;
	}

}
