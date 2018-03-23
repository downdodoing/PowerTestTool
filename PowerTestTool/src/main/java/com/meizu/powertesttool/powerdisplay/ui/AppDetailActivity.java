package com.meizu.powertesttool.powerdisplay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.meizu.powertesttool.R;

/**
 * Created by wangwen1 on 16-3-28.
 * １、显示应用详细耗电信息
 */
public class AppDetailActivity extends Activity{
    private TextView mUidView;
    private TextView mPackageNameView;
    private TextView mPowerView;
    private TextView mCpuTimeView;
    private TextView mFgTimeView;
    private TextView mMbDataView;
    private TextView mMbTimeView;
    private TextView mWifiDataView;
    private TextView mWifiTimeView;
    private TextView mWlTimeView;
    private TextView mAlarmTimesView;
    private TextView mFullWlTimeView;
    private TextView mGpsTimeView;
    private TextView mSensorTimeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_app_detail_layout);
        mUidView = (TextView) findViewById(R.id.app_detail_uid);
        mPackageNameView = (TextView) findViewById(R.id.app_detail_package_name);
        mPowerView = (TextView) findViewById(R.id.app_detail_power);
        mCpuTimeView = (TextView) findViewById(R.id.app_detail_cputime);
        mFgTimeView = (TextView) findViewById(R.id.app_detail_fgtime);
        mMbDataView = (TextView) findViewById(R.id.app_detail_mbdata);
        mMbTimeView = (TextView) findViewById(R.id.app_detail_mbtime);
        mWifiDataView = (TextView) findViewById(R.id.app_detail_wifidata);
        mWifiTimeView = (TextView) findViewById(R.id.app_detail_wifitime);
        mWlTimeView = (TextView) findViewById(R.id.app_detail_wltime);
        mAlarmTimesView = (TextView) findViewById(R.id.app_detail_alarmtimes);
        mFullWlTimeView = (TextView) findViewById(R.id.app_detail_fullwltime);
        mGpsTimeView = (TextView) findViewById(R.id.app_detail_gpstime);
        mSensorTimeView = (TextView) findViewById(R.id.app_detail_sensortime);

        Intent intent = getIntent();
        int uid = intent.getIntExtra("uid", -1);
        String packageName = intent.getStringExtra("package_name");
        int power = intent.getIntExtra("power", 0);
        int cpuTime = intent.getIntExtra("cpu_time", 0);
        int fgTime = intent.getIntExtra("fg_time", 0);
        int mbData = intent.getIntExtra("mb_data", 0);
        int mbTime = intent.getIntExtra("mb_time", 0);
        int wifiData = intent.getIntExtra("wifi_data", 0);
        int wifiTime = intent.getIntExtra("wifi_time", 0);
        int wlTime = intent.getIntExtra("wl_time", 0);
        int alarmTimes = intent.getIntExtra("alarm_times", 0);
        int fullWlTime = intent.getIntExtra("full_wl_time", 0);
        int gpsTime = intent.getIntExtra("gps_time", 0);
        int sensorTime = intent.getIntExtra("sensor_time", 0);

        mUidView.setText(String.valueOf(uid));
        mPackageNameView.setText(packageName);
        mPowerView.setText(String.valueOf(power));
        mCpuTimeView.setText(String.valueOf(cpuTime));
        mFgTimeView.setText(String.valueOf(fgTime));
        mMbDataView.setText(String.valueOf(mbData));
        mMbTimeView.setText(String.valueOf(mbTime));
        mWifiDataView.setText(String.valueOf(wifiData));
        mWifiTimeView.setText(String.valueOf(wifiTime));
        mWlTimeView.setText(String.valueOf(wlTime));
        mAlarmTimesView.setText(String.valueOf(alarmTimes));
        mFullWlTimeView.setText(String.valueOf(fullWlTime));
        mGpsTimeView.setText(String.valueOf(gpsTime));
        mSensorTimeView.setText(String.valueOf(sensorTime));

    }
}
