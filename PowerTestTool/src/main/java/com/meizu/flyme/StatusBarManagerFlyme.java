
package com.meizu.flyme;

import android.content.Context;

import java.lang.reflect.Method;

public class StatusBarManagerFlyme {
    public static final int DISABLE_NONE = ReflectUtils.getStaticVariableInt("android.app.StatusBarManager", "DISABLE_NONE");
    public static final int DISABLE_EXPAND = ReflectUtils.getStaticVariableInt("android.app.StatusBarManager", "DISABLE_EXPAND");

    public static void disable(Context context, int what) {
        try {
            String STATUS_BAR_SERVICE = ReflectUtils.getStaticVariableString("android.content.Context",
                    "STATUS_BAR_SERVICE");
            Object sb = context.getSystemService(STATUS_BAR_SERVICE);
            Class c = sb.getClass();
            Method method = c.getMethod("disable", int.class);
            method.invoke(sb, what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
