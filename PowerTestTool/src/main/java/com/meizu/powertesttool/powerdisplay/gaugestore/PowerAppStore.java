
package com.meizu.powertesttool.powerdisplay.gaugestore;

import android.net.Uri;

public class PowerAppStore extends PowerStore {
    public static final String TABLE_NAME = "power_app_level";
    public static final String PATH = "power_app_level";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    /* package name */
    public static final String PACKAGE_NAME = "package_name";

    // def level type user_setting
    public static final String DEFAULT = "def";

    public static final String APP_LEVEL = "level";

    public static final String APP_TYPE = "type";

    public static final String USR_SETTING = "usr_setting";

    public static final String USR_BLACK = "ub";

    //public static final int FREEZE_AT_IDLE = 1;

}
