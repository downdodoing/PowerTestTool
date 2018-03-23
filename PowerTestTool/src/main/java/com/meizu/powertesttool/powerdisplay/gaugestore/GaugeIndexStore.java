package com.meizu.powertesttool.powerdisplay.gaugestore;

/**
 * Created by xiexiaohua on 15-5-25.
 */

import android.net.Uri;

public class GaugeIndexStore extends PowerStore {
    public static final String TABLE_NAME = "stats_index";
    public static final String PATH = "stats_index";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    /* the statistics of hour */
    public static final String INDEX = "ind";       // int

    /* the statistics of value */
    public static final String TIME = "time";       // long

   // public static final String DUR  = "dursecs";    // integer


    public static final String REABATTIME = "reabattime"; // integer

    public static final String UPBATTIME = "upbattime";   // integer

}
