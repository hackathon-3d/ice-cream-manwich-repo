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
		
		String uri = "https://" + ProductInfoManager.BASE_URL;
		uri += "?key=" + ProductInfoManager.API_KEY;
		uri += "&country=" + ProductInfoManager.COUNTRY;
		uri += "&q=" + barcodeNumber;
				
		BeanLoader.loadBean(ProductSearchResult.class, uri, this);
		
		Log.i("FutureProductInfo", "Fetching " + uri);
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
		ProductInfo info = this.info;
		
		Log.i("FutureProductInfo", "Getting " + barcodeNumber + " result " + info);
		
		if(info == null)
			return null;
		
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
			if(item.product == null)
				continue;
			
			Log.i("FutureProductInfo", "Item " + item.product.title + " images " + item.product.images);
			
			if(item.product.title == null)
				continue;
			if(item.product.images == null)
				continue;
			
			ProductInfo info = new ProductInfo();
			
			info.name = item.product.title;
			info.barcodeNumber = barcodeNumber;
			
			for(ImageBean image : item.product.images)
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
