package com.meizu.flyme;

import android.content.Context;
import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class Utils {

    public static Object getIPackageManager() {
        try {
            Class<?> c = Class.forName("android.content.pm.IPackageManager$Stub");
            Method method = c.getMethod("asInterface", new Class[]{IBinder.class});
            IBinder packageService = getIBinder("package");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getIPowerManager() {
        try {
            Class<?> c = Class.forName("android.os.IPowerManager$Stub");
            Method method = c.getMethod("asInterface", new Class[]{IBinder.class});
            IBinder packageService = getIBinder("power");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getITelephonyExt() {
        try {
            Class<?> c = Class.forName("android.telephony.ITelephonyExt$Stub");
            Method method = c.getMethod("asInterface", new Class[]{IBinder.class});
            IBinder packageService = getIBinder("phone_ext");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getTelephonyManager(Context context) {
        try {
            Class<?> c = Class.forName("android.telephony.TelephonyManager");
            Method method = c.getMethod("from", Context.class);
            return method.invoke(c, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getIVoiceSenseService() {
        try {
            Class<?> c = Class.forName("android.os.IVoiceSenseService$Stub");
            Method method = c.getMethod("asInterface", new Class[] {
                    IBinder.class
            });
            IBinder packageService = getIBinder("voicesense");
            return method.invoke(c, packageService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IBinder getIBinder(String name) {
        try {
            Class<?> c = Class.forName("android.os.ServiceManager");
            Method m = c.getMethod("getService", new Class[]{String.class});
            return (IBinder)m.invoke(c, name);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStaticVariableString(String className, String variableName) {
        try {
            Class<?> c = Class.forName(className);
            Field field = c.getDeclaredField(variableName);
            field.setAccessible(true);
            Object object = field.get(c);
            return (String)object;
        } catch (Exception e) {
            e.printStackTrace();
            return variableName.toLowerCase(Locale.US);
        }
    }

    public static int getStaticVariableInt(String className, String variableName) {
        try {
            Class<?> c = Class.forName(className);
            Field field = c.getDeclaredField(variableName);
            field.setAccessible(true);
            Object object = field.get(c);
            return (Integer)object;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
