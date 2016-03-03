package org.pdtyph.cummon;

import com.vaadin.data.validator.AbstractStringValidator;

@SuppressWarnings("serial")
public class ValidatorInteger extends AbstractStringValidator{
	
	public ValidatorInteger(String errorMessage) {
		super(errorMessage);
		
	}
  

    @Override
    protected boolean isValidValue(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (value != null && value instanceof Integer) {
            return;
        }

        super.validate(value);
    }

}
