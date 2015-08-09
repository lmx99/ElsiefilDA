package com.lifeisle.jekton.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Jekton
 * @version 0.01 7/21/2015
 */
public class Toaster {

    private Toaster() {
        throw new AssertionError("Can not instantiate this class.");
    }

    public static void showShort(Context context, int msgID) {
        Toast.makeText(context, msgID, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}
