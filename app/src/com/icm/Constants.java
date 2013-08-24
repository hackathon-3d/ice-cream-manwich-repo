package com.icm;

public class Constants {

    private static final String SERVER_URL = "rendaz.pythonanywhere.com/bookswithfriends/";
    public static final String NEW_USER_URL = SERVER_URL + "newUser/";
	
	// POST parameters. please do not muck with them.
    public static final String NAME = "name/";
	public static final String PHONE = "phone/";
	
	public static final String newUserPostUrl(String name, String phone){
	    return NEW_USER_URL + name + "/" + phone + "phone";
	}

}