package com.icm;

import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.icm.products.ProductInfoManager;



@ContentView(R.layout.activity_loan_borrow)
public class LoanBorrowActivity extends RoboSherlockActivity {

    public static final String TITLE_INTENT = "title";
    private static final int PICK_CONTACT_INTENT  = 1234; // arbitrary!
    
    @InjectExtra(TITLE_INTENT)              String title;
    @InjectResource(R.string.share_text)    String shareText;
    @InjectView(R.id.contact_name_input)    EditText contactTextView;
    @InjectView(R.id.dueDatePicker)         DatePicker dueDatePicker;
    @InjectView(R.id.media_editable_title)  EditText barcodeTextView;
    @InjectView(R.id.scan_button)           Button scanButton;
    @InjectView(R.id.loan_borrow_book_image)ImageView bookImageView;
    
    private String contactName;    
    private String contactNumber;  
    private String barcodeString;
    
    private ProductInfoManager productInfoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        contactTextView.setOnClickListener(contactClickListener);
        scanButton.setOnClickListener(scanButtonClickListener);
        productInfoManager = new ProductInfoManager(this); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem shareMenuItem = menu.add(shareText);
        shareMenuItem.setOnMenuItemClickListener(submitButtonClickListener);
        return true;
    }
    
    private final View.OnClickListener contactClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT_INTENT);
        }
    };

    private final View.OnClickListener scanButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentIntegrator integrator = new IntentIntegrator(LoanBorrowActivity.this);
            integrator.initiateScan();
        }
    };

    private final MenuItem.OnMenuItemClickListener submitButtonClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            postStuff(contactNumber);
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Nope", requestCode + " " + resultCode);

        switch(requestCode){
        case PICK_CONTACT_INTENT: 
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData, null, null, null, null);
                try {
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equals("1")) {
                            Cursor phones = getContentResolver().query(
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            phones.moveToFirst();
                            contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contactName = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                            final String contactTextForDisplay = String.format("%s (%s)", contactName, contactNumber);
                            contactTextView.setText(contactTextForDisplay);
                        }
                    }
                } catch (Exception e) {
                    Log.e("NOPE", e.getMessage());
                } finally {
                    if (c != null) { // and it shouldn't be null!
                        c.close();
                    }
                }
            }
            break; //break dat code;
        default: // cause xzing library doesn't allow an activity request code in its library
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (scanResult != null) { // don't be dumb user!
                    barcodeString = scanResult.getContents();
                    barcodeTextView.setText(barcodeString);
                    
                    loadBarcodeInformation();
                }
            break;
        }
    }
    
    private void loadBarcodeInformation() {
		// TODO Auto-generated method stub
    	
    	Future<ProductInfo> productInfo = productInfoManager.getProductInfo(barcodeString);
    	
    	
		
	}

	public void postStuff(String phoneNumber){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Constants.newUserPostUrl(phoneNumber));

        try{
            // no post params?
            HttpResponse response = httpClient.execute(httpPost);
        } catch(Exception e){
            
        }
    }
    
    public String getDevicePhoneNumber(){
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = manager.getLine1Number();
        return phoneNumber;
    }
    
    private void setBookImage(String url){

    }
}
