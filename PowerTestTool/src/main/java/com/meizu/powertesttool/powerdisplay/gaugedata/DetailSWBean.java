package com.meizu.powertesttool.powerdisplay.gaugedata;

/**
 * Created by wangwen1 on 16-3-31.
 */
public class DetailSWBean {
    private int uid;
    private int power;
    private int cpuTime;
    private int fgTime;
    private int mbData;
    private int mbTime;
    private int wifiData;
    private int wifiTime;
    private int wlTime;
    private int alarmTimes;
    private int fullWlTime;
    private int gpsTime;
    private int sensorTime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(int cpuTime) {
        this.cpuTime = cpuTime;
    }

    public int getFgTime() {
        return fgTime;
    }

    public void setFgTime(int fgTime) {
        this.fgTime = fgTime;
    }

    public int getMbData() {
        return mbData;
    }

    public void setMbData(int mbData) {
        this.mbData = mbData;
    }

    public int getMbTime() {
        return mbTime;
    }

    public void setMbTime(int mbTime) {
        this.mbTime = mbTime;
    }

    public int getWifiData() {
        return wifiData;
    }

    public void setWifiData(int wifiData) {
        this.wifiData = wifiData;
    }

    public int getWifiTime() {
        return wifiTime;
    }

    public void setWifiTime(int wifiTime) {
        this.wifiTime = wifiTime;
    }

    public int getWlTime() {
        return wlTime;
    }

    public void setWlTime(int wlTime) {
        this.wlTime = wlTime;
    }

    public int getAlarmTimes() {
        return alarmTimes;
    }

    public void setAlarmTimes(int alarmTimes) {
        this.alarmTimes = alarmTimes;
    }

    public int getFullWlTime() {
        return fullWlTime;
    }

    public void setFullWlTime(int fullWlTime) {
        this.fullWlTime = fullWlTime;
    }

    public int getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(int gpsTime) {
        this.gpsTime = gpsTime;
    }

    public int getSensorTime() {
        return sensorTime;
    }

    public void setSensorTime(int sensorTime) {
        this.sensorTime = sensorTime;
    }

    @Override
    public String toString() {
        return "DetailSWBean{" +
                "uid=" + uid +
                ", power=" + power +
                ", cpuTime=" + cpuTime +
                ", fgTime=" + fgTime +
                ", mbData=" + mbData +
                ", mbTime=" + mbTime +
                ", wifiData=" + wifiData +
                ", wifiTime=" + wifiTime +
                ", wlTime=" + wlTime +
                ", alarmTimes=" + alarmTimes +
                ", fullWlTime=" + fullWlTime +
                ", gpsTime=" + gpsTime +
                ", sensorTime=" + sensorTime +
                '}';
    }
}
