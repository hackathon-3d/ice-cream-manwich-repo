package com.icm.bean;

public class LoanBean {

	public String name;
	public String category;
	public String dueDate;
	public transient String person; //transient because I don't want to mess up the server. delete 'transient' if you want.
	
	public String imageURL;
	
	
}
