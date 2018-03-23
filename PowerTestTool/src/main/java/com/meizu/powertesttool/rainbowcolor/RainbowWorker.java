package com.meizu.powertesttool.rainbowcolor;

import android.content.Context;
import android.content.Intent;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 15-12-23.
 */
public class RainbowWorker implements IWorker{

    private static final String TAG = "RainbowWorker";
    private Context mContext;
    private boolean isWorking;

    public RainbowWorker(Context mContext) {
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
        return mContext.getResources().getString(R.string.rainbow_color);
    }
}
