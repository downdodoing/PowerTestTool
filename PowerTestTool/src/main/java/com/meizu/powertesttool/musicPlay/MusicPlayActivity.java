package com.meizu.powertesttool.musicPlay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.musicPlay.service.MusicPlayService;

public class MusicPlayActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private RadioButton mRadioButton;
    private RadioButton mSilentPlay;
    private RadioButton mSoundlessTrack;

    private Button mStartBnt;
    private Button mPauseBnt;
    private Button mStopBnt;

    private MusicPlayService.MusicPlayBinder mBinder;
    private ServiceConnection cnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (MusicPlayService.MusicPlayBinder) service;
            //设置默认播放哪种声音
            mBinder.setWhichWayPlay("radiobnt");
            Log.i("onServiceConnected", "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        init();
    }

    private void init() {
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, cnn, BIND_AUTO_CREATE);
        startService(intent);

        mRadioButton = findViewById(R.id.radiobnt);
        mSilentPlay = findViewById(R.id.silent_radiobnt);
        mSoundlessTrack = findViewById(R.id.soundless_track_radiobnt);

        mStartBnt = findViewById(R.id.start_bnt);
        mPauseBnt = findViewById(R.id.pause_bnt);
        mStopBnt = findViewById(R.id.stop_bnt);

        mRadioButton.setOnCheckedChangeListener(this);
        mSilentPlay.setOnCheckedChangeListener(this);
        mSoundlessTrack.setOnCheckedChangeListener(this);

        if (!MusicPlayService.isStop) {
            mStartBnt.setText("正在播放");
        }
    }

    public void clickbnt(View view) {
        switch (view.getId()) {
            case R.id.start_bnt:
                if (MusicPlayService.isStop) {
                    mBinder.play();
                    mStartBnt.setText("正在播放");
                    mPauseBnt.setText("暂停");
                    mStopBnt.setText("停止");
                } else {
                    showToast("正在播放,点击停止或者暂停后可重新播放");
                }
                break;
            case R.id.pause_bnt:
                if (!MusicPlayService.isStop) {
                    mBinder.pause();
                    mPauseBnt.setText("已暂停");
                    mStartBnt.setText("开始");
                }
                break;
            case R.id.stop_bnt:
                if (!MusicPlayService.isStop) {
                    mBinder.stop();
                    mStopBnt.setText("已停止");
                    mStartBnt.setText("开始");
                    mPauseBnt.setText("暂停");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.radiobnt:
                if (isChecked) {
                    mBinder.setWhichWayPlay("radiobnt");
                }
                break;
            case R.id.silent_radiobnt:
                if (isChecked) {
                    mBinder.setWhichWayPlay("silent");
                }
                break;
            case R.id.soundless_track_radiobnt:
                if (isChecked) {
                    mBinder.setWhichWayPlay("soundlessTrack");
                }
                break;
            default:
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(MusicPlayActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(cnn);
        //stopService(new Intent(this, MusicPlayService.class));
    }
}
