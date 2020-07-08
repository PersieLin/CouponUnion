package com.example.couponunion.utils;

import android.app.Application;
import android.widget.Toast;

import com.example.couponunion.base.BaseApplication;

public class ToastUtils {
    //使用同一个Toast对象显示信息，避免短时间产生多个对象从而长时间显示
    private static Toast mToast;

    public static void showToast(String tips){
        if (mToast != null) {
            mToast.setText(tips);
        } else {
            mToast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


}
