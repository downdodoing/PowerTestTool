package com.meizu.powertesttool.unInterruptAwake.jobScheduler.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.unInterruptAwake.jobScheduler.JobSchedulerReceiver;
import com.meizu.powertesttool.unInterruptAwake.syncmanger.SyncMangerReceiver;

import java.util.Calendar;

public class UnInterruptAwakeService extends JobService {

    public static int i;
    public static boolean isJobServiceRun;

    public UnInterruptAwakeService() {
    }

    /**
     * @param params
     * @return 返回false表示执行完毕, 返回true表示需要开发者自己调用jobFinished方法通知系统已执行完成
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        if (isJobServiceRun) {
            Log.i("UnInterruptAwakeService", "onStartJob: " + hour + ":" + minutes + ":" + seconds);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 4; j++) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i("UnInterruptAwakeService", "JobScheduler 后台唤醒" + j);
                    }
                    //Toast.makeText(UnInterruptAwakeService.this, "JobScheduler 后台唤醒" + i, Toast.LENGTH_SHORT).show();
                }
            }).start();

            AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, JobSchedulerReceiver.class), 0);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), mPendingIntent);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
