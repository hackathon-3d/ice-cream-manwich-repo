package com.icm.products;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import android.content.Context;

import com.icm.ProductInfo;

public class ProductInfoManager {
	
	public static final String BASE_URL = "https://www.googleapis.com/shopping/search/v1/public/products";
	
	public static final String API_KEY = "AIzaSyDLbWae-NdyvQUNhXPzYQSQUUmH_o0IFp8";
	public static final String COUNTRY = "US";

	/**
	 * List is passed to FutureProductInfo, which populates with data.
	 */
	private final Map<String,ProductInfo> products = new ConcurrentHashMap<String, ProductInfo>();
	
	private final Context context;
	
	
	public ProductInfoManager(Context context)
	{
		this.context = context;
	}
	
	
	public Future<ProductInfo> getProductInfo(String barcodeNumber)
	{
		return new FutureProductInfo(context, products, barcodeNumber);
	}
	
	
}
