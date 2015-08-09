package com.lifeisle.jekton.util.validator;

/**
 * A interface that to be implemented to validate some data.<br />
 *
 * @author Jekton
 * @version 0.01 7/20/2015
 */
public interface Validator {

    /**
     * validating the data
     * @return <code>true</code> if valid, otherwise, return <code>false</code>.
     */
    boolean validate();

    /**
     * method that can be call to get an error message to indicate that the data inputted are invalid.
     * @return a {@link String} that describes why the input data are invalid.
     */
    String getErrorMessage();
}
