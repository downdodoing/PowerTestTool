package com.meizu.powertesttool.powerdisplay.gaugedata;

/**
 * Created by root on 15-7-8.
 */
public class PowerHWDetail {
    public static int POWER_MULTIPLIER = 10000;

    public static final int NONE = -1;
    public static final int IDLE = 0;
    public static final int CELL = 1;
    public static final int PHONE= 2;
    public static final int WIFI = 3;
    public static final int BLUETOOTH = 4;
    public static final int FLASHLIGHT = 5;
    public static final int SCREEN = 6;
    public static final int TYPE_SIZE = 7;

    public int type = NONE;
    public int time = 0;
    public int extraTime = 0;
    public int power = 0;
}
