package com.meizu.powertesttool.musicPlay.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.meizu.powertesttool.R;

import java.io.IOException;

public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private String mWhichWayPlay;
    public static boolean isStop = true;

    public static final String TAG = "MusicPlayService";

    public MusicPlayService() {
    }

    private void createMediaPlayer() {
        if (isStop) {
            //释放上一次的MediaPlayer
            releaseMediaPlayer();
            int which = R.raw.fly;
            if (mWhichWayPlay.equals("radiobnt")) {
                which = R.raw.fly;
            } else if (mWhichWayPlay.equals("silent")) {
                which = R.raw.silent;
            } else {

            }
            mMediaPlayer = MediaPlayer.create(MusicPlayService.this, which);
            mMediaPlayer.setOnCompletionListener(MusicPlayService.this);

            Log.i(TAG, "createMediaPlayer: " + mMediaPlayer.getTrackInfo().length);

            mMediaPlayer.setLooping(true);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isStop = true;
        releaseMediaPlayer();
        createMediaPlayer();
        showToast("播放完毕");
    }

    private void releaseMediaPlayer() {
        if (null != mMediaPlayer) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayBinder();
    }

    public class MusicPlayBinder extends Binder {
        public void play() {
            mMediaPlayer.start();
            isStop = false;
        }

        public void pause() {
            isStop = true;
            mMediaPlayer.pause();
        }

        public void stop() {
            isStop = true;
            mMediaPlayer.stop();
            //重新进行创建便于下次播放
            createMediaPlayer();
        }

        public void setWhichWayPlay(String whichWayPlay) {
            mWhichWayPlay = whichWayPlay;
            createMediaPlayer();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    private void showToast(String message) {
        Toast.makeText(MusicPlayService.this, message, Toast.LENGTH_SHORT).show();
    }
}
