package com.icm;

import java.util.concurrent.atomic.AtomicInteger;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.icm.bean.LoanBean;
import com.icm.pojo.BeanPoster;

public class LoanPoster extends AsyncTask<LoanBean, Void, Void> implements BeanPoster.Callback<LoanBean>{
	
	private String fromId;
	private String toId;
	
	private AtomicInteger beanCount = new AtomicInteger();
	
	
	public LoanPoster(String fromId, String toId)
	{
		this.fromId = fromId;
		this.toId = toId;
	}
	
	@Override
	protected synchronized Void doInBackground(LoanBean... params) {
		
		
		for(LoanBean bean : params)
		{
			Bundle postBody = new Bundle();
			postBody.putString("name", bean.name);
			// postBody.putString("category", bean.category);
			postBody.putString("dueDate", bean.dueDate);
			postBody.putString("imageURL", bean.imageURL);
			
			
			String url = Constants.postNewLoanUrl(fromId, toId);
			
			BeanPoster.postBean(LoanBean.class, url, postBody, this);
			beanCount.addAndGet(1);
		}
		
		try 
		{
			if(beanCount.get() > 0)
				this.wait(10000);
		} catch (InterruptedException e)
		{
			Log.e("LoanPoster", "Waited too long", e);
		}
		
		
		return null;
	}

	@Override
	public synchronized void beanPosted(LoanBean bean) {
		// TODO Auto-generated method stub
		int count = beanCount.addAndGet(-1);
		
		if(count <= 0)
		{
			this.notifyAll();
		}
		
	}

}
