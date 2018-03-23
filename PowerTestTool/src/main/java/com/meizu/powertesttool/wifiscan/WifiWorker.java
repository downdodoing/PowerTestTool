package com.meizu.powertesttool.wifiscan;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

import java.util.Timer;
import java.util.TimerTask;

public class WifiWorker implements IWorker{
    //定义一个WifiManager对象
    private WifiManager mWifiManager;
    private Context mContext;
    private boolean isWorking;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private static final int SCAN_PERIOD = 10*1000;
    public WifiWorker(Context context){
        //取得WifiManager对象
        this.mContext = context;
        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void doWork() {
        Log.i("WifiWorker", "doWork");
        isWorking = true;
        if (mTimer == null)
            mTimer = new Timer();
        openWifi();
        mTimerTask =  new TimerTask() {
            @Override
            public void run() {
                startScan();
            }
        };
        mTimer.schedule(mTimerTask, 0, SCAN_PERIOD);
    }

    @Override
    public void cancleWork() {
        Log.i("WifiWorker", "cancleWork");
        isWorking = false;
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
        mTimerTask.cancel();
    }

    @Override
    public boolean getWorkStatus() {
        Log.i("WifiWorker", "getWorkStatus = " + isWorking);
        return isWorking;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.wifi_scan_per10);
    }

    //打开wifi
    private void openWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }

    private void startScan(){
        Log.i("WifiWorker", "startScan");
        mWifiManager.startScan();
    }

}

