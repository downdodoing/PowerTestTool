package com.meizu.powertesttool.bluetoothTest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.meizu.powertesttool.R;

public class BluetoothActivity extends Activity {
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        init();
        setStateChangeListener();

    }

    private void init() {
        mSwitch = findViewById(R.id.bnt_switch);
    }

    private void setStateChangeListener() {
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openBluetooth();
                } else {
                    closeBluetooth();
                }
            }
        });
    }

    private void openBluetooth() {

    }

    /**
     * 检查当前设备是否已经开启蓝牙设备,如果没有则开启
     *
     * @return
     */
    private String isOpen() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {

        }
        return "蓝牙已经开启";
    }

    /**
     * 判断该设备是否支持蓝牙
     *
     * @return 返回支持与否
     */
    private boolean isSupport() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null == bluetoothAdapter) {
            return false;
        } else {
            return true;
        }
    }

    private void closeBluetooth() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
