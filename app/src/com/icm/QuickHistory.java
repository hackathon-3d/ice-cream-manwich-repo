package com.icm;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.icm.bean.EventBean;

public class QuickHistory {

	public class Item
	{
		public URL image;
		
		public String name;
		public String person;
		
		public String date;
	}
	
	private List<EventBean> events;
	
	
	public QuickHistory(List<EventBean> events)
	{
		this.events = events;
		
		
	}
	
	
	
	public Item getItem(int index)
	{
		
		EventBean bean = events.get(index);
		
		Item item = new Item();
		
		item.image = null;
		item.person = bean.person.name;
		item.name = bean.item;
		item.date = bean.date;
		
		return item;
	}
	
	public int size()
	{
		return events.size();
	}


	public Collection<Item> getCollection() {
		
		List<Item> items = new ArrayList<Item>();
		for(int i=0; i<size(); ++i)
		{
			items.add(getItem(i));
		}
		
		return items;
	}
	
	
	
	
}
