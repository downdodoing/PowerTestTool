package com.meizu.powertesttool.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.meizu.powertesttool.R;

import java.util.ArrayList;
import java.util.List;


public class SensorAdapter extends BaseAdapter {

    private List<SensorBean> mSensors;
    private LayoutInflater mInflater;

    private ProSensorListener proSensorListener;
    private StepSensorListener stepSensorListener;
    private StepDetectorSensorListener stepDetectorSensorListener;
    private AccSensorListener accSensorListener;
    private LinearAccSensorListener linearAccSensorListener;
    private GravitySensorListener gravitySensorListener;
    private HumiditySensorListener humiditySensorListener;
    private GyroscopeSensorListener gyroscopeSensorListener;
    private RotationVectorSensorListener rotationVectorSensorListener;
    private MagneticFieldSensorListener magneticFieldSensorListener;
    private LightSensorListener lightSensorListener;
    private PressureSensorListener pressureSensorListener;
    private ProximitySensorListener proximitySensorListener;
    private AmbientTempSensorListener ambientTempSensorListener;
    private GameRotateVectorSensorListener gameRotateVectorSensorListener;
    private SignificatMotionSensorListener significatMotionSensorListener;
    private TiltDetectorSensorListener tiltDetectorSensorListener;
    private OrientationSensorListener orientationSensorListener;


    /** 传感器管理器 */
    private SensorManager manager;
    private List<SwitchCheckListener> listeners;

    SensorAdapter(Activity activity, List<SensorBean> sensors){
        this.mSensors = sensors;
        this.mInflater = activity.getLayoutInflater();
        //获取系统服务（SENSOR_SERVICE)返回一个SensorManager 对象
        manager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        listeners = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mSensors.size();
    }

    @Override
    public Object getItem(int position) {
        return mSensors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("SensorAdapter", "position = " + position);
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.sensor_item_layout, null);
            vh.sensorName = (TextView)convertView.findViewById(R.id.sensor_name);
            vh.sensorSwitch = (Switch)convertView.findViewById(R.id.sensor_switch);
            vh.value = (TextView)convertView.findViewById(R.id.sensor_info);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        vh.sensorName.setText("");
        vh.value.setText("");
        SensorBean sensorBean = mSensors.get(position);
        vh.sensorName.setText(sensorBean.getSensor().getName());
        vh.value.setText(sensorBean.getSensorInfo());
        vh.sensorSwitch.setOnCheckedChangeListener(null);
        vh.sensorSwitch.setChecked(sensorBean.getSwitchStatus());
        vh.sensorSwitch.setOnCheckedChangeListener(new SwitchCheckListener(position, vh.value));

