package com.example.couponunion.utils;

import android.util.Log;

public class LogUtil {
    //调试信息工具类

    private static int current_lev = 4;
    private static final int DEBUG_LEV = 4;
    private static final int INFO_LEV = 3;
    private static final int WARNING_LEV = 2;
    private static final int ERROR_LEV = 1;


    public static void d(Object obj, String msg) {
        if (current_lev >= DEBUG_LEV) {
            Log.d(obj.getClass().getSimpleName(), msg);
        }
    }

    public static void i(Object obj, String msg) {
        if (current_lev >= INFO_LEV) {
            Log.i(obj.getClass().getSimpleName(), msg);
        }
    }

    public static void w(Object obj, String msg) {
        if (current_lev >= WARNING_LEV) {
            Log.w(obj.getClass().getSimpleName(), msg);
        }
    }

    public static void e(Object obj, String msg) {
        if (current_lev >= ERROR_LEV) {
            Log.e(obj.getClass().getSimpleName(), msg);
        }
    }
}
