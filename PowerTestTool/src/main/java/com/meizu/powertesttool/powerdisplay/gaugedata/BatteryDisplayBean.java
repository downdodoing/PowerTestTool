package com.meizu.powertesttool.powerdisplay.gaugedata;

/**
 * Created by wangwen1 on 16-3-30.
 */
public class BatteryDisplayBean {
    private int ind;
    private long time;
    private int level;
    private long reabattime;
    private int plugType;

    @Override
    public String toString() {
        return "BatteryDisplayBean{" +
                "ind=" + ind +
                ", time=" + time +
                ", level=" + level +
                ", reabattime=" + reabattime +
                ", plugType=" + plugType +
                '}';
    }

    public int getPlugType() {
        return plugType;
    }

    public void setPlugType(int plugType) {
        this.plugType = plugType;
    }

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

    public long getReabattime() {
        return reabattime;
    }

    public void setReabattime(long reabattime) {
        this.reabattime = reabattime;
    }

    public int getInd() {

        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }
}
