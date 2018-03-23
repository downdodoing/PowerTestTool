package com.meizu.powertesttool.powerdisplay.gaugedata;

public class PowerSWDetail {
    public static int POWER_MULTIPLIER = 10000;
    int uid;
    int power;
    CpuGauge mCpu;
    GpuGauge mGpu;
    NetworkGauge mNetwork;
    StandbyGauge mStandby;
    GpsGauge mGps;
    OtherSensorGauge mSensor;
    public int getTotalPower(){
        return mCpu.cpupower + mGpu.gpupower + mNetwork.wifiPower + mNetwork.mobilePower + mStandby.alarmPower + mStandby.wakelockPower + mGps.gpsPower + mSensor.sensorPower;
    }

    public PowerSWDetail(){
        mCpu = new CpuGauge();
        mGpu = new GpuGauge();
        mNetwork = new NetworkGauge();
        mStandby = new StandbyGauge();
        mGps     = new GpsGauge();
        mSensor  = new OtherSensorGauge();
    }
    public class CpuGauge{
        long cpuScore = 0;
        int cputime = 0;
        int cpupower = 0;
    }

    public class GpuGauge {
        int fgtime = 0;
        int gpupower = 0;
    }

    public class NetworkGauge{
        long mobilePackets = 0;
        int mobileActiveTime = 0;
        int mobilePower = 0;

        long wifiPackets = 0;
        int wifiRunningTime = 0;
        int wifiPower = 0;
    }

    public class StandbyGauge{
        int wakelockTime = 0;   //minutes
        int wakelockPower = 0;
        int alarmTimes = 0;
        int alarmPower = 0;
    }

    public class GpsGauge{
        int gpsTime = 0;
        int gpsPower= 0;
    }

    public class OtherSensorGauge {
        int sensorTime = 0;
        int sensorPower = 0;
    }
}
