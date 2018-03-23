package com.meizu.powertesttool.bluetoothTest;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

public class BlueToothWorker implements IWorker {
    private Context mContext;
    private boolean isWork;

    public BlueToothWorker(Context mContext) {
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

    public String getWorkerName() {
        return mContext.getResources().getString(R.string.scan_blueTooth);
    }
}
