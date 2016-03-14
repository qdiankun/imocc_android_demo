package com.imocc.indicator;

import android.app.Activity;
import android.content.Context;

public class ScreenSizeUtil {

    public static int getScreenWidth(Activity activity) {
        return activity.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        return activity.getResources().getDisplayMetrics().heightPixels;
    }
}
