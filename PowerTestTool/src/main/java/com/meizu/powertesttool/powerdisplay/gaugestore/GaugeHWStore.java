package com.meizu.powertesttool.powerdisplay.gaugestore;

/**
 * Created by xiexiaohua on 15-5-25.
 */

import android.net.Uri;

public class GaugeHWStore extends PowerStore {
    public static final String TABLE_NAME = "stats_hw";
    public static final String PATH = "stats_hw";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    /* the statistics of index */
    public static final String INDEX = "ind";

    /* the statistics of hw type */
    public static final String TYPE = "type";

    /* the statistics of use time */
    public static final String TIME = "time";

    /* the statistics of use time */
    public static final String TIME_EXT = "time_ext";

    /* the statistics of power */
    public static final String POWER = "power";
}
