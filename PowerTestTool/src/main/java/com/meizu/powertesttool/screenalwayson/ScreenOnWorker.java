package com.meizu.powertesttool.screenalwayson;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

public class ScreenOnWorker implements IWorker{
    private static final String TAG = "ScreenOnWorker";
    private Context mContext;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    private boolean isWorking;
    public ScreenOnWorker(Context context) {
        this.mContext = context;
    }
    @Override
    public void doWork() {
        Log.i(TAG, "doWork");
        isWorking = true;
        if (pm == null)
            pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        if (wakeLock == null)
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    public void cancleWork() {
        Log.i(TAG, "cancleWork");
        isWorking = false;
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    @Override
    public boolean getWorkStatus() {
        Log.i(TAG, "getWorkStatus = " + isWorking);
        return isWorking;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.screen_always_on);
    }
}
