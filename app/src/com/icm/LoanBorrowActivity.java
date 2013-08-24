package com.icm;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

@ContentView(R.layout.activity_loan_borrow)
public class LoanBorrowActivity extends RoboSherlockActivity {

    public static final String TITLE_INTENT = "title";
    private static final int PICK_CONTACT = 1234;

    @InjectExtra(TITLE_INTENT)              String title;
    @InjectView(R.id.contact_name_input)    EditText contactTextView;
    @InjectView(R.id.due_date_edit_text)    EditText dueDateTextView;
    @InjectView(R.id.scan_button)           Button scanButton;
    
    private String contactName;    
    private String contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        contactTextView.setOnClickListener(contactClickListener);
        scanButton.setOnClickListener(scanButtonClickListener);
    }

    private final View.OnClickListener contactClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    };

    private final View.OnClickListener scanButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // fire something that scans a barcode
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Nope", requestCode + " " + resultCode);

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
    }
}
