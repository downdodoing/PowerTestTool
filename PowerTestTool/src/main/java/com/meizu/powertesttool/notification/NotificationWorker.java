package com.meizu.powertesttool.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.meizu.powertesttool.MainActivity;
import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

/**
 * Created by wangwen1 on 16-2-3.
 */
public class NotificationWorker implements IWorker{
    private static final String TAG = "NotificationWorker";
    private Context mContext;
    private boolean isWorking;
    NotificationManager manager;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    int i = 0;

    public NotificationWorker(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void doWork() {
        isWorking = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (pm == null)
                        pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
                    if (wakeLock == null)
                        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"WakeUpWorker");
                    if (wakeLock != null) {
                        wakeLock.acquire();
                    }
                    Thread.sleep(1000);
                    startNotification();
//                    Thread.sleep(1000);
//                    startNotification();
//                    Thread.sleep(1000);
//                    startNotification();
                }catch (Exception e) {

                } finally {
                    if (wakeLock != null) {
                        wakeLock.release();
                        Log.i("ww", "release wakelock");
                    }
                }
            }
        }).start();


    }

    @Override
    public void cancleWork() {
        isWorking = false;
//        manager.cancel(1);
//        manager.cancel(2);
    }

    @Override
    public boolean getWorkStatus() {
        return isWorking;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.notification_worker);
    }

    private void startNotification(){
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent2 = PendingIntent.getActivity(mContext, 0,
//                new Intent(mContext, MainActivity.class), 0);
        Intent intent = new Intent("com.meizu.flyme.push.intent.MESSAGE");

        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(mContext, 0,
                intent, 0);

        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notify2 = new Notification.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                        // icon)
                .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
                        // bar上显示的提示文字
                .setContentTitle("Notification Title")// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText("This is the notification message")// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
//        notify2.flags |= Notification.FLAG_SHOW_LIGHTS;
//        notify2.flags |= Notification.FLAG_NO_CLEAR;
        notify2.flags |= Notification.FLAG_ONGOING_EVENT;
//        notify2.ledARGB = Color.WHITE;
        notify2.defaults = Notification.DEFAULT_SOUND;
//        notify2.ledOnMS = 10000;
//        notify2.ledOffMS = 1000;
        manager.notify(++i, notify2);
//        manager.notify(2, notify2);

    }
}
