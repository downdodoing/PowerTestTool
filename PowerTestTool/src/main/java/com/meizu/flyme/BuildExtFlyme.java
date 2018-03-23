package com.meizu.flyme;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BuildExtFlyme {

    public static final Boolean IS_MX2 = isMX("IS_MX2");
    public static final Boolean IS_MX3 = isMX("IS_MX3");
    public static final Boolean IS_MX4 = isMX("IS_MX4");           //m75
    public static final Boolean IS_MX4_Pro = isMX("IS_MX4_Pro");   //m76
    public static final Boolean IS_M1_NOTE = isMX("IS_M1_NOTE");   //m71
    public static final Boolean IS_M1 = isMX("IS_M1");             //m79
    public static final Boolean IS_POWERXTEND = isPowerXtendPreviousProduct();
    public static final Boolean IS_POWERXTEND_FOR_M = isPowerXtendProductForM();



    public static boolean CUSTOMIZE_CHINAMOBILE = isCustomizeChinaMobile();

    private static Boolean isMX(String is) {
        try {
            Class<?> c = Class.forName("android.os.BuildExt");
            Field field = c.getDeclaredField(is);
            field.setAccessible(true);
            Object object = field.get(c);
            return (Boolean)object;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isProductInternational(){
        try {
            Class<?> c = Class.forName("android.os.BuildExt");
            Method method = c.getDeclaredMethod("isProductInternational");
            method.setAccessible(true);
            return (Boolean) method.invoke(c);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isCustomizeChinaMobile() {
        try {
            Class<?> c = Class.forName("android.os.BuildExt");
            Field field = c.getDeclaredField("CUSTOMIZE_CHINAMOBILE");
            field.setAccessible(true);
            Object object = field.get(c);
            return (Boolean) object;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String getCTA() {
        try {
            Class<?>c = Class.forName("android.os.BuildExt");
            Field fieldCTA = c.getDeclaredField("CTA");
            fieldCTA.setAccessible(true);
            Object objectCTA = fieldCTA.get(c);
            return (String) objectCTA;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static Boolean isPowerXtendPreviousProduct(){
        try {
            System.load("libPowerStretch64.so");
            return true;
        } catch (UnsatisfiedLinkError var13) {
            Log.e("PowerModeManager", "not PowerXtend Previous Product");
        }

        return false;
    }

    private static Boolean isPowerXtendProductForM(){
        if (IS_POWERXTEND) {
            return false;
        }

        try {
            System.load("libPowerStretch.so");
            return true;
        } catch (UnsatisfiedLinkError var13) {
            Log.e("PowerModeManager", "not PowerXtend for android m Product");
        }
        return false;
    }




}
