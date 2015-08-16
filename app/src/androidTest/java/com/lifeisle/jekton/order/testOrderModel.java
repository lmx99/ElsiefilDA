package com.lifeisle.jekton.order;

import android.test.AndroidTestCase;

/**
 * @author Jekton
 */
public class testOrderModel extends AndroidTestCase {

    public void testOrderCodeValidator() {
        String[] orderCodes = {
                "123456789012",
                "1234567890123",
                "12345678901234",
                "123456789012345",
                "1234567890123456",
                "12345678901234567",
                "123456789012345678",
                "1234567890123456789",

                "1234a567890123",
        };
        boolean[] expects = {
                false,
                true,
                true,
                true,
                true,
                true,
                true,
                false,
                false,
        };

        for (int i = 0; i < orderCodes.length; ++i) {
            assertEquals(orderCodes[i].matches("\\d{13,18}"), expects[i]);
        }
    }
}
