package com.meizu.powertesttool;


import android.content.Context;
import android.util.Log;

import com.meizu.powertesttool.woker.IWorker;

import java.util.ArrayList;
import java.util.List;

public class WorkerManager {
    private Context mContext;
    private volatile static WorkerManager mInstance= null;
    private List<IWorker> mPipeLine = null;

    public static WorkerManager getInstance() {
        if (mInstance == null) {
            synchronized (WorkerManager.class) {
                if (mInstance == null) {
                    mInstance = new WorkerManager();
                }
            }
        }
        return mInstance;
    }

    private WorkerManager() {
        Log.i("WorkerManager", "WorkerManager");
        mPipeLine = new ArrayList<IWorker>();
    }

    public void addToPipeLine(IWorker worker){
        Log.i("WorkerManager", "addToPipeLine");
        mPipeLine.add(worker);
    }

    public void doWorkInPipeLine(){
        for (IWorker worker : mPipeLine) {
            worker.doWork();
        }
    }

    public void canclePipeLine(){
        for (IWorker worker : mPipeLine) {
            worker.cancleWork();
        }
    }

    public List<IWorker> getPipeLine(){
        return mPipeLine;
    }

    public void removeFormPipeLine(IWorker worker){
        mPipeLine.remove(worker);
    }
}
