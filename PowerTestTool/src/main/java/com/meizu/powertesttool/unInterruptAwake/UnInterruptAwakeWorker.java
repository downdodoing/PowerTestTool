package com.meizu.powertesttool.unInterruptAwake;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

public class UnInterruptAwakeWorker implements IWorker {

    private Context mContext;
    private boolean isWork;

    public UnInterruptAwakeWorker(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void doWork() {
        isWork = true;
    }

    @Override
    public void cancleWork() {
        isWork = false;
    }

    @Override
    public boolean getWorkStatus() {
        return isWork;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.unInterrupt_awake);
    }
}
