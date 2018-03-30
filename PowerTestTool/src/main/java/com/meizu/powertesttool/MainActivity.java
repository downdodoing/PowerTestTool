package com.meizu.powertesttool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.meizu.powertesttool.bluetoothTest.BluetoothActivity;
import com.meizu.powertesttool.broadcasttest.BroadcastActivity;
import com.meizu.powertesttool.cpuload.HighCpuloadActivity;
import com.meizu.powertesttool.downloadTest.UnInterruptedDownloadActivity;
import com.meizu.powertesttool.gps.GpsListenerActivity;
import com.meizu.powertesttool.pingtest.PingActivity;
import com.meizu.powertesttool.powerdisplay.ui.PowerDisplayBarChart;
import com.meizu.powertesttool.rainbowcolor.RainbowActivity;
import com.meizu.powertesttool.sensor.SensorActivity;
import com.meizu.powertesttool.switchanimation.SwitchAnimationActivity;
import com.meizu.powertesttool.wakeupperoid.WakeUpPeroidWorker;
import com.meizu.powertesttool.woker.IWorker;

import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {

    private WorkerManager mWorkerManager;
    private List<IWorker> mWorkers;
    private ListView mWorkerListView;
    private WorkerAdapter mWorkerAdapter;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, WorkService.class);
        //开启Service进行数据初始化
        startService(intent);
        setContentView(R.layout.activity_main);
        mWorkerManager = WorkerManager.getInstance();
        //获取已经初始化的数据并进行显示
        mWorkers = mWorkerManager.getPipeLine();
        for (IWorker worker : mWorkers) {
            Log.i("MainActivity", "worker status = " + worker.getWorkStatus());
        }

        mWorkerListView = (ListView) findViewById(R.id.wroker_listview);
        mWorkerAdapter = new WorkerAdapter(this, mWorkers);

        mWorkerAdapter.addListener(new WorkerAdapter.WorkerSwtichListener() {
            @Override
            public void onWorkerSwitch(int position, boolean isWork) {
                Log.i("MainActivity", "position = " + position + " isWork = " + isWork);
                IWorker worker = mWorkers.get(position);
                if (position == 3) {
                    String period = mWorkerAdapter.getEditContent();
                    ((WakeUpPeroidWorker) worker).setPeriod(Integer.valueOf(period));
                } else if (position == 4) {
                    startActivity(new Intent(MainActivity.this, RainbowActivity.class));
                } else if (position == 6) {
                    startActivity(new Intent(MainActivity.this, BroadcastActivity.class));
                } else if (position == 7) {
                    startActivity(new Intent(MainActivity.this, SensorActivity.class));
                } else if (position == 9) {
                    startActivity(new Intent(MainActivity.this, SwitchAnimationActivity.class));
                } else if (position == 10) {
                    Log.i("ww", "lunch app start");
                    // 通过包名获取要跳转的app，创建intent对象
                    final Intent intent = getPackageManager().getLaunchIntentForPackage("com.meizu.lunchertest");
                    if (intent != null) {
                        // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(1000);
                                        Log.i("ww", "lunch app");
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                }
                            }
                        }).start();
                    } else {
                        // 没有安装要跳转的app应用，提醒一下
                        Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
                    }

                } else if (position == 11) {
                    startActivity(new Intent(MainActivity.this, PingActivity.class));
                } else if (position == 12) {
                    startActivity(new PowerDisplayBarChart().execute(MainActivity.this));
                    //startActivity(new Intent(MainActivity.this, PieChartBuilder.class));
                } else if (position == 14) {
                    startActivity(new Intent(MainActivity.this, HighCpuloadActivity.class));
                } else if (position == 15) {
                    startActivity(new Intent(MainActivity.this, GpsListenerActivity.class));
                } else if (position == 16) {
                    //startActivity(new Intent(MainActivity.this, BluetoothActivity.class));
                    mReceiver = new BlueToothBroadcastReceiver();
                    scanBluetooth();
                } else if (position == 17) {
                    startActivity(new Intent(MainActivity.this, UnInterruptedDownloadActivity.class));
                }
                if (isWork) {
                    worker.doWork();
                } else {
                    worker.cancleWork();
                }
            }
        });

        mWorkerListView.setAdapter(mWorkerAdapter);
    }

    private void scanBluetooth() {
        Toast.makeText(MainActivity.this, "开始扫描蓝牙设备", Toast.LENGTH_SHORT).show();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //开启当前蓝牙设备
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);//设置持续时间（最多300秒）
        startActivity(discoveryIntent);
        //开始扫描其他蓝牙设备
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mReceiver) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    class BlueToothBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                Toast.makeText(MainActivity.this, name + "-----------" + address, Toast.LENGTH_SHORT).show();
                Log.i("设备名称", device.getName() + "-----------" + device.getAddress());
            }
        }
    }
}
