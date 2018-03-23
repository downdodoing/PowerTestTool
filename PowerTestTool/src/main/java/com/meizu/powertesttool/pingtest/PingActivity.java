package com.meizu.powertesttool.pingtest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.utils.ShellUtils;

import java.io.IOException;

/**
 * Created by wangwen1 on 16-2-3.
 */
public class PingActivity extends Activity{
    private static final String TAG = "PingActivity";
    private EditText mPingAddress;
    private EditText mPingCount;
    private Button mSendButton;
    private EditText mPingPeriod;
    private static final String DEFAULT_ADDRESS = "www.meizu.com";
    private static final String DEFAULT_COUNT = "100";
    private static final String DEFAULT_PERIOD = "1";

    private String mCount;
    private String mPeriod;
    private String mAddress;

    Intent intent;
    PendingIntent pintent;
    private AlarmManager mAlarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ping_layout);

        init();

        mPingAddress = (EditText) findViewById(R.id.ping_address);
        mPingCount = (EditText) findViewById(R.id.ping_count);
        mPingPeriod = (EditText) findViewById(R.id.ping_period);
        mSendButton = (Button) findViewById(R.id.send_ping);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("ww", "sss   " + ShellUtils.execCommand("mkdir -p /mnt/sdcard/test" , false));
                String period = mPingPeriod.getText().toString();
                if (TextUtils.isEmpty(period)) {
                    period = "-1";

                }

                String address = mPingAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    address = DEFAULT_ADDRESS;

                }
                String count1 = mPingCount.getText().toString();
                if (TextUtils.isEmpty(count1)) {
                    count1 = DEFAULT_COUNT;

                }

                mPeriod = period;
                mAddress = address;
                mCount = count1;

                if (!mPeriod.equals("-1")){

                    long firstime= SystemClock.elapsedRealtime();
                    mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, Integer.valueOf(mPeriod) * 60 * 1000, pintent);
                } else {
                    Log.i(TAG, "mPeriod = " + mPeriod + " mCount = " + mCount);

                    startPing(mCount, mAddress);
                }

                //Log.i("ww", " ping result = " + pingNetWork("www.meizu.com", "10"));
                //Log.i("ww", ShellUtils.execCommand("/system/bin/ping -c " + 10 + " -w 100 " + DEFAULT_ADDRESS + " &", false));

            }
        });

    }


    private void init(){
        mAlarmManager = (AlarmManager) PingActivity.this.getSystemService(Context.ALARM_SERVICE);
        intent =new Intent(getStrIntent());
        intent.setAction("repeating");
        pintent=PendingIntent
                .getBroadcast(PingActivity.this, 0, intent, 0);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("repeating");
        intentFilter.addAction(getStrIntent());
        PingActivity.this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int count = intent.getExtras().getInt(Intent.EXTRA_ALARM_COUNT);
                String action = intent.getAction();
                Log.i(TAG, "mPeriod = " + mPeriod + " action = " + action);

                startPing(mCount, mAddress);
            }
        }, intentFilter);
    }


    private void startPing(final String count, final String address){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "ping -c " + count + " -w 100 " + address  + " " + ShellUtils.execCommand("/system/bin/ping -c " + count + " -w 100 " + address + " &", false));
//                ShellUtils.execCommand("/system/bin/ping -c " + count + " -w 100 " + address + " &", false);
            }
        }).start();
    }

    private String getStrIntent(){
        return PingActivity.this.getPackageName() + ".alarm.test";
    }

    private boolean pingNetWork(String address, String count){
        String mPingIpAddressResult = "";
        try {
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c " + count + " -w 100 " + address);
            int status = p.waitFor();
            if (status == 0) {
                return true;
            } else {
                mPingIpAddressResult = "Fail :  Ip address not reachable";
            }

        } catch (Exception e) {
            Log.i("ww", "mPingIpAddressResult = " + mPingIpAddressResult + " " + e.toString());
        }
        return false;
    }

}
