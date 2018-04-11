package com.meizu.powertesttool.IPC;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

public class IPCWorker implements IWorker {

    private boolean isWorker;
    private Context mContext;

    public IPCWorker(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void doWork() {
        isWorker = true;
    }

    @Override
    public void cancleWork() {
        isWorker = false;
    }

    @Override
    public boolean getWorkStatus() {
        return isWorker;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.ipc_worker);
    }
}
