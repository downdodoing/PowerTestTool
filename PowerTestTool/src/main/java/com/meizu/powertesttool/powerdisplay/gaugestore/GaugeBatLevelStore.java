package com.meizu.powertesttool.powerdisplay.gaugestore;

/**
 * Created by xiexiaohua on 15-5-25.
 */

import android.net.Uri;

public class GaugeBatLevelStore extends PowerStore {
    public static final String TABLE_NAME = "battery_level";
    public static final String PATH = "battery_level";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    /* the battery level */
    public static final String LEVEL = "level";

    /* the statistics of time */
    public static final String TIME = "time";

    /* plugged or not plugged */
    public static final String PLUGGED = "plugged";


}
