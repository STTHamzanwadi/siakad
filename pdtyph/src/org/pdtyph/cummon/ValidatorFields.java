package org.pdtyph.cummon;

import com.vaadin.data.Validator;

@SuppressWarnings("serial")
public class ValidatorFields implements Validator{

	public boolean isValid(Object value){
		try {

		} catch (InvalidValueException ive) {
			return false;
		}
		return true;
	}
	@Override
	public void validate(Object value) throws InvalidValueException {
		if (value instanceof String) {
			String email =(String) value;
			if (email=="" || email.isEmpty())
				throw new InvalidValueException("Empty");

		}
	}
}