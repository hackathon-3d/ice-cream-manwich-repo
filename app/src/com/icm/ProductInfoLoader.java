package com.icm;

import android.content.Context;
import android.net.Uri;

import com.icm.pojo.BeanLoader;

public abstract class ProductInfoLoader extends BeanLoader<ProductInfo> {

	public static final String BASE_URL = "https://www.googleapis.com/shopping/search/v1/public/products";
	
	public static final String API_KEY = "AIzaSyDLbWae-NdyvQUNhXPzYQSQUUmH_o0IFp8";
	public static final String COUNTRY = "US";

	public final String barcodeNumber;
	
	public ProductInfoLoader(Context context, String barcodeNumber) {
		super(context, ProductInfo.class, getURL(barcodeNumber));
		
		this.barcodeNumber = barcodeNumber;
	}
	
	public static String getURL(String barcodeNumber)
	{
		Uri.Builder builder = new Uri.Builder();
		
		builder.path(BASE_URL);
		builder.appendQueryParameter("key", API_KEY);
		builder.appendQueryParameter("country", COUNTRY);
		builder.appendQueryParameter("q", barcodeNumber);
		
		Uri uri = builder.build();
		return uri.toString();
	}

	@Override
	public abstract void deliverResult(ProductInfo item);


}
