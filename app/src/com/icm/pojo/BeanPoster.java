package com.icm.pojo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

public final class BeanPoster {

	public interface Callback<T> {
		public void beanPosted(T bean);
	}
	
	private BeanPoster(){}
	
	public static <T> void postBean(
			final Class<T> beanClass, 
			final String urlString, 
			final Bundle postBody,
			final Callback<T> callback) 
	{
		new AsyncTask<Void, Void, T>() {

			@Override
			protected T doInBackground(Void... params) {
				try 
				{
					return postData(beanClass, postBody);
				} 
				catch (MalformedURLException e) 
				{
					Log.e("BeanLoader", "Exception loading bean", e);
				}
				catch (IOException e) 
				{
					Log.e("BeanLoader", "Exception loading bean", e);
				}

				return null;
			}
			
			@Override
			protected void onPostExecute(T result) {
				if (result != null) {
					callback.beanPosted(result);
				}
			}
		}.execute();
	}
	
	
	private static <T> T postData(Class<T> beanClass, Bundle postBody) throws ClientProtocolException, IOException {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://www.yoursite.com/api/TripLocker");

	    // Add your data
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    
	    for(String key : postBody.keySet())
	    {
	    	String value = postBody.getString(key);
	    	
	    	if(value == null)
	    	{
	    		Log.e("BeanPoster", "Value in postBody was not a string", new Exception());
	    		continue;
	    	}
	    	
	    	nameValuePairs.add(new BasicNameValuePair(key, value));
	    }
	    
	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	    // Execute HTTP Post Request
	    HttpResponse response = httpclient.execute(httppost);
	    
	    
		InputStream is = response.getEntity().getContent();
		InputStreamReader reader = new InputStreamReader(is);
		return new Gson().fromJson(reader, beanClass);

	}
}