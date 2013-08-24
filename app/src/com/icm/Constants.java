package com.icm;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Constants {

    private static final String SERVER_URL = "http://rendaz.pythonanywhere.com/bookswithfriends/";
    public static final String NEW_USER_URL = SERVER_URL + "user";
	
	// POST parameters. please do not muck with them.
    public static final String NAME = "name/";
	public static final String PHONE = "phone/";
	
	public static final String newUserPostUrl(String name, String phone){
	    return NEW_USER_URL; // + name + "/" + phone + "phone";
	}
	
    public static final String newUserPostUrl(String phone){
        return NEW_USER_URL; //  + phone + "phone";
    }

    
    public static String getDevicePhoneNumber(Context context){
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = manager.getLine1Number();
        return phoneNumber;
    }

    
}