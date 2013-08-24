package com.icm.pojo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public abstract class BeanLoader<T> extends AsyncTaskLoader<T> {
	
	private final Class<T> beanClass;
	public final String urlString;
	
	public BeanLoader(Context context, Class<T> beanClass, String urlString)
	{
		super(context);
		this.beanClass = beanClass;
		this.urlString = urlString;
	}
	
	@Override
	public T loadInBackground() 
	{
		
		try {
			InputStream is = new URL(urlString).openStream();
			InputStreamReader reader = new InputStreamReader(is);
			return new Gson().fromJson(reader, beanClass);
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
	public abstract void deliverResult(T item);
	
}

