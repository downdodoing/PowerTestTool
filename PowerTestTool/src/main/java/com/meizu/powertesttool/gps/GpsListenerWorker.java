package com.meizu.powertesttool.gps;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 17-12-8.
 */

public class GpsListenerWorker implements IWorker {
    private static final String TAG = "GpsListenerWorker";
    private Context mContext;
    private boolean isWorking;

    public GpsListenerWorker(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void doWork() {
        isWorking = true;

    }

    @Override
    public void cancleWork() {
        isWorking = false;

    }

    @Override
    public boolean getWorkStatus() {
        return isWorking;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.gps_listener);
    }
}
