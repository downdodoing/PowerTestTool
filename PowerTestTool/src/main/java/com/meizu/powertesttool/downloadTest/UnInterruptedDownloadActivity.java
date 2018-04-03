package com.meizu.powertesttool.downloadTest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.downloadTest.service.DownloadService;

public class UnInterruptedDownloadActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private UnInterruptedDownloadActivity activity;

    private RadioButton mRadioButton50;
    private RadioButton mRadioButton100;
    private RadioButton mRadioButton1M;

    private boolean isChecked50, isChecked100, isChecked1M;

    private DownloadService.DownloadBinder mDownloadBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_interrupted_download);

        init();
    }

    private void init() {
        activity = this;

        Intent intent = new Intent(activity, DownloadService.class);
        startService(intent);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        mRadioButton50 = findViewById(R.id.download_radioButton);
        mRadioButton100 = findViewById(R.id.download_radioButton1);
        mRadioButton1M = findViewById(R.id.download_radioButton2);

        mRadioButton50.setOnCheckedChangeListener(this);
        mRadioButton100.setOnCheckedChangeListener(this);
        mRadioButton1M.setOnCheckedChangeListener(this);

        mSharedPreferences = getSharedPreferences("radioBnt", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        initRadioBnt(mSharedPreferences.getString("radioBnt", "third"));
    }

    private void initRadioBnt(String which) {
        if ("first".equals(which)) {
            mRadioButton50.setChecked(true);
        } else if ("second".equals(which)) {
            mRadioButton100.setChecked(true);
        } else {
            mRadioButton1M.setChecked(true);
        }
    }

    public void bntClick(View view) {
        switch (view.getId()) {
            case R.id.start_bnt:
                if (!isChecked50 && !isChecked100 && !isChecked1M) {
                    showToast("请选择下载速率");
                } else {
                    mDownloadBinder.startDownload("https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe");
                }
                break;
            case R.id.stop_bnt:
                mDownloadBinder.cancelDownload();
                break;
            case R.id.pause_bnt:
                mDownloadBinder.pauseDownload();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.download_radioButton:
                isChecked50 = isChecked;
                if (isChecked) {
                    DownloadTask.mPerLengthSeconds = 50;
                    mEditor.putString("radioBnt", "first");
                }
                break;
            case R.id.download_radioButton1:
                isChecked100 = isChecked;
                if (isChecked) {
                    DownloadTask.mPerLengthSeconds = 100;
                    mEditor.putString("radioBnt", "second");
                }
                break;
            case R.id.download_radioButton2:
                isChecked1M = isChecked;
                if (isChecked) {
                    DownloadTask.mPerLengthSeconds = 1024;
                    mEditor.putString("radioBnt", "third");
                }
                break;
            default:
                break;
        }
        mEditor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
