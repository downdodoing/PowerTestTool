package com.meizu.powertesttool.alarmtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;

import com.meizu.flyme.SettingsFlyme;
import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangwen1 on 16-1-11.
 */
public class AlarmTest implements IWorker{

    private static final String TAG = "AlarmTest";
    private static final int DEFAULT_PERIOD = 2;
    private Context mContext;
    private boolean isWorking;
    private int period = DEFAULT_PERIOD;

    Intent intent;
    PendingIntent pintent;
    private AlarmManager mAlarmManager;


    private Timer mTimer;
    private TimerTask mTimerTask;
    private static final int SCAN_PERIOD = 10*1000;

    public AlarmTest(Context context){
        mContext = context;
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void doWork() {
        Log.i(TAG, "doWork period = " + period);
        isWorking = true;
        if (mTimer == null)
            mTimer = new Timer();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i = 0; i < 1000 ; i++) {
//                    mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() - i * 100000, getPintent());
//                    Log.i(TAG, "i = " + i + "  time = " + (System.currentTimeMillis() - i * 100000));
//                    try{
//                        Thread.sleep(1000);
//                    }catch (Exception e) {
//
//                    }
//
//                }
//            }
//        }).start();

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, getPintent());
//        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60 * 60 * 1000, getPintent());
//        mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60 * 60 * 1000, getPintent());

        mTimerTask =  new TimerTask() {
            @Override
            public void run() {
                //mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, getPintent());
            }
        };
        //mTimer.schedule(mTimerTask, 0, SCAN_PERIOD);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("alarm_test_for_m");
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("ww", "get test alarm");
            }
        }, intentFilter);
//        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, getPintent());
    }

    @Override
    public void cancleWork() {
        isWorking = false;
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
        mTimerTask.cancel();
        if(pintent != null){
            mAlarmManager.cancel(pintent);
            pintent = null;
        }
    }

    @Override
    public boolean getWorkStatus() {
        Log.i(TAG, "getWorkStatus = " + isWorking);
        return isWorking;
    }

    public PendingIntent getPintent(){
        intent = new Intent(mContext, TestBroadReceiver.class);
//        intent = new Intent();
//        intent.setAction("alarm_test_for_m");
//        intent.setAction("alarmtest" + System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        return pendingIntent;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.alarm_test);
    }
}
