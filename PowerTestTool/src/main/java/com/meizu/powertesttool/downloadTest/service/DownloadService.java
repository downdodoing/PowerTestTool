package com.meizu.powertesttool.downloadTest.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.downloadTest.DownloadTask;
import com.meizu.powertesttool.downloadTest.interfaceI.IDownloadListener;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask mDownloadTask;
    private String downloadUrl;
    private boolean isPaused;
    public static final String TAG = "DownloadService";

    private IDownloadListener mListener = new IDownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotifaction("下载", progress));
            Log.i(TAG, "onProgress: " + progress);
        }

        @Override
        public void onSuccess() {
            mDownloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotifaction("下载成功", -1));

            showToast("下载成功");

            //当下载成功后重新进行下载使下载的过程能够不断的被继续
            mDownloadTask = new DownloadTask(mListener);
            mDownloadTask.execute(downloadUrl);
            startForeground(1, getNotifaction("开始下载", 0));
        }

        @Override
        public void onFailed() {
            mDownloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotifaction("下载失败", -1));
            showToast("下载失败");
        }

        @Override
        public void onPause() {
            //mDownloadTask = null;
            isPaused = true;
            showToast("暂停下载");
        }

        @Override
        public void onCanceled() {
            mDownloadTask = null;
            stopForeground(true);
            showToast("取消下载并删除原文件");
        }
    };

    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }

    private Notification getNotifaction(String title, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, true);
        }
        return builder.build();
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    public void showToast(String message) {
        Toast.makeText(DownloadService.this, message, Toast.LENGTH_LONG).show();
    }

    public class DownloadBinder extends Binder {
        public void startDownload(String url) {
            if (null == mDownloadTask || isPaused) {
                downloadUrl = url;
                mDownloadTask = new DownloadTask(mListener);
                mDownloadTask.execute(downloadUrl);
                startForeground(1, getNotifaction("开始下载", 0));
                showToast("开始下载");
            } else {
                showToast("正在下载");
            }
        }

        public void pauseDownload() {
            if (null != mDownloadTask) {
                mDownloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (null != mDownloadTask) {
                mDownloadTask.cancelDownload();
                if (null != downloadUrl) {
                    deleteFile();
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                }
            }
        }
    }

    public void deleteFile() {
        File file = DownloadTask.getFile(downloadUrl);
        if (file.exists()) {
            file.delete();
        }
    }
}

