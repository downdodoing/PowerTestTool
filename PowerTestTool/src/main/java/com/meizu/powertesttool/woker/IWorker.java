package com.meizu.powertesttool.woker;

/**
 * Created by wangwen1 on 15-11-23.
 */
public interface IWorker {
    public void doWork();
    public void cancleWork();
    public boolean getWorkStatus();
    public String getWorkerName();
}
