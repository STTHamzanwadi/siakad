package org.pdtyph.cummon;

import com.vaadin.data.Validator;

@SuppressWarnings("serial")
public class ValidatorEmailAdd implements Validator{

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
			if (!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
				throw new InvalidValueException("The e-mail address provided is not valid!");

		}
	}
}