package com.icm.pojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public final class BeanLoader {
	
	public interface Callback<T> {
		public void beanLoaded(T bean);
	}
	
	private BeanLoader(){}
	
	public static <T> void loadBean(
			final Class<T> beanClass, 
			final String urlString, 
			final Callback<T> callback) 
	{
		new AsyncTask<Void, Void, T>() {

			@Override
			protected T doInBackground(Void... params) {
				try 
				{
					
					InputStream is = new URL(urlString).openStream();
					InputStreamReader reader = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(reader);
					
					String whole = "";
				    try
				    {
						String next = br.readLine();
						while(next != null)
						{
							whole += next;
							next = br.readLine();
						}
						
						return new Gson().fromJson(whole, beanClass);
				    }
				    catch(JsonParseException e)
				    {
				    	Log.e("BeanLoader", "Bad JSON: " + whole);
				    	throw e;
				    }
				    finally
				    {
				    	if(br != null)
				    		br.close();
				    }

				} 
				catch (MalformedURLException e) 
				{
					Log.e("BeanLoader", "Malformed URL for bean", e);
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
					callback.beanLoaded(result);
				}
			}
		}.execute();
	}
}



