package org.stth.siak.ui.util;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertyValueGenerator;

public class StringPropertyGenerated extends PropertyValueGenerator<String>{
	private static final long serialVersionUID = 1L;
	private String label;
	
	public StringPropertyGenerated(String label) {
		this.label=label;
	}

	@Override
	public String getValue(Item item, Object itemId, Object propertyId) {
		// TODO Auto-generated method stub
		return label;
	}

	@Override
	public Class<String> getType() {
		// TODO Auto-generated method stub
		return String.class;
	}
	
	

}
