package com.meizu.powertesttool.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wangwen1 on 16-2-1.
 */
public class SensorTestManager {
    private List<Sensor> sensors;
    private Context mContext;

    public List<SensorBean> getSensors(Context context){
        //从系统服务中获得传感器管理器
        android.hardware.SensorManager sm = (android.hardware.SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //从传感器管理器中获得全部的传感器列表
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        List<SensorBean> sensorBeans = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (Sensor sensor : allSensors) {
            Log.i("ww", "" + sensor.toString());
            if (sensor.getVendor().equals("ASOP")){
                continue;
            }
            int type = sensor.getType();
            if (set.contains(type)) {
                Log.i("ww", "contain " + type);
                continue;
            }
            set.add(type);
            SensorBean sensorBean = new SensorBean();
            sensorBean.setSensor(sensor);
            sensorBean.setSwitchStatus(false);
            sensorBeans.add(sensorBean);
            switch (type) {
                case Sensor.TYPE_ACCELEROMETER:
                    String s = Sensor.STRING_TYPE_ACCELEROMETER;
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    break;
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    break;
                case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                    break;
                case Sensor.TYPE_GRAVITY:
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    break;
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    break;
                case Sensor.TYPE_HEART_RATE:
                    break;
                case Sensor.TYPE_LIGHT:
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    break;
                case Sensor.TYPE_PRESSURE:
                    break;
                case Sensor.TYPE_PROXIMITY:
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    break;
                case Sensor.TYPE_STEP_DETECTOR:
                    break;
                case Sensor.TYPE_ORIENTATION:
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    break;

            }
        }

        return sensorBeans;
    }
}
