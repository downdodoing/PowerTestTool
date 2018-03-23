package com.meizu.powertesttool.sensor;

import android.hardware.Sensor;

/**
 * Created by wangwen1 on 16-2-1.
 */
public class SensorBean {
    private Sensor sensor;
    private String sensorInfo;
    private Boolean switchStatus;

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getSensorInfo() {
        return sensorInfo;
    }

    public void setSensorInfo(String sensorInfo) {
        this.sensorInfo = sensorInfo;
    }

    public Boolean getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(Boolean switchStatus) {
        this.switchStatus = switchStatus;
    }

    @Override
    public String toString() {
        return "SensorBean{" +
                "sensor=" + sensor +
                ", sensorInfo='" + sensorInfo + '\'' +
                ", switchStatus=" + switchStatus +
                '}';
    }
}
