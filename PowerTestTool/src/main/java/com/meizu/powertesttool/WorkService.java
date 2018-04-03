package com.meizu.powertesttool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.meizu.powertesttool.alarmtest.AlarmTest;
import com.meizu.powertesttool.alwayswakeup.WakeUpWorker;
import com.meizu.powertesttool.bluetoothTest.BlueToothWorker;
import com.meizu.powertesttool.broadcasttest.BroadcastWorker;
import com.meizu.powertesttool.cpuload.HighCpuloadWorker;
import com.meizu.powertesttool.downloadTest.UninterruptedDownloadWorker;
import com.meizu.powertesttool.gps.GpsListenerWorker;
import com.meizu.powertesttool.luncherOtherApp.LuncherWorker;
import com.meizu.powertesttool.musicPlay.MusicPlayWorker;
import com.meizu.powertesttool.notification.NotificationWorker;
import com.meizu.powertesttool.pingtest.PingWorker;
import com.meizu.powertesttool.powerdataupdate.UpdateWorker;
import com.meizu.powertesttool.powerdisplay.PowerWorker;
import com.meizu.powertesttool.rainbowcolor.RainbowWorker;
import com.meizu.powertesttool.screenalwayson.ScreenOnWorker;
import com.meizu.powertesttool.sensor.SensorWorker;
import com.meizu.powertesttool.switchanimation.SwitchAnimationWorker;
import com.meizu.powertesttool.wakeupperoid.WakeUpPeroidWorker;
import com.meizu.powertesttool.wifiscan.WifiWorker;
import com.meizu.powertesttool.woker.IWorker;
import com.meizu.powertesttool.unInterruptAwake.UnInterruptAwakeWorker;


public class WorkService extends Service {

    private WorkerManager mWorkerManager;
//    private IWorker mWakeUpWorker;
//    private IWorker mWifiWorker;
//    private IWorker mScreenOnWorker;
//    private IWorker mWakeUpPeroidWorker;
//    private IWorker mRainbowWorker;
//    private IWorker alarmTest;
//    private IWorker mBroadcastWorker;
//    private IWorker mSensorWorker;
//    private IWorker mNotificationWorker;
//    private IWorker mSwitchAnimationWorker;
//    private IWorker mLuncherWorker;
//    private IWorker mPingWorker;

    private IWorker[] mWorkers;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreate", "onCreate");
//        mWakeUpWorker = new WakeUpWorker(WorkService.this);
//        mWifiWorker = new WifiWorker(WorkService.this);
//        mScreenOnWorker = new ScreenOnWorker(WorkService.this);
//        mWakeUpPeroidWorker = new WakeUpPeroidWorker(WorkService.this);
//        mRainbowWorker = new RainbowWorker(WorkService.this);
//        alarmTest = new AlarmTest(WorkService.this);
//        mBroadcastWorker = new BroadcastWorker(WorkService.this);
//        mSensorWorker = new SensorWorker(WorkService.this);
//        mNotificationWorker = new NotificationWorker(WorkService.this);
//        mSwitchAnimationWorker = new SwitchAnimationWorker(WorkService.this);
//        mLuncherWorker = new LuncherWorker(WorkService.this);
//        mPingWorker = new PingWorker(WorkService.this);

        mWorkers = new IWorker[]{new WakeUpWorker(WorkService.this), new WifiWorker(WorkService.this), new ScreenOnWorker(WorkService.this),
                new WakeUpPeroidWorker(WorkService.this), new RainbowWorker(WorkService.this), new AlarmTest(WorkService.this),
                new BroadcastWorker(WorkService.this), new SensorWorker(WorkService.this), new NotificationWorker(WorkService.this),
                new SwitchAnimationWorker(WorkService.this), new LuncherWorker(WorkService.this), new PingWorker(WorkService.this),
                new PowerWorker(WorkService.this), new UpdateWorker(WorkService.this), new HighCpuloadWorker(WorkService.this), new GpsListenerWorker(WorkService.this),
                new BlueToothWorker(WorkService.this), new UninterruptedDownloadWorker(WorkService.this), new MusicPlayWorker(WorkService.this),
                new UnInterruptAwakeWorker(WorkService.this)
        };

        mWorkerManager = WorkerManager.getInstance();

        for (IWorker worker : mWorkers) {
            mWorkerManager.addToPipeLine(worker);
        }

//        mWorkerManager.addToPipeLine(mWakeUpWorker);
//        mWorkerManager.addToPipeLine(mWifiWorker);
//        mWorkerManager.addToPipeLine(mScreenOnWorker);
//        mWorkerManager.addToPipeLine(mWakeUpPeroidWorker);
//        mWorkerManager.addToPipeLine(mRainbowWorker);
//        mWorkerManager.addToPipeLine(alarmTest);
//        mWorkerManager.addToPipeLine(mBroadcastWorker);
//        mWorkerManager.addToPipeLine(mSensorWorker);
//        mWorkerManager.addToPipeLine(mNotificationWorker);
//        mWorkerManager.addToPipeLine(mSwitchAnimationWorker);
//        mWorkerManager.addToPipeLine(mLuncherWorker);
//        mWorkerManager.addToPipeLine(mPingWorker);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
