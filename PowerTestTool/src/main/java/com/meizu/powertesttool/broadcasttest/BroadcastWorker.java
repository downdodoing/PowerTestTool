package com.meizu.powertesttool.broadcasttest;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 16-1-25.
 */
public class BroadcastWorker implements IWorker{
    private static final String TAG = "BroadcastWorker";
    private Context mContext;
    private boolean isWorking;

    public BroadcastWorker(Context mContext) {
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
        return mContext.getResources().getString(R.string.broadcast_sender);
    }
}
