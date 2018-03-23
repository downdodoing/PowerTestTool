
package com.meizu.flyme;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActivityManagerFlyme {
    private static final String TAG = "ActivityManagerFlyme";

    public static void forceKillProcess(int pid) {
        try {
            Object service = ReflectUtils.getIFlymePermissionService();
            service.getClass().getMethod("forceKillProcess", new Class[]{
                    Integer.TYPE
            }).invoke(service, pid);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "forceKillProcess failed: " + e);
            e.printStackTrace();
        }
    }

    public static void forceStopPackage(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Method method;
        try {
            method = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
            method.setAccessible(true);
            method.invoke(am, packageName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "forceStopPackage failed: " + e);
            e.printStackTrace();
        }
    }

    public static boolean removeTask(Context context, int taskId) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Method method;
        Field field = null;
        try {
            field = am.getClass().getDeclaredField("REMOVE_TASK_KILL_PROCESS");
            field.setAccessible(true);
        } catch (Exception e) {
            Log.e(TAG, "remove_task_kill_process field does not exist");

            e.printStackTrace();
        }

        try {
            if (field != null) {
                method = am.getClass().getDeclaredMethod("removeTask", int.class, int.class);
                method.setAccessible(true);
                return (Boolean) method.invoke(am, taskId, field.getInt(am));
            } else {
                method = am.getClass().getDeclaredMethod("removeTask", int.class);
                method.setAccessible(true);
                return (Boolean) method.invoke(am, taskId);
            }
        } catch (Exception e) {
            Log.e(TAG, "removeTask failed: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public static void forceFreezePackage(Context context, String string) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Method method;
        try {
            method = am.getClass().getDeclaredMethod("forceFreezePackage", String.class);
            method.setAccessible(true);
            method.invoke(am, string);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            Log.d(TAG, "freeze app : " + string);
        }
    }

    public static void forceUnfreezePackage(Context context, String string) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Method method;
        try {
            method = am.getClass().getDeclaredMethod("forceUnfreezePackage", String.class);
            method.setAccessible(true);
            method.invoke(am, string);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static boolean isOldVersion(Context context) {
        return !isFreezeVersion(context);
    }

    public static boolean isFreezeVersion(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            Class<?> amClass = am.getClass();
            Method[] methods = amClass.getMethods();
            boolean hasFreeze = false;
            for (Method method : methods) {
                if (method.getName().equals("forceFreezePackage")) {
                    hasFreeze = true;
                    break;
                }
            }
            return hasFreeze;

        } catch (Exception e) {
            Log.e(TAG, " " + e);
        }

        return false;
    }

}
