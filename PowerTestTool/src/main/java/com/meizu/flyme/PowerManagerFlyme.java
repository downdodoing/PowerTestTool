package com.meizu.flyme;

import android.content.Context;
import android.os.PowerManager;


import java.lang.reflect.Method;


public class PowerManagerFlyme {
    public static final int BRIGHTNESS_ON = ReflectUtils.getStaticVariableInt("android.os.PowerManager", "BRIGHTNESS_ON");

    public static void setTemporaryScreenBrightnessSettingOverride(Context context, int brightness){
        try {
            Object object = ReflectUtils.getIPowerManager();
            Class c = object.getClass();
            Method method = c.getMethod("setTemporaryScreenBrightnessSettingOverride", int.class);
            method.invoke(object, brightness);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isScreenDim(Context context){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        try {
            Method method = pm.getClass().getDeclaredMethod("isScreenDim");
            method.setAccessible(true);
            return (Boolean) method.invoke(pm);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
