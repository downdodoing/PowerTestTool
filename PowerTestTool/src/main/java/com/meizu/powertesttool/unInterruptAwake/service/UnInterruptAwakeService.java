package com.meizu.powertesttool.unInterruptAwake.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.meizu.powertesttool.unInterruptAwake.AlarmOperate;

public class UnInterruptAwakeService extends JobService {
    private static int i;

    public UnInterruptAwakeService() {
    }

    /**
     * @param params
     * @return 返回false表示执行完毕, 返回true表示需要开发者自己调用jobFinished方法通知系统已执行完成
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("UnInterruptAwakeService", "onStartJob: " + (i++));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }
}
