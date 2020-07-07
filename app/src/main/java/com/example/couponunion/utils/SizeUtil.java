package com.example.couponunion.utils;

import android.content.Context;
import android.util.TypedValue;

public class SizeUtil {
    public static int dp2px(Context context, int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }
}
