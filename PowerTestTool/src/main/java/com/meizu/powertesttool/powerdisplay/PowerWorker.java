package com.meizu.powertesttool.powerdisplay;

import android.content.Context;
import android.util.Log;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.powerdisplay.gaugedata.GaugeDataManager;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 16-3-30.
 */
public class PowerWorker implements IWorker {
    private Context mContext;

    private boolean isWorking;
    public PowerWorker(Context context) {
        this.mContext = context;
        GaugeDataManager.getInstance(mContext);
    }

    @Override
    public void doWork() {
        Log.i("PowerWorker", "doWork");
        isWorking = true;
        //GaugeDataManager.getInstance(mContext).loadAllDbUids();
        GaugeDataManager.getInstance(mContext).loadBatteryList();
    }

    @Override
    public void cancleWork() {
        Log.i("PowerWorker", "cancleWork");
        isWorking = false;

    }

    @Override
    public boolean getWorkStatus() {
        Log.i("PowerWorker", "getWorkStatus = " + isWorking);
        return isWorking;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.power_display);
    }
}
