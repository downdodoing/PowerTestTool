package com.meizu.powertesttool.unInterruptAwake.jobScheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.unInterruptAwake.jobScheduler.service.UnInterruptAwakeService;

public class JobSchedulerReceiver extends BroadcastReceiver {
    private JobScheduler mJobScheduler;

    @Override
    public void onReceive(Context context, Intent intent) {
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context, UnInterruptAwakeService.class));
        //设备重启后是否继续
        builder.setPersisted(true);
        //任务每5秒运行一次
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(5000);
        } else {
            builder.setPeriodic(5000);
        }

        //是否在充电的时候被执行
        builder.setRequiresCharging(false);
        //是否在空闲的时候被执行
        builder.setRequiresDeviceIdle(false);
        //任务最晚被执行时间
        //builder.setOverrideDeadline(3000);
        JobInfo job = builder.build();
        mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.schedule(job);
    }
}
