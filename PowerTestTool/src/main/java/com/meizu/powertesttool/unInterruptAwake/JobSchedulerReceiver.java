package com.meizu.powertesttool.unInterruptAwake;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.meizu.powertesttool.unInterruptAwake.service.UnInterruptAwakeService;

public class JobSchedulerReceiver extends BroadcastReceiver {
    private JobScheduler mJobScheduler;

    @Override
    public void onReceive(Context context, Intent intent) {
        mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context, UnInterruptAwakeService.class));
        //设备重启后是否继续
        builder.setPersisted(true);
        //任务每1秒运行一次
        builder.setPeriodic(1000);
        //是否在充电的时候被执行
        builder.setRequiresCharging(false);
        //是否在空闲的时候被执行
        builder.setRequiresDeviceIdle(false);
        //任务最晚被执行时间
        //builder.setOverrideDeadline(3000);
        JobInfo job = builder.build();
        mJobScheduler.schedule(job);
    }
}
