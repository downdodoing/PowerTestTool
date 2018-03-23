
package com.meizu.flyme;

import android.view.WindowManager.LayoutParams;

import java.lang.reflect.Field;

public class LayoutParamsFlyme {
    public static int MEIZU_FLAG_INTERCEPT_HOME_KEY = ReflectUtils.getStaticVariableInt(
            "MeizuLayoutParams", "MEIZU_FLAG_INTERCEPT_HOME_KEY");

    public static void SetMeizuFlag(LayoutParams lp, int flag) {
        try {
            Field field = LayoutParams.class.getDeclaredField("meizuFlags");
            int meizuFlags = field.getInt(lp);
            field.set(lp, meizuFlags | flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
