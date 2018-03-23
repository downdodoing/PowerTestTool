package com.meizu.powertesttool.powerdisplay.gaugedata;

/**
 * Created by wangwen1 on 16-3-30.
 */
public class GaugeStatsIndexBean {
    private int ind;
    private long time;
    private long reabattime;

    public int getInd() {
        return ind;
    }

    public long getTime() {
        return time;
    }

    public long getReabattime() {
        return reabattime;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setReabattime(long reabattime) {
        this.reabattime = reabattime;
    }

    @Override
    public String toString() {
        return "GaugeStatsIndexBean{" +
                "ind=" + ind +
                ", time=" + time +
                ", reabattime=" + reabattime +
                '}';
    }
}
