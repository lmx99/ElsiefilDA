package com.lifeisle.jekton.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author Jekton Luo
 * @version 0.01 6/14/2015.
 */
public class DimensionUtils {

    private DimensionUtils() {
        throw new AssertionError("Can not instantiate this class.");
    }



    public static int dp2px(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float scale = metrics.density;
        return (int) (dp * scale + 0.5);
    }

}
