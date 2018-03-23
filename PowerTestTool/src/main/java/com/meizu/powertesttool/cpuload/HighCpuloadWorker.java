package com.meizu.powertesttool.cpuload;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 17-11-28.
 */

public class HighCpuloadWorker implements IWorker{
    private static final String TAG = "HighCpuloadWorker";
    private Context mContext;
    private boolean isWorking;

    public HighCpuloadWorker(Context mContext) {
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
        return mContext.getResources().getString(R.string.high_cpu_load);
    }
}
