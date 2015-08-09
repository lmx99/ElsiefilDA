package com.lifeisle.jekton.util.validator;


import com.lifeisle.android.R;
import com.lifeisle.jekton.util.StringUtils;

/**
 * @author Jekton
 * @version 0.01 7/20/2015
 */
public class PasswordValidator implements Validator {

    private String password0;
    private String password1;

    public PasswordValidator(String password0, String password1) {
        this.password0 = password0;
        this.password1 = password1;
    }


    @Override
    public boolean validate() {
        return password0.length() == password1.length()
                && 6 <= password0.length() && password0.length() <= 16
                && password0.equals(password1);
    }

    @Override
    public String getErrorMessage() {
        if (password0.length() == password1.length()
                && password0.length() <= 6 && 16 <= password0.length()) {
            return StringUtils.getStringFromResource(R.string.error_pwd_length);
           
        }
        return StringUtils.getStringFromResource(R.string.error_pwd_unmatched);
    }
}
