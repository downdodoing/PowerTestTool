package com.meizu.flyme;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PowerProfileFlyme {

    public static final double getBatteryCapacity(Object powerProfileObj) {
        try {
            // Class<?> c = Class.forName("com.android.internal.os");
            if (powerProfileObj == null)
                return -1;

            Class<?> c = powerProfileObj.getClass();
            Method method = c.getDeclaredMethod("getBatteryCapacity");
            method.setAccessible(true);
            return (double) method.invoke(powerProfileObj);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static final Object createPowerProfile(Context context) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.os.PowerProfile");
            Constructor<?> c = clazz.getDeclaredConstructor(Context.class);
            c.setAccessible(true);
            Object profileObj = c.newInstance(context);
            return profileObj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        }

    }

}
