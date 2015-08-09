package com.lifeisle.jekton.util.validator;


import com.lifeisle.android.R;
import com.lifeisle.jekton.util.StringUtils;

/**
 * @author Jekton
 * @version 0.01 7/21/2015
 */
public class PhoneNumberValidator implements Validator {

    private static final int PHONE_NUMBER_LENGTH = 11;

    private String phone;

    public PhoneNumberValidator(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean validate() {
        return phone.length() == PHONE_NUMBER_LENGTH;
    }
    

    @Override
    public String getErrorMessage() {
        return StringUtils.getStringFromResource(R.string.error_phone_invalid);
    }
}
