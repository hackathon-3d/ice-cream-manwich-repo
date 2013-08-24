package com.icm.products;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.icm.ProductInfo;
import com.icm.bean.google.ImageBean;
import com.icm.bean.google.ProductItem;
import com.icm.bean.google.ProductSearchResult;
import com.icm.pojo.BeanLoader;

public class FutureProductInfo implements Future<ProductInfo>, BeanLoader.Callback<ProductSearchResult>
{

	private final Map<String,ProductInfo> products;
	private final String barcodeNumber;
	
	private ProductInfo info = null;
	private boolean cancelled = false;
	
	public FutureProductInfo(Context context, Map<String,ProductInfo> products,
			String barcodeNumber) {
		
		this.products = products;
		this.barcodeNumber = barcodeNumber;
		
		info = products.get(barcodeNumber);
		if(info != null)
			return;
		
		Uri.Builder builder = new Uri.Builder();
		
		builder.scheme("https");
		
		builder.path(ProductInfoManager.BASE_URL);
		builder.appendQueryParameter("key", ProductInfoManager.API_KEY);
		builder.appendQueryParameter("country", ProductInfoManager.COUNTRY);
		builder.appendQueryParameter("q", barcodeNumber);
		
		Uri uri = builder.build();
				
		BeanLoader.loadBean(ProductSearchResult.class, uri.toString(), this);
		
		Log.i("FutureProductInfo", "Fetching " + uri.toString());
	}

	@Override
	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		cancelled = true;
		return false;
	}

	@Override
	public synchronized ProductInfo get() throws InterruptedException,
			ExecutionException {
		
		if(info == null)
		{
			this.wait(10000);
		}
		
		Log.i("FutureProductInfo", "Getting " + barcodeNumber + " result " + info);
		
		products.put(barcodeNumber, info);
		return info;
	}

	@Override
	public ProductInfo get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException,
			TimeoutException {
		
		return get(); // TODO
	}

	@Override
	public boolean isCancelled() {
		return (info == null) && (cancelled);
	}

	@Override
	public boolean isDone() {
		return (cancelled) || (info != null);
	}

	@Override
	public synchronized void beanLoaded(ProductSearchResult searchResult) {
		
		List<ProductItem> resultList = searchResult.items;
		
		if(resultList == null)
		{
			Log.e("FutureProductInfo", "resultList null in response: " + barcodeNumber);
			this.notifyAll();
			return;
		}
		
		if(resultList.isEmpty())
		{
			Log.e("FutureProductInfo", "resultList empty: " + barcodeNumber);
			this.notifyAll();
			return;
		}
		
		Log.i("FutureProductInfo", "Got " + resultList.size() + " results: " + barcodeNumber);
		
		for(ProductItem item : resultList)
		{
			if(item.description == null)
				continue;
			if(item.images == null)
				continue;
			
			ProductInfo info = new ProductInfo();
			
			info.name = item.description;
			info.barcodeNumber = barcodeNumber;
			
			Log.i("FutureProductInfo", "item " + info.name + " barcode: " + barcodeNumber);
			
			for(ImageBean image : item.images)
			{
				if(image.link == null)
					continue;
				
				try 
				{
					info.imageUrl = new URL(image.link);
				} catch (MalformedURLException e) {
					Log.w("FutureProductInfo", "Unable to parse url in response", e);
				}
			}
			
			if(info.imageUrl == null)
				continue;
		
			
			this.info = info;
			this.notifyAll();
			return;
		}
		
		
		this.notifyAll();
	}
	
}
