package com.meizu.powertesttool.wakeupperoid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;


public class WakeUpPeroidWorker implements IWorker{

    private static final String TAG = "WakeUpPeroidWorker";
    private static final int DEFAULT_PERIOD = 2;
    private Context mContext;
    private boolean isWorking;
    private int period = DEFAULT_PERIOD;

    Intent intent;
    PendingIntent pintent;
    private AlarmManager mAlarmManager;


    public WakeUpPeroidWorker(Context context) {
        this.mContext = context;

        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        intent =new Intent(getStrIntent());
        intent.setAction("repeating");
        pintent=PendingIntent
                .getBroadcast(mContext, 0, intent, 0);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("repeating");
        intentFilter.addAction(getStrIntent());
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int count = intent.getExtras().getInt(Intent.EXTRA_ALARM_COUNT);
                String action = intent.getAction();
                Log.i(TAG, "count = " + count + " action = " + action);
            }
        } ,intentFilter);
    }

    @Override
    public void doWork() {
        Log.i(TAG, "doWork period = " + period);
        isWorking = true;

        long firstime= SystemClock.elapsedRealtime();
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, period * 60 * 1000, pintent);
    }


    @Override
    public void cancleWork() {
        isWorking = false;
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

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.wake_up_system_period);
    }

    public void setPeriod(int period){
        this.period = period;
    }

    private String getStrIntent(){
        return mContext.getPackageName() + ".alarm.test";
    }


}
