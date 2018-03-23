
package com.meizu.flyme;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import java.lang.reflect.Method;

public class PackageManagerFlyme {

    public static void updatemPowerSavingMode(Context context, boolean saving) {
        PackageManager pm = context.getPackageManager();
        try {
            Method method = pm.getClass().getDeclaredMethod("updatemPowerSavingMode", boolean.class);
            method.setAccessible(true);
            method.invoke(pm, saving);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setComponentEnabledSetting(Context context, ComponentName componentName,
                                                  int newState, int flags) {
        PackageManager pm = context.getPackageManager();
        try {
            Method method = pm.getClass().getDeclaredMethod("setComponentEnabledSetting", ComponentName.class, int.class, int.class);
            method.setAccessible(true);
            method.invoke(pm, componentName, newState, flags);
        } catch (Exception e) {
            e.printStackTrace();
        }  }

    public static void addPersistentPreferredActivity(IntentFilter filter, ComponentName activity,int userId){
        try {
            Object object = ReflectUtils.getIPackageManager();
            Class c = object.getClass();
            Method method = c.getMethod("addPersistentPreferredActivity", IntentFilter.class, ComponentName.class, int.class);
            method.invoke(object, filter,activity,userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearPackagePersistentPreferredActivities(String packageName, int userId) {
        try {
            Object object = ReflectUtils.getIPackageManager();
            Class c = object.getClass();
            Method method = c.getMethod("clearPackagePersistentPreferredActivities", String.class, int.class);
            method.invoke(object,packageName,userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearPreferredActivityWithIntentFilter(Context context, IntentFilter filter) {
        try {
            Object object = ReflectUtils.getIPackageManager();
            Class c = object.getClass();
            Method method = c.getMethod("clearPreferredActivityWithIntentFilter", IntentFilter.class);
            method.invoke(object, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replacePreferredActivity(Context context, IntentFilter filter, int match,ComponentName[] set, ComponentName activity){
        PackageManager pm = context.getPackageManager();
        try {
            Class c = pm.getClass();
            Method method = c.getMethod("replacePreferredActivity", IntentFilter.class, int.class, ComponentName[].class,ComponentName.class);
            method.invoke(pm, filter, match,set,activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isGuestMode(Context context) {
        try {
            Object object = ReflectUtils.getIPackageManager();
            Class c = object.getClass();
            Method method = c.getMethod("isGuestMode");
            return (Boolean) method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