        return convertView;
    }

    private static class ViewHolder{
        TextView sensorName;
        Switch sensorSwitch;
        TextView value;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHandler.removeMessages(1);
            notifyDataSetChanged();
        }
    };


    class SwitchCheckListener implements CompoundButton.OnCheckedChangeListener {
        private int position;
        private TextView info;
        public SwitchCheckListener(int position, TextView info) {
            this.position = position;
            this.info = info;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //处理监听器逻辑
            SensorBean sensorBean = mSensors.get(position);
            sensorBean.setSwitchStatus(isChecked);
            int type = sensorBean.getSensor().getType();
            switch (type) {
                case Sensor.TYPE_ACCELEROMETER:

                    if (isChecked) {
                        Log.i("ww", "开启加速度传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        //应用在前台时候注册监听器
                        if (accSensorListener == null)
                        accSensorListener = new AccSensorListener(position);
                        manager.registerListener(accSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_GAME);

                    } else {
                        Log.i("ww", "关闭加速度传感器");
                        manager.unregisterListener(accSensorListener);
                    }
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    if (isChecked) {
                        Log.i("ww", "开启温度传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
                        //应用在前台时候注册监听器
                        if (ambientTempSensorListener == null)
                        ambientTempSensorListener = new AmbientTempSensorListener(position);
                        manager.registerListener(ambientTempSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭温度传感器");
                        manager.unregisterListener(ambientTempSensorListener);
                    }
                    break;
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    if (isChecked) {
                        Log.i("ww", "开启游戏旋转矢量传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
                        //应用在前台时候注册监听器
                        if (gameRotateVectorSensorListener == null)
                            gameRotateVectorSensorListener = new GameRotateVectorSensorListener(position);
                        manager.registerListener(gameRotateVectorSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭游戏旋转矢量传感器");
                        manager.unregisterListener(gameRotateVectorSensorListener);
                    }
                    break;
                case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                    break;
                case Sensor.TYPE_GRAVITY:
                    if (isChecked) {
                        Log.i("ww", "开启重力传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                        //应用在前台时候注册监听器
                        if (gravitySensorListener == null)
                            gravitySensorListener = new GravitySensorListener(position);
                        manager.registerListener(gravitySensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭重力传感器");
                        manager.unregisterListener(gravitySensorListener);
                    }
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    if (isChecked) {
                        Log.i("ww", "开启弧度传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                        //应用在前台时候注册监听器
                        if (gyroscopeSensorListener == null)
                            gyroscopeSensorListener = new GyroscopeSensorListener(position);
                        manager.registerListener(gyroscopeSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_GAME);

                    } else {
                        Log.i("ww", "关闭弧度传感器");
                        manager.unregisterListener(gyroscopeSensorListener);
                    }
                    break;
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    break;
                case Sensor.TYPE_HEART_RATE:
                    break;
                case Sensor.TYPE_LIGHT:
                    if (isChecked) {
                        Log.i("ww", "开启光感传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
                        //应用在前台时候注册监听器
                        if (lightSensorListener == null)
                            lightSensorListener = new LightSensorListener(position);
                        manager.registerListener(lightSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_GAME);

                    } else {
                        Log.i("ww", "关闭光感传感器");
                        manager.unregisterListener(lightSensorListener);
                    }
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    if (isChecked) {
                        Log.i("ww", "开启线性加速度传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                        //应用在前台时候注册监听器
                        if (linearAccSensorListener == null)
                        linearAccSensorListener = new LinearAccSensorListener(position);
                        manager.registerListener(linearAccSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_GAME);

                    } else {
                        Log.i("ww", "关闭线性加速度传感器");
                        manager.unregisterListener(linearAccSensorListener);
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    if (isChecked) {
                        Log.i("ww", "开启磁场传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                        //应用在前台时候注册监听器
                        if (magneticFieldSensorListener == null)
                            magneticFieldSensorListener = new MagneticFieldSensorListener(position);
                        manager.registerListener(magneticFieldSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭磁场传感器");
                        manager.unregisterListener(magneticFieldSensorListener);
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    break;
                case Sensor.TYPE_PRESSURE:
                    if (isChecked) {
                        Log.i("ww", "开启压力传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_PRESSURE);
                        //应用在前台时候注册监听器
                        if (pressureSensorListener == null)
                            pressureSensorListener = new PressureSensorListener(position);
                        manager.registerListener(pressureSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭压力传感器");
                        manager.unregisterListener(pressureSensorListener);
                    }
                    break;
                case Sensor.TYPE_PROXIMITY:
                    if (isChecked) {
                        Log.i("ww", "开启距离传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                        //应用在前台时候注册监听器
                        if (proximitySensorListener == null)
                            proximitySensorListener = new ProximitySensorListener(position);
                        manager.registerListener(proximitySensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭距离传感器");
                        manager.unregisterListener(proximitySensorListener);
                    }
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    if (isChecked) {
                        Log.i("ww", "开启设备方向传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
                        //应用在前台时候注册监听器
                        if (rotationVectorSensorListener == null)
                            rotationVectorSensorListener = new RotationVectorSensorListener(position);
                        manager.registerListener(rotationVectorSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭设备方向传感器");
                        manager.unregisterListener(rotationVectorSensorListener);
                    }
                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    //特殊动作触发传感器
                    if (isChecked) {
                        Log.i("ww", "开启特殊动作触发传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
                        //应用在前台时候注册监听器
                        if (significatMotionSensorListener == null)
                            significatMotionSensorListener = new SignificatMotionSensorListener(position);
                        manager.registerListener(significatMotionSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭特殊动作触发传感器");
                        manager.unregisterListener(significatMotionSensorListener);
                    }
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    if (isChecked) {
                        Log.i("ww", "开启记步器传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                        //应用在前台时候注册监听器
                        if (stepSensorListener == null)
                        stepSensorListener = new StepSensorListener(position);
                        manager.registerListener(stepSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭记步器传感器");
                        manager.unregisterListener(stepSensorListener);
                    }
                    break;
                case Sensor.TYPE_STEP_DETECTOR:
                    if (isChecked) {
                        Log.i("ww", "开启记步器传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
                        //应用在前台时候注册监听器
                        if (stepDetectorSensorListener == null)
                            stepDetectorSensorListener = new StepDetectorSensorListener(position);
                        manager.registerListener(stepDetectorSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭记步器传感器");
                        manager.unregisterListener(stepDetectorSensorListener);
                    }
                    break;
                case Sensor.REPORTING_MODE_SPECIAL_TRIGGER:

                    if (isChecked) {
                        Log.i("ww", "开启方向传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.REPORTING_MODE_SPECIAL_TRIGGER);
                        //应用在前台时候注册监听器
                        if (orientationSensorListener == null)
                            orientationSensorListener = new OrientationSensorListener(position);
                        manager.registerListener(orientationSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭方向传感器");
                        manager.unregisterListener(orientationSensorListener);
                    }
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    if (isChecked) {
                        Log.i("ww", "开启相对湿度传感器");
                        Sensor sensorPro = manager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
                        //应用在前台时候注册监听器
                        if (humiditySensorListener == null)
                            humiditySensorListener = new HumiditySensorListener(position);
                        manager.registerListener(humiditySensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭相对湿度传感器");
                        manager.unregisterListener(humiditySensorListener);
                    }
                    break;
                case 22:
                    //TYPE_TILT_DETECTOR 每次检测到倾斜事件后均生成事件
                    if (isChecked) {
                        Log.i("ww", "开启倾斜事件传感器");
                        Sensor sensorPro = manager.getDefaultSensor(22);
                        //应用在前台时候注册监听器
                        if (tiltDetectorSensorListener == null)
                            tiltDetectorSensorListener = new TiltDetectorSensorListener(position);
                        manager.registerListener(tiltDetectorSensorListener, sensorPro,
                                SensorManager.SENSOR_DELAY_NORMAL);

                    } else {
                        Log.i("ww", "关闭倾斜事件传感器");
                        manager.unregisterListener(tiltDetectorSensorListener);
                    }
                    break;
                default:
//                    if (isChecked) {
//                        Log.i("ww", "开启默认传感器");
//                        Sensor sensorPro = manager.getDefaultSensor(type);
//                        //应用在前台时候注册监听器
//                        if (tiltDetectorSensorListener == null)
//                            tiltDetectorSensorListener = new TiltDetectorSensorListener(position);
//                        manager.registerListener(tiltDetectorSensorListener, sensorPro,
//                                SensorManager.SENSOR_DELAY_NORMAL);
//
//                    } else {
//                        Log.i("ww", "关闭默认传感器");
//                        manager.unregisterListener(tiltDetectorSensorListener);
//                    }
                    break;

            }

        }
    }

    /**
     * 默认传感器
     */
    private final class DefaultSensorListener implements SensorEventListener {

        private int position;
        DefaultSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];// 存放了方向值

            Log.i("SensorTest", "x =" + x);
//            info.setText("lenth = " + lenth);
            mSensors.get(position).setSensorInfo("x = " + x);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    /**
     * 压力
     */
    private final class PressureSensorListener implements SensorEventListener {

        private int position;
        PressureSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float pressure = event.values[0];// 存放了方向值

            Log.i("SensorTest", "pressure=" + pressure);
//            info.setText("lenth = " + lenth);
            mSensors.get(position).setSensorInfo("pressure = " + pressure);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    /**
     * 红外
     */
    private final class ProSensorListener implements SensorEventListener {

        private int position;
        ProSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float lenth = event.values[0];// 存放了方向值

            Log.i("SensorTest", "lenth=" + (int) lenth);
//            info.setText("lenth = " + lenth);
            mSensors.get(position).setSensorInfo("lenth = " + lenth);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    /**
     * 重大动作TYPE_SIGNIFICANT_MOTION
     */
    private final class SignificatMotionSensorListener implements SensorEventListener {

        private int position;
        SignificatMotionSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            float motion = event.values[0];// 存放了方向值
            Log.i("SensorTest", "motion=" +  motion);
//            info.setText("count = " + count);
            mSensors.get(position).setSensorInfo("motion = " + motion);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    /**
     * 记步器
     */
    private final class StepSensorListener implements SensorEventListener {

        private int position;
        StepSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            float count = event.values[0];// 存放了方向值
            Log.i("SensorTest", "count=" + (int) count);
//            info.setText("count = " + count);
            mSensors.get(position).setSensorInfo("count = " + count);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    /**
     * 记步器
     */
    private final class StepDetectorSensorListener implements SensorEventListener {

        private int position;
        StepDetectorSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            float count = event.values[0];// 存放了方向值
            Log.i("SensorTest", "count=" + (int) count);
//            info.setText("count = " + count);
            mSensors.get(position).setSensorInfo("count = " + count);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    /**
     * Orientation Sensor方向传感器是基于软件的，并且它的数据是通过加速度传感器和磁场传感器共同获得的
     */
    private final class OrientationSensorListener implements SensorEventListener {

        private int position;
        OrientationSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            float x=event.values[SensorManager.DATA_X];
            float y=event.values[SensorManager.DATA_Y];
            float z=event.values[SensorManager.DATA_Z];
            //info.setText("x = " + x + "\ny = " + y + "\nz = " + z);

            mSensors.get(position).setSensorInfo("x = " + x + "\ny = " + y + "\nz = " + z);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    /**
     * 加速度
     */
    private final class AccSensorListener implements SensorEventListener {
        private int position;
        //private TextView info;
//        AccSensorListener(TextView info){
//            this.info = info;
//        }
        AccSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            /**
             *  values[0]: x-axis 方向加速度
             *  values[1]: y-axis 方向加速度
             *  values[2]: z-axis 方向加速度
             */
            float degree = event.values[0];// 存放了方向值

            float x=event.values[SensorManager.DATA_X];
            float y=event.values[SensorManager.DATA_Y];
            float z=event.values[SensorManager.DATA_Z];
            //info.setText("x = " + x + "\ny = " + y + "\nz = " + z);

            mSensors.get(position).setSensorInfo("x = " + x + "\ny = " + y + "\nz = " + z);
            mHandler.sendEmptyMessageDelayed(1, 1000);
            //notifyDataSetChanged();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 线性加速度
     */
    private final class LinearAccSensorListener implements SensorEventListener {
//        private TextView info;
        private int position;
        LinearAccSensorListener(int position){
            this.position = position;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            /**
             *  values[0]: x-axis 方向加速度
             *  values[1]: y-axis 方向加速度
             *  values[2]: z-axis 方向加速度
             */

            float x=event.values[SensorManager.DATA_X];
            float y=event.values[SensorManager.DATA_Y];
            float z=event.values[SensorManager.DATA_Z];
//            info.setText("x = " + x + "\ny = " + y + "\nz = " + z);
            mSensors.get(position).setSensorInfo("x = " + x + "\ny = " + y + "\nz = " + z);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 重力
     */
    private final class GravitySensorListener implements SensorEventListener {
//        private TextView info;
//        GravitySensorListener(TextView info){
//            this.info = info;
//        }
        private int position;
        GravitySensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[SensorManager.DATA_X];
            float y=event.values[SensorManager.DATA_Y];
            float z=event.values[SensorManager.DATA_Z];
//            info.setText("x = " + x + "\ny = " + y + "\nz = " + z);
            mSensors.get(position).setSensorInfo("x = " + x + "\ny = " + y + "\nz = " + z);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 湿度
     */
    private final class HumiditySensorListener implements SensorEventListener {
//        private TextView info;
//        HumiditySensorListener(TextView info){
//            this.info = info;
//        }
        private int position;
        HumiditySensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x =event.values[0];
//            info.setText("x = " + x);
            mSensors.get(position).setSensorInfo("x = " + x);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 弧度
     */
    private final class GyroscopeSensorListener implements SensorEventListener {
//        private TextView info;
//        GyroscopeSensorListener(TextView info){
//            this.info = info;
//        }
        private int position;
        GyroscopeSensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[SensorManager.DATA_X];
            float y=event.values[SensorManager.DATA_Y];
            float z=event.values[SensorManager.DATA_Z];
//            info.setText("x = " + x + "\ny = " + y + "\nz = " + z);
            mSensors.get(position).setSensorInfo("x = " + x + "\ny = " + y + "\nz = " + z);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 旋转角度
     */
    private final class RotationVectorSensorListener implements SensorEventListener {
//        private TextView info;
//        RotationVectorSensorListener(TextView info){
//            this.info = info;
//        }
        private int position;
        RotationVectorSensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[0];
            float y=event.values[1];
            float z=event.values[2];
            //float cos = event.values[3];
//            info.setText("x = " + x + "\ny = " + y + "\nz = " + z);
            mSensors.get(position).setSensorInfo("x = " + x + "\ny = " + y + "\nz = " + z);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 磁场
     */
    private final class MagneticFieldSensorListener implements SensorEventListener {
//        private TextView info;
//        MagneticFieldSensorListener(TextView info){
//            this.info = info;
//        }
        private int position;
        MagneticFieldSensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[0];
            float y=event.values[1];
            float z=event.values[2];
//            info.setText("x = " + x + "\ny = " + y + "\nz = " + z);
            mSensors.get(position).setSensorInfo("x = " + x + "\ny = " + y + "\nz = " + z);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 亮度
     */
    private final class LightSensorListener implements SensorEventListener {
//        private TextView info;
//        LightSensorListener(TextView info){
//            this.info = info;
//        }
        private int position;
        LightSensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[0];
//            info.setText("x = " + x);
            mSensors.get(position).setSensorInfo("x = " + x);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    /**
     * 距离
     */
    private final class ProximitySensorListener implements SensorEventListener {
        //private TextView info;
        private int position;
        ProximitySensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[0];
            //info.setText("x = " + x);
            mSensors.get(position).setSensorInfo("x = " + x);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 温度
     */
    private final class AmbientTempSensorListener implements SensorEventListener {
//        private TextView info;
        private int position;
        AmbientTempSensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[0];
//            info.setText("x = " + x);
            mSensors.get(position).setSensorInfo("x = " + x);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * 游戏旋转矢量
     */
    private final class GameRotateVectorSensorListener implements SensorEventListener {
        //        private TextView info;
        private int position;
        GameRotateVectorSensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[0];
            float y=event.values[1];
            float z=event.values[2];
//            info.setText("x = " + x + "\ny = " + y + "\nz = " + z);
            mSensors.get(position).setSensorInfo("x = " + x + "\ny = " + y + "\nz = " + z);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    /**
     * TYPE_TILT_DETECTOR 每次检测到倾斜事件后均生成事件
     */
    private final class TiltDetectorSensorListener implements SensorEventListener {
        //        private TextView info;
        private int position;
        TiltDetectorSensorListener(int position){
            this.position = position;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[0];
//            float y=event.values[1];
//            float z=event.values[2];
//            info.setText("x = " + x + "\ny = " + y + "\nz = " + z);
            mSensors.get(position).setSensorInfo("x = " + x);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
