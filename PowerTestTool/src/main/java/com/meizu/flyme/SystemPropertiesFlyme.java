
package com.meizu.flyme;

import java.lang.reflect.Method;

public class SystemPropertiesFlyme {

    public static void set(String key, String val) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getDeclaredMethod("set", String.class, String.class);
            method.setAccessible(true);
            method.invoke(c, key, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key, String def) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getDeclaredMethod("get", String.class, String.class);
            method.setAccessible(true);
            return (String) method.invoke(c, key, def);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }
}
