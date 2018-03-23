package com.meizu.flyme;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class AppOpsManagerFlyme {
    private static final String TAG = "AppOpsManagerFlyme";
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = ReflectUtils.getStaticVariableInt("android.app.AppOpsManager", "OP_MONITOR_HIGH_POWER_LOCATION");


    public static List<Object> getPackagesForOps(int[] ops) {
        try {
            Object object= ReflectUtils.getIAppOpsManager();
            Class<?> c = object.getClass();
            Method m = c.getDeclaredMethod("getPackagesForOps", int[].class);
            m.setAccessible(true);

            return (List<Object>)m.invoke(object, ops);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class PackageOps {

        public static String getPackageName(Object packageOps) {
            try {
                Class<?> c = Class.forName("android.app.AppOpsManager$PackageOps");
                Method method = c.getDeclaredMethod("getPackageName");
                method.setAccessible(true);
                return (String)method.invoke(packageOps);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static int getUid(Object packageOps) {
            try {
                Class<?> c = Class.forName("android.app.AppOpsManager$PackageOps");
                Method method = c.getDeclaredMethod("getUid");
                method.setAccessible(true);
                return (int)method.invoke(packageOps);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }

        public static List<Object> getOps(Object packageOps) {
            try {
                Class<?> c = Class.forName("android.app.AppOpsManager$PackageOps");
                Method method = c.getDeclaredMethod("getOps");
                method.setAccessible(true);
                return (List<Object>)method.invoke(packageOps);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static class OpEntry {
        public static boolean isRunning(Object opEntry) {
            try {
                Class<?> c = Class.forName("android.app.AppOpsManager$OpEntry");
                Method method = c.getDeclaredMethod("isRunning");
                method.setAccessible(true);
                return (boolean)method.invoke(opEntry);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }

}
