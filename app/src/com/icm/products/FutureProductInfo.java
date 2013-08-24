package com.icm.products;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.net.Uri;

import com.icm.ProductInfo;
import com.icm.pojo.BeanLoader;

public class FutureProductInfo implements Future<ProductInfo>, BeanLoader.Callback<ProductInfo>
{

	private final Map<String,ProductInfo> products;
	private final String barcodeNumber;
	
	private ProductInfo info = null;
	private boolean cancelled = false;
	
	public FutureProductInfo(Context context, Map<String,ProductInfo> products,
			String barcodeNumber) {
		
		this.products = products;
		this.barcodeNumber = barcodeNumber;
		
		Uri.Builder builder = new Uri.Builder();
		
		builder.path(ProductInfoManager.BASE_URL);
		builder.appendQueryParameter("key", ProductInfoManager.API_KEY);
		builder.appendQueryParameter("country", ProductInfoManager.COUNTRY);
		builder.appendQueryParameter("q", barcodeNumber);
		
		Uri uri = builder.build();
				
		BeanLoader.loadBean(ProductInfo.class, uri.toString(), this);
	}

	@Override
	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		cancelled = true;
		return false;
	}

	@Override
	public synchronized ProductInfo get() throws InterruptedException,
			ExecutionException {
		
		int timeout = 10;
		
		if((info == null) && (timeout > 0))
		{
			this.wait(1000);
			--timeout;
		}
		
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
	public synchronized void beanLoaded(ProductInfo item) {
		products.put(barcodeNumber,item);
		
		this.info = item;
		this.notifyAll();
	}
	
}
