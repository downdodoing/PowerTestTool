
package com.meizu.flyme;

import android.os.UserHandle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UserHandleFlyme {
    public static final int USER_ALL = ReflectUtils.getStaticVariableInt("android.os.UserHandle",
            "USER_ALL");
    public static final int USER_CURRENT = ReflectUtils.getStaticVariableInt("android.os.UserHandle",
            "USER_CURRENT");
    public static final UserHandle ALL = getStaticVariableUserHandle("ALL");

    public static final UserHandle CURRENT = getStaticVariableUserHandle("CURRENT");

    private static UserHandle getStaticVariableUserHandle(String variableName) {
        try {
            Class<?> c = UserHandle.class;
            Field field = c.getDeclaredField(variableName);
            field.setAccessible(true);
            Object object = field.get(c);
            return (UserHandle) object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int myUserId(){
        try {
            Class c = UserHandle.class;
            Method method = c.getMethod("myUserId");
            return (int)method.invoke(c);
        } catch (Exception e) {
            e.printStackTrace();
            return USER_CURRENT;
        }
    }
}
