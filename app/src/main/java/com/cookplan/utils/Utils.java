package com.cookplan.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public class Utils {

    public static void log(String tag, String textLog) {
        Log.d(tag, textLog);
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
