package com.meizu.powertesttool.sensor;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 16-2-1.
 */
public class SensorWorker implements IWorker{
    private static final String TAG = "SensorWorker";
    private Context mContext;
    private boolean isWorking;

    public SensorWorker(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void doWork() {
        isWorking = true;
        SensorTestManager sensorTestManager = new SensorTestManager();
        sensorTestManager.getSensors(mContext);
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
        return mContext.getResources().getString(R.string.sensor_test);
    }
}
