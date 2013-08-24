package com.icm;

import java.net.URL;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

public class ProductInfoLoader extends AsyncTaskLoader<ProductInfo> {

	public static final String BASE_URL = "https://www.googleapis.com/shopping/search/v1/public/products";
	
	public static final String API_KEY = "AIzaSyDLbWae-NdyvQUNhXPzYQSQUUmH_o0IFp8";
	public static final String COUNTRY = "US";

	public final String barcodeNumber;
	
	public ProductInfoLoader(Context context, String barcodeNumber) {
		super(context);
		
		this.barcodeNumber = barcodeNumber;
	}

	@Override
	public ProductInfo loadInBackground() {
		
		URL url = new URL(BASE_URL);
		
		Uri.Builder builder = new Uri.Builder();
		
		builder.path(BASE_URL);
		builder.appendQueryParameter("key", API_KEY);
		builder.appendQueryParameter("country", COUNTRY);
		builder.appendQueryParameter("q", barcodeNumber);
		
		
		
		// TODO Auto-generated method stub
		return null;
	}


}
