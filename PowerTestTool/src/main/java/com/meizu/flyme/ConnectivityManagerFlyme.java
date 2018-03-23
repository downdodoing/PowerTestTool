package com.meizu.flyme;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.Method;

public class ConnectivityManagerFlyme {

    public static void setMobileDataEnabled(Context context, boolean enabled){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Method method = cm.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);
            method.setAccessible(true);
            method.invoke(cm, enabled);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean getMobileDataEnabled(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Method method = cm.getClass().getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(cm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
