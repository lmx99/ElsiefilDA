package com.lifeisle.jekton.util.validator;


import com.lifeisle.android.R;
import com.lifeisle.jekton.util.StringUtils;

/**
 * @author Jekton
 * @version 0.01 7/20/2015
 */
public class UserNameValidator implements Validator {

    private String userName;

    public UserNameValidator(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean validate() {
        return userName.matches("^[a-z]\\w{2,15}");
    }

    @Override
    public String getErrorMessage() {

        if (userName.length() < 3 || 20 < userName.length()) {
            return StringUtils.getStringFromResource(R.string.error_user_name_length);
        }
        return StringUtils.getStringFromResource(R.string.error_user_name_rule);
    }
}

