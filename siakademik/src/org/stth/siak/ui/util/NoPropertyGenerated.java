package org.stth.siak.ui.util;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.PropertyValueGenerator;

public class NoPropertyGenerated extends PropertyValueGenerator<Integer>{
	private static final long serialVersionUID = 1L;
	private BeanItemContainer<?> bi;
	public NoPropertyGenerated(BeanItemContainer<?> bi) {
	this.bi=bi;
	}

	@Override
	public Integer getValue(Item item, Object itemId, Object propertyId) {
		return bi.indexOfId(itemId)+1;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
	
	

}
