package com.meizu.flyme;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class ReflectUtils {
    private static final String TAG = "ReflectUtils";

    public static Object getIPackageManager() {
        try {
            Class<?> c = ReflectionCache.build().forName("android.content.pm.IPackageManager$Stub");
            Method method = ReflectionCache.build().getCachedMethod(c, "asInterface", false, new Class[]{IBinder.class});
            IBinder packageService = getIBinder("package");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            Log.e(TAG, "getIPackageManager exception: " +e);
            e.printStackTrace();
        }
        return null;
    }

    public static Object getIPowerManager() {
        try {
            Class<?> c = ReflectionCache.build().forName("android.os.IPowerManager$Stub");
            Method method = ReflectionCache.build().getCachedMethod(c, "asInterface", false, new Class[]{IBinder.class});
            IBinder packageService = getIBinder("power");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            Log.e(TAG, "getIPowerManager exception: " +e);
            e.printStackTrace();
        }
        return null;
    }

    public static Object getITelephonyExt() {
        try {
            Class<?> c = ReflectionCache.build().forName("android.telephony.ITelephonyExt$Stub");
            Method method = ReflectionCache.build().getCachedMethod(c, "asInterface", false, new Class[]{IBinder.class});
            IBinder packageService = getIBinder("phone_ext");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            Log.e(TAG, "getITelephonyExt exception: " +e);
            e.printStackTrace();
        }
        return null;
    }

    public static Object getTelephonyManager(Context context) {
        try {
            Class<?> c = ReflectionCache.build().forName("android.telephony.TelephonyManager");
            Method method = ReflectionCache.build().getCachedMethod(c, "from", false, Context.class);
            return method.invoke(c, context);
        } catch (Exception e) {
            Log.e(TAG, "getTelephonyManager exception: " +e);
            e.printStackTrace();
        }
        return null;
    }

    public static Object getIVoiceSenseService() {
        try {
            Class<?> c = ReflectionCache.build().forName("android.os.IVoiceSenseService$Stub");
            Method method = ReflectionCache.build().getCachedMethod(c, "asInterface", false, new Class[] {
                    IBinder.class
            });
            IBinder packageService = getIBinder("voicesense");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            Log.e(TAG, "getIVoiceSenseService exception: " +e);
            e.printStackTrace();
        }
        return null;
    }

    public static Object getIFlymePermissionService() {
        try {
            Class<?> c = ReflectionCache.build().forName("meizu.security.IFlymePermissionService$Stub");
            Method method = ReflectionCache.build().getCachedMethod(c, "asInterface", false, new Class[]{IBinder.class});
            IBinder windowService = getIBinder("flyme_permission");
            return method.invoke(c, windowService);
        } catch (Exception e) {
            Log.e(TAG, "getIFlymePermissionService exception: " +e);
            e.printStackTrace();
        }
        return null;
    }

    public static Object getIAppOpsManager() {
        try {
            Class<?> c = ReflectionCache.build().forName("com.android.internal.app.IAppOpsService$Stub");
            Method method = ReflectionCache.build().getCachedMethod(c, "asInterface", false, new Class[]{IBinder.class});
            IBinder packageService = getIBinder("appops");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getIBatteryStats() {
        try {
            Class<?> c = ReflectionCache.build().forName("com.android.internal.app.IBatteryStats$Stub");
            Method method = ReflectionCache.build().getCachedMethod(c, "asInterface", false, new Class[]{IBinder.class});
            IBinder packageService = getIBinder("batterystats");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IBinder getIBinder(String name) {
        try {
            Class<?> c = ReflectionCache.build().forName("android.os.ServiceManager");
            Method m = ReflectionCache.build().getCachedMethod(c, "getService", false, new Class[]{String.class});
            return (IBinder)m.invoke(c, name);
        } catch(Exception e) {
            Log.e(TAG, "getIBinder exception: " +e);
            e.printStackTrace();
        }
        return null;
    }

    public static String getStaticVariableString(String className, String variableName) {
        try {
            Class<?> c = ReflectionCache.build().forName(className);
            Field field = ReflectionCache.build().getCachedField(c, variableName, true);;
            field.setAccessible(true);
            Object object = field.get(c);
            return (String)object;
        } catch (Exception e) {
            Log.e(TAG, "getStaticVariableString exception: " +e);
            e.printStackTrace();
            return variableName.toLowerCase(Locale.US);
        }
    }

    public static int getStaticVariableInt(String className, String variableName) {
        try {
            Class<?> c = ReflectionCache.build().forName(className);
            Field field = ReflectionCache.build().getCachedField(c, variableName, true);
            field.setAccessible(true);
            Object object = field.get(c);
            return (Integer)object;
        } catch (Exception e) {
            Log.e(TAG, "getStaticVariableInt exception: " +e);
            e.printStackTrace();
            return -1;
        }
    }
}
