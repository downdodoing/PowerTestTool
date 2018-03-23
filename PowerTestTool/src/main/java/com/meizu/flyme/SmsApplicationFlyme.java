package com.meizu.flyme;

import android.content.ComponentName;
import android.content.Context;

import java.lang.reflect.Method;

public class SmsApplicationFlyme {

    public static ComponentName getDefaultSmsApplication(Context context, boolean updateIfNeeded){
        try {
            Class<?> c = Class.forName("com.android.internal.telephony.SmsApplication");
            Method method = c.getDeclaredMethod("getDefaultSmsApplication",Context.class, boolean.class);
            method.setAccessible(true);
            return (ComponentName) method.invoke(c,context, updateIfNeeded);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setDefaultApplication(String packageName, Context context){
        try {
            Class<?> c = Class.forName("com.android.internal.telephony.SmsApplication");
            Method method = c.getDeclaredMethod("setDefaultApplication",String.class, Context.class);
            method.setAccessible(true);
            method.invoke(c,packageName, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
