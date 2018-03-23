package com.meizu.powertesttool.powerdisplay.gaugedata;

/**
 * Created by wangwen1 on 16-3-30.
 */
public class GaugeBatteryBean {
    private long time;
    private int level;
    private int plugType;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPlugType() {
        return plugType;
    }

    public void setPlugType(int plugType) {
        this.plugType = plugType;
    }

    @Override
    public String toString() {
        return "GaugeBatteryBean{" +
                "time=" + time +
                ", level=" + level +
                ", plugType=" + plugType +
                '}';
    }
}
