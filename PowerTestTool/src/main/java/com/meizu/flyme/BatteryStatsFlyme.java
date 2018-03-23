
package com.meizu.flyme;

import android.os.IBinder;
import android.os.Parcel;
import android.util.SparseArray;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by xiexiaohua on 15-4-2.
 */

public class BatteryStatsFlyme {

    public static String getBatteryServiceName() {
        try {
            Class<?> c = Class.forName("android.os.BatteryStats");
            Field field = c.getDeclaredField("SERVICE_NAME");
            field.setAccessible(true);
            Object object = field.get(c);
            return (String) object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getSinceCharged() {
        try {
            Class<?> c = Class.forName("android.os.BatteryStats");
            Field field = c.getDeclaredField("STATS_SINCE_CHARGED");
            field.setAccessible(true);
            Object object = field.get(c);
            return (Integer) object;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getSinceUnplugged() {
        try {
            Class<?> c = Class.forName("android.os.BatteryStats");
            Field field = c.getDeclaredField("STATS_SINCE_UNPLUGGED");
            field.setAccessible(true);
            Object object = field.get(c);
            return (Integer) object;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getWakeTypePartial() {
        try {
            Class<?> batStatsClazz = Class.forName("android.os.BatteryStats");
            Field field = batStatsClazz.getDeclaredField("WAKE_TYPE_PARTIAL");
            return (Integer) field.get(batStatsClazz);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        }
    }

    public static int getWakeTypeFull() {
        try {
            Class<?> batStatsClazz = Class.forName("android.os.BatteryStats");
            Field field = batStatsClazz.getDeclaredField("WAKE_TYPE_FULL");
            return (Integer) field.get(batStatsClazz);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        }
    }

    public static int getWakeTypeWindow() {
        try {
            Class<?> batStatsClazz = Class.forName("android.os.BatteryStats");
            Field field = batStatsClazz.getDeclaredField("WAKE_TYPE_WINDOW");
            return (Integer) field.get(batStatsClazz);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        }
    }

    public static Object getIBatteryStats() {
        try {
            String service_name = getBatteryServiceName();

            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            Method getServiceMethod = serviceManager.getMethod("getService", new Class[]{
                    String.class
            });
            getServiceMethod.setAccessible(true);
            IBinder battery = (IBinder) getServiceMethod.invoke(serviceManager, service_name);

            Class<?> stubClass = Class.forName("com.android.internal.app.IBatteryStats$Stub");
            Method stubMethod = stubClass.getMethod("asInterface", new Class[]{
                    IBinder.class
            });
            Object IBatteryStats = (Object) stubMethod.invoke(stubClass, battery);

            return IBatteryStats;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static byte[] getStatistics(Object batStatsObj) {
        try {
            Class<?> c = batStatsObj.getClass();
            Method method = c.getMethod("getStatistics");
            byte[] data = (byte[]) method.invoke(batStatsObj);
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getUseSensorsUids() {
        try {
            Object object = ReflectUtils.getIBatteryStats();
            Class<?> IBatteryStats = object.getClass();
            Method m = IBatteryStats.getDeclaredMethod("getUseSensorsUids");
            m.setAccessible(true);
            return (List<String>) m.invoke(object);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void distributeWorkLocked(int which, Object stats) {
        try {
            Class<?> batStatsImplClazz = stats.getClass();
            Method method = batStatsImplClazz.getDeclaredMethod("distributeWorkLocked", int.class);
            method.setAccessible(true);
            method.invoke(stats, which);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public static Object createFromParcel(Parcel in) {
        try {
            Class<?> batStatsImplClazz = Class.forName("com.android.internal.os.BatteryStatsImpl");
            Field field = batStatsImplClazz.getDeclaredField("CREATOR");
            field.setAccessible(true);
            Object object = field.get(batStatsImplClazz);

            Class<?> creatorClazz = object.getClass();
            Method method = creatorClazz.getDeclaredMethod("createFromParcel", Parcel.class);
            method.setAccessible(true);
            Object resultObj = (Object) method.invoke(object, in);
            return resultObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, ? extends Object> getKernelWakelockStats(Object batStatsObj) {
        try {
            Class<?> batStatsClazz = batStatsObj.getClass();
            Method method = batStatsClazz.getDeclaredMethod("getKernelWakelockStats");
            method.setAccessible(true);
            Map<String, ? extends Object> map = (Map<String, ? extends Object>) method
                    .invoke(batStatsObj);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SparseArray<? extends Object> getUidStats(Object statsObj) {
        try {
            Class<?> c = statsObj.getClass();
            Method method = c.getDeclaredMethod("getUidStats");
            method.setAccessible(true);
            SparseArray<? extends Object> uidArr = (SparseArray<? extends Object>) method
                    .invoke(statsObj);
            return uidArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public static long getBatteryRealtime(Object batStatsObj, long realTime) {
        try {
            Class<?> c = batStatsObj.getClass();
            Method method = c.getDeclaredMethod("getBatteryRealtime", long.class);
            method.setAccessible(true);
            return (long) method.invoke(batStatsObj, realTime);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static long computeWakeLock(Object timer, long batteryRealtime, int which) {
        if (timer != null) {
            try {
                Class<?> timerClazz = timer.getClass();
                Method method = timerClazz.getMethod("getTotalTimeLocked", long.class, int.class);
                method.setAccessible(true);
                long totalTimeMicros = (Long) method.invoke(timer, batteryRealtime, which);
                long totalTimeMillis = (totalTimeMicros + 500) / 1000;
                return totalTimeMillis;

            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            }

        }
        return 0;

    }

    public static final class Uid {

        public static Map<String, ? extends Object> getWakelockStats(Object u) {
            try {
                Class<?> c = u.getClass();
                Method method = c.getDeclaredMethod("getWakelockStats");
                method.setAccessible(true);
                Map<String, ? extends Object> map = (Map<String, ? extends Object>) method
                        .invoke(u);
                return map;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static final class Wakelock {

            public static Object getWakeTime(Object wl, int type) {
                try {
                    Class<?> c = wl.getClass();
                    Method method = c.getDeclaredMethod("getWakeTime", int.class);
                    method.setAccessible(true);
                    Object timerObj = (Object) method.invoke(wl, type);
                    return timerObj;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

        }

        public static final class Pkg {

            public static int getWakeups(Object pkg, int which) {
                try {
                    Class<?> c = pkg.getClass();
                    Method method = c.getDeclaredMethod("getWakeups", int.class);
                    method.setAccessible(true);
                    return (int) method.invoke(pkg, which);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }

            }
        }

        public static int getUid(Object u) {
            try {
                Class<?> c = u.getClass();
                Method method = c.getDeclaredMethod("getUid");
                method.setAccessible(true);
                return (int) method.invoke(u);

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

        }

        public static Map<String, ? extends Object> getPackageStats(Object u) {
            try {
                Class<?> c = u.getClass();
                Method method = c.getDeclaredMethod("getPackageStats");
                method.setAccessible(true);
                Map<String, ? extends Object> map = (Map<String, ? extends Object>) method
                        .invoke(u);
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public static final class Timer {

        public static int getCountLocked(Object statsTimer, int which) {
            try {
                Class<?> timerClazz = statsTimer.getClass();
                Method method = timerClazz.getMethod("getCountLocked", int.class);
                method.setAccessible(true);
                int count = (int) method.invoke(statsTimer, which);
                return count;

            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            }

        }

        public static long getTotalTimeLocked(Object parTimer, long batteryRealtime, int which) {
            try {
                Class<?> parClazz = parTimer.getClass();

                Method method = parClazz.getMethod("getTotalTimeLocked", long.class, int.class);
                method.setAccessible(true);
                long resultTime = (long) method.invoke(parTimer, batteryRealtime, which);
                return resultTime;

            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            }

        }

    }

}
