
package com.meizu.flyme;

import android.content.ContentResolver;

import java.lang.reflect.Method;

public class SettingsFlyme {
    private static final String CLASS_SETTINGS_SYSTEM = "android.provider.Settings$System";
    private static final String CLASS_MZSETTINGS_SYSTEM = "android.provider.MzSettings$System";
    private static final String CLASS_SETTINGS_GLOBAL = "android.provider.Settings$Global";
    private static final String CLASS_MZSETTINGS_SECURE = "android.provider.MzSettings$Secure";
    private static final String CLASS_SETTINGS_SECURE = "android.provider.Settings$Secure";

    public static final class System {
        public static final String CPU_L = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "CPU_L");
        public static final String SCREEN_BRIGHTNESS_ANIMATION = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "SCREEN_BRIGHTNESS_ANIMATION");

        public static final String LIGHT_FEEDBACK_ENABLED = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "LIGHT_FEEDBACK_ENABLED");
        public static final String MZ_POWER_BRIGHT_ALGOL = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "MZ_POWER_BRIGHT_ALGOL");
        public static final String VIBRATE_WHEN_RINGING = ReflectUtils.getStaticVariableString(
                CLASS_SETTINGS_SYSTEM, "VIBRATE_WHEN_RINGING");

        public static final String MZ_QUICK_WAKEUP_SWITCH = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "MZ_QUICK_WAKEUP_SWITCH");

        public static final String MZ_SMART_VOICE_WAKEUP_BY_VOICE = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "MZ_SMART_VOICE_WAKEUP_BY_VOICE");

        public static final String MZ_SMART_TOUCH_SWITCH = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "MZ_SMART_TOUCH_SWITCH");

        public static final String KEY_SYSTEM_TIPS_SOUND = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "KEY_SYSTEM_TIPS_SOUND");

        public static final String LOCKSCREEN_SOUNDS_ENABLED = ReflectUtils.getStaticVariableString(
                CLASS_SETTINGS_SYSTEM, "LOCKSCREEN_SOUNDS_ENABLED");

        public static final String KEY_SOUNDS_ENABLED = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "KEY_SOUNDS_ENABLED");

        public static final String CAMERA_SOUNDS_ENABLED = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SYSTEM, "CAMERA_SOUNDS_ENABLED");
    }

    public static final class Global {
        public static final String WIFI_SCAN_ALWAYS_AVAILABLE = ReflectUtils.getStaticVariableString(
                CLASS_SETTINGS_GLOBAL, "WIFI_SCAN_ALWAYS_AVAILABLE");

        public static final String MOBILE_DATA = ReflectUtils.getStaticVariableString(
                CLASS_SETTINGS_GLOBAL, "MOBILE_DATA");

        public static final String PREFERRED_NETWORK_MODE = ReflectUtils.getStaticVariableString(
                CLASS_SETTINGS_GLOBAL, "PREFERRED_NETWORK_MODE");
    }

    public static final class Secure {
        public static final String MZ_CURRENT_POWER_MODE = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SECURE, "MZ_CURRENT_POWER_MODE");
        public static final String DRIVE_MODE_ENABLED = ReflectUtils.getStaticVariableString(
                CLASS_MZSETTINGS_SECURE, "MZ_DRIVE_MODE");

        public static int getIntForUser(ContentResolver cr, String name, int def, int userHandle) {
            try {
                Class<?> c = Class.forName(CLASS_SETTINGS_SECURE);
                Method method = c.getDeclaredMethod("getIntForUser", ContentResolver.class, String.class, int.class, int.class);
                method.setAccessible(true);
                return (Integer) method.invoke(c,cr,name,def,userHandle);
            } catch (Exception e) {
                e.printStackTrace();
                return def;
            }
        }

        public static boolean putIntForUser(ContentResolver cr, String name, int value,
                int userHandle) {
            try {
                Class<?> c = Class.forName(CLASS_SETTINGS_SECURE);
                Method method = c.getDeclaredMethod("putIntForUser", ContentResolver.class, String.class, int.class, int.class);
                method.setAccessible(true);
                return (Boolean) method.invoke(c,cr,name,value,userHandle);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
