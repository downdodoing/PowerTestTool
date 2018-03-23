package com.meizu.powertesttool.pingtest;

import android.content.Context;
import android.util.Log;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 16-2-3.
 */
public class PingWorker implements IWorker{
    private static final String TAG = "PingWorker";
    private Context mContext;
    private boolean isWorking;

    public PingWorker(Context mContext) {
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
        Log.i(TAG, "getWorkStatus = " + isWorking);
        return isWorking;    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.ping_network);
    }

}
