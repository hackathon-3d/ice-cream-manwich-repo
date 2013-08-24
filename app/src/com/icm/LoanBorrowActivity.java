package com.icm;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.icm.bean.LoanBean;
import com.icm.bean.PersonBean;
import com.icm.pojo.BeanPoster;
import com.icm.products.ProductInfoManager;
import com.nostra13.universalimageloader.core.ImageLoader;



@ContentView(R.layout.activity_loan_borrow)
public class LoanBorrowActivity extends RoboSherlockActivity {

    public static final String TITLE_INTENT_EXTRA = "title";
    public static final String SUBMIT_INTENT_EXTRA = "submit";
    private static final int PICK_CONTACT_INTENT  = 1234; // arbitrary!
    
    @InjectExtra(TITLE_INTENT_EXTRA)        String title;
    @InjectExtra(SUBMIT_INTENT_EXTRA)       String submitText;
    @InjectView(R.id.contact_name_input)    EditText contactTextView;
    @InjectView(R.id.dueDatePicker)         DatePicker dueDatePicker;
    @InjectView(R.id.media_editable_title)  EditText barcodeTextView;
    @InjectView(R.id.scan_button)           Button scanButton;
    @InjectView(R.id.loan_borrow_book_image)ImageView bookImageView;
    @InjectView(R.id.manual_scan_button)    Button manualScanButton; 
    
    private String contactName;    
    private String contactNumber;  
    private String barcodeString;
    
	protected String imageURL;

    
    public int userid;
    
    private ProductInfoManager productInfoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(title);
        contactTextView.setOnClickListener(contactClickListener);
        scanButton.setOnClickListener(scanButtonClickListener);
        manualScanButton.setOnClickListener(manualScanButtonClickListener);
        productInfoManager = new ProductInfoManager(this); 
        
        getMyUserId();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem submitMenuItem = menu.add(submitText);
        submitMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        submitMenuItem.setOnMenuItemClickListener(submitButtonClickListener);
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
    
    private final View.OnClickListener manualScanButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	
        	barcodeString = barcodeTextView.getText().toString();
        	
            loadBarcodeInformation();
        }
    };


    private final MenuItem.OnMenuItemClickListener submitButtonClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            postStuff();
            // hopefully after the post, we do
            // onBackPressed
            // and return the user to the main screen with an updated due book list.
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
                    scanButton.setVisibility(View.GONE);
                    barcodeTextView.setVisibility(View.VISIBLE);
                    
                    barcodeString = scanResult.getContents();
                    barcodeTextView.setText(barcodeString);
                    
                    loadBarcodeInformation();
                }
            break;
        }
    }
    
    private void getMyUserId()
    {
		// Get the username from the phone number
		String phonenumber = Constants.getDevicePhoneNumber(this);
		String url = Constants.newUserPostUrl(phonenumber);
		
		Bundle postBody = new Bundle();
		postBody.putString("name", "Big Mike");
		postBody.putString("phone", phonenumber);
		postBody.putString("email", "bigmike@bigmike.com");
		
		BeanPoster.postBean(PersonBean.class, url, postBody, new BeanPoster.Callback<PersonBean>() {
			@Override
			public void beanPosted(PersonBean bean) {
				
				LoanBorrowActivity.this.userid = bean.user_id;
				
				Log.i("LoanBorrowActivity", "My user id is " + bean.user_id);
			}
		});
		

    }
    
    private void loadBarcodeInformation() {
		// TODO Auto-generated method stub
    	
    	final Future<ProductInfo> productInfo = productInfoManager.getProductInfo(barcodeString);
    	
    	new AsyncTask<Void,Void,ProductInfo>() {

    		@Override
			protected ProductInfo doInBackground(Void... params) {
				
		    	try {
					return productInfo.get();
										
				} catch (InterruptedException e) {
					Log.e("LoanBorrowActivity", "Interrupted", e);
				} catch (ExecutionException e) {
					Log.e("LoanBorrowActivity", "Execution exception", e);
				}
		    	return null;
			}

			@Override
			protected void onPostExecute(ProductInfo result) {
				
				Log.i("LoanBorrowActivity", "Barcode info " + result);
				
				if(result == null)
					return;
				
				Log.i("LoanBorrowActivity", "Barcode name " + result.name);
				
				barcodeTextView.setText(result.name);
				
				LoanBorrowActivity.this.imageURL = result.imageUrl.toString();
				
				ImageLoader.getInstance().displayImage(result.imageUrl.toString(), bookImageView);
				
				super.onPostExecute(result);
			}
			
			
    	}.execute();
    	
    
		
	}

	public void postStuff(){
		if(this.contactNumber == null)
			contactNumber = "555-555-5555";
		if(this.contactName == null)
			contactName = "Mike";
		
		
		Log.i("LoanBorrowActivity", "Getting friend id " + this.contactNumber);
		
		// Create a username for the friend
		String url = Constants.newUserPostUrl(this.contactNumber);
		
		Bundle postBody = new Bundle();
		postBody.putString("name", this.contactName);
		postBody.putString("phone", this.contactNumber);
		postBody.putString("email", "noone@example.com");
		
		BeanPoster.postBean(PersonBean.class, url, postBody, new BeanPoster.Callback<PersonBean>() {
			@Override
			public void beanPosted(PersonBean bean) {
				
				Log.i("LoanBorrowActivity", "Friend ID is " + bean.user_id);
				int friendId = bean.user_id;
				
				
				postLoanStuff(friendId);
			}
		});
		
    }
    
    protected void postLoanStuff(int friendId) {
		Log.i("LoanBorrowActivity", "posting new loan stuff");
    	
    	String fromId = Integer.toString(this.userid);
		String toId = Integer.toString(friendId);
		
		LoanBean bean = new LoanBean();
		bean.name = barcodeTextView.getText().toString();
		bean.category = "Book";
		bean.dueDate = dueDatePicker.getMonth() + "/" + dueDatePicker.getDayOfMonth() + "/" + dueDatePicker.getYear();
		bean.imageURL = this.imageURL;
		
		LoanPoster loanPoster = new LoanPoster(fromId, toId);
		
		loanPoster.execute(bean);
		
		this.finish();

	}

	public String getDevicePhoneNumber(){
        return Constants.getDevicePhoneNumber(this);
    }
    
    private void setBookImage(String url){

    }

}
