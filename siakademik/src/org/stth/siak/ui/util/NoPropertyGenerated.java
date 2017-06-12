package org.stth.siak.ui.util;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertyValueGenerator;

public class NoPropertyGenerated extends PropertyValueGenerator<Integer>{
	private static final long serialVersionUID = 1L;
	private Integer rowNumber;
	
	public NoPropertyGenerated() {
		rowNumber=1;
	}

	@Override
	public Integer getValue(Item item, Object itemId, Object propertyId) {
		// TODO Auto-generated method stub
		return rowNumber++;
	}

	@Override
	public Class<Integer> getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}
	
	

}
