package com.meizu.powertesttool.switchanimation;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 16-2-3.
 */
public class SwitchAnimationWorker implements IWorker{
    private static final String TAG = "SwitchAnimationWorker";
    private Context mContext;
    private boolean isWorking;

    public SwitchAnimationWorker(Context mContext) {
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
        return mContext.getResources().getString(R.string.switch_animation);
    }
}
