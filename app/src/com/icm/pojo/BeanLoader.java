package com.icm.pojo;

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
					
				    try
				    {
						return new Gson().fromJson(reader, beanClass);
				    }
				    catch(JsonParseException e)
				    {
				    	Log.e("BeanLoader", "Bad JSON: " + e.getMessage());
				    	throw e;
				    }
				    finally
				    {
				    	if(reader != null)
				    		reader.close();
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



