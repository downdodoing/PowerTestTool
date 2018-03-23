package com.meizu.powertesttool.powerdisplay.gaugestore;

import android.net.Uri;

public class Constants {

    public static final String PACKAGE_NAME_YUNOS_SECURITY_CENTER = "com.aliyun.SecurityCenter";
    public static final String PACKAGE_NAME_ALIPAY = "com.eg.android.AlipayGphone";
    public static final String PACKAGE_NAME_ALARM_CLOCK = "com.android.alarmclock";
    public static final String PACKAGE_NAME_SOGOU_INPUTMETHOD = "com.sohu.inputmethod.sogou";
    public static final String PACKAGE_NAME_STK = "com.android.stk";
    public static final String PACKAGE_NAME_UTK = "com.android.utk";

    public static final String PACKAGE_NAME_SYSTEM_KEYBOARD = "com.meizu.flyme.input";
    public static final String PACKAGE_NAME_SAFE_CENTER = "com.meizu.safe";
    public static final String PACKAGE_NAME_BACKUP = "com.meizu.backup";
    public static final String PACKAGE_NAME_CUSTOMIZECENTER = "com.meizu.customizecenter";
    public static final String PACKAGE_NAME_SINA_WEIBO = "com.meizu.weiboshare";
    public static final String PACKAGE_NAME_SYSTEM_WALLPAPER = "com.meizu.systemwallpaper";
    public static final String PACKAGE_NAME_GALLERY = "com.meizu.media.gallery";
    public static final String PACKAGE_NAME_SETUP = "com.meizu.setup";
    public static final String PACKAGE_NAME_SYSTEM_UPGRADE = "com.meizu.flyme.update";
    public static final String PACKAGE_NAME_FLYME_LAUNCHER = "com.meizu.flyme.launcher";
    public static final String PACKAGE_NAME_FLYME_WEATHER = "com.meizu.flyme.weather";
    public static final String PACKAGE_NAME_FLASHLIGHT = "com.meizu.flashlight";
    public static final String PACKAGE_NAME_POWER_SAVE = "com.meizu.powersave";
    public static final String PACKAGE_NAME_FLYME_ACCOUNT = "com.meizu.account";
    public static final String PACKAGE_NAME_FLYME_VOICE_ASSISTANT = "com.meizu.voiceassistant";
    public static final String PACKAGE_NAME_OP09_PLUGIN = "com.mediatek.op09.plugin";
    public static final String PACKAGE_NAME_SEARCH = "com.meizu.net.search";
    public static final String PACKAGE_NAME_MZ_CLOUD = "com.meizu.cloud";

    public static final String ACTION_OPEN_FLYME_ACCOUNT = "com.meizu.account.ACCOUNTCENTER";
    public static final String ACTION_HANDLE_ROOT = "com.meizu.action.ROOT";
    public static final String ACTION_ROOT_SETTINGS = "android.settings.ROOT_SETTINGS";
    public static final String ACTION_EASY_MODE_CHANGE = "com.meizu.flyme.easylauncher.ACTION_EASY_MODE_CHANGE";
    public static final String ACTION_BATTERY_OPTIMIZAION = "com.meizu.power.POWER_UI_MAIN";
    public static final String ACTION_SYSTEM_KEYBOARD = "android.intent.action.SYSTEM_KEYBOARD_SETTINGS";

    public static final String ACTION_START_POWER = "com.meizu.power.start.power";

    public static final String ACTION_START_POWER_KILL_APP = "com.meizu.power.PowerAppKilledNotification";

    public static final Uri URI_MEIZU_ACCOUNT = Uri.parse("content://com.meizu.account/account");

    //zengxin@SHELL.MTP add CDROM/Charge-Only function {@
    /**
     * Name of the USB Charging function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     * {@hide}
     */
    public static final String USB_FUNCTION_CHARGING_ONLY = "charging";

    /**
     * Name of the USB BICR function.
     * Used in extras for the {@link #ACTION_USB_STATE} broadcast
     */
    public static final String USB_FUNCTION_BICR = "bicr";
    //@}

    public static final String QUICK_WAKEUP_ALIPAY_CODE = "alipayCode";
    /**
     * dismiss time should less than task delay time FIX=#244200
     * Popup dialog dismiss time should confirm with cjy@meizu.com
     * Last comfirm time: 2015-11-09 Time:224ms < 240ms
     */
    public static final int LIST_PREFERENCE_ANIM_TIME = 240;
}
