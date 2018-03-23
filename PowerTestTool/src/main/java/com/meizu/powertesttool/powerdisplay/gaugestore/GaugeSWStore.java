package com.meizu.powertesttool.powerdisplay.gaugestore;

/**
 * Created by xiexiaohua on 15-5-25.
 */

import android.net.Uri;

public class GaugeSWStore extends PowerStore {
    public static final String TABLE_NAME = "stats_sw";
    public static final String PATH = "stats_sw";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    public static final String DETAIL_TABLE_NAME = "stats_sw_detail";
    public static final String DETAIL_PATH = "stats_sw_detail";
    public static final Uri DETAIL_URI = Uri.parse("content://" + AUTHORITY + "/"
            + DETAIL_TABLE_NAME);

    /* Index */
    public static final String INDEX = "ind";

    /* UID */
    public static final String UID = "uid";

    /* Power statistics */
    public static final String POWER = "power";

    /* Power statistics */
    public static final String CPU_TIME = "cputime";

    /* Cpu statistics*/
    public static final String CPU_SCORE = "cpuscore";
    public static final String CPU_POWER = "cpupower";


    /* Power statistics */
    public static final String FG_TIME = "fgtime";
    public static final String GPU_POWER = "gpupower";

    /* Power statistics */
    public static final String MOBILE_DATA = "mbdata";


    /* Power statistics */
    public static final String MOBILE_TIME = "mbtime";

    public static final String MOBILE_POWER = "mbpower";

    /* Power statistics */
    public static final String WIFI_DATA = "wifidata";

    /* Power statistics */
    public static final String WIFI_TIME = "wifitime";

    public static final String WIFI_POWER = "wifipower";


    /* Power statistics */
    public static final String WAKELOCK_TIME = "wltime";
    public static final String WAKELOCK_TIMES = "wltimes";

    public static final String WAKELOCK_POWER = "wlpower";

    /* Power statistics */
    public static final String ALARM_TIMES = "alarmtimes";

    public static final String ALARM_POWER = "alarmpower";


    public static final String FULL_WAKE_TIME = "fullwltime";

    /* power statistics */
    public static final String GPS_TIME = "gpstime";
    public static final String GPS_POWER = "gpspower";

    /* power statistics */
    public static final String SENSOR_TIME = "sensortime";
    public static final String SENSOR_POWR = "sensorpower";



}

