package com.meizu.powertesttool.alwayswakeup;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * 一直保持wakelock
 */
public class WakeUpWorker implements IWorker {
    private Context mContext;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    private boolean isWorking;
    public WakeUpWorker(Context context) {
        this.mContext = context;
    }

    @Override
    public void doWork() {
        Log.i("WakeUpWorker", "doWork");
        isWorking = true;
        if (pm == null)
        pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        if (wakeLock == null)
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"WakeUpWorker");
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    public void cancleWork() {
        Log.i("WakeUpWorker", "cancleWork");
        isWorking = false;
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    @Override
    public boolean getWorkStatus() {
        Log.i("WakeUpWorker", "getWorkStatus = " + isWorking);
        return isWorking;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.system_not_wake);
    }
}
