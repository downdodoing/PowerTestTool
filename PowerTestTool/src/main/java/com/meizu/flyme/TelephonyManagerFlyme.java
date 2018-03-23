package com.meizu.flyme;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

public class TelephonyManagerFlyme {

    public static void setDataEnabled(Context context,boolean enable) {
        try {
            TelephonyManager tm = (TelephonyManager) ReflectUtils.getTelephonyManager(context);
            Method method = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            method.setAccessible(true);
            method.invoke(tm, enable);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
