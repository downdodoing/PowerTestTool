package com.meizu.powertesttool.broadcasttest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.utils.LocationUtils;
import com.meizu.powertesttool.utils.ShellUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangwen1 on 16-1-25.
 */
public class BroadcastActivity extends Activity{

//关闭或打开飞行模式时的广播
    public static final String ACTION_AIRPLANE_MODE_CHANGED = Intent.ACTION_AIRPLANE_MODE_CHANGED;

//充电状态，或者电池的电量发生变化//电池的充电状态、电荷级别改变，不能通过组建声明接收这个广播，只有通过Context.registerReceiver()注册
    public static final String ACTION_BATTERY_CHANGED = Intent.ACTION_BATTERY_CHANGED;

//表示电池电量低
    public static final String ACTION_BATTERY_LOW = Intent.ACTION_BATTERY_LOW;

//表示电池电量充足，即从电池电量低变化到饱满时会发出广播
    public static final String ACTION_BATTERY_OKAY = Intent.ACTION_BATTERY_OKAY;

//在系统启动完成后，这个动作被广播一次（只有一次）。
    public static final String ACTION_BOOT_COMPLETED = Intent.ACTION_BOOT_COMPLETED;

//按下照相时的拍照按键(硬件按键)时发出的广播
    public static final String ACTION_CAMERA_BUTTON = Intent.ACTION_CAMERA_BUTTON;

//当屏幕超时进行锁屏时,当用户按下电源按钮,长按或短按(不管有没跳出话框)，进行锁屏时,android系统都会广播此Action消息
    public static final String ACTION_CLOSE_SYSTEM_DIALOGS = Intent.ACTION_CLOSE_SYSTEM_DIALOGS;

//设备当前设置被改变时发出的广播(包括的改变:界面语言，设备方向，等，请参考Configuration.java)
    public static final String ACTION_CONFIGURATION_CHANGED = Intent.ACTION_CONFIGURATION_CHANGED;

//设备日期发生改变时会发出此广播
    public static final String ACTION_DATE_CHANGED = Intent.ACTION_DATE_CHANGED;

//设备内存不足时发出的广播,此广播只能由系统使用，其它APP不可用
    public static final String ACTION_DEVICE_STORAGE_LOW = Intent.ACTION_DEVICE_STORAGE_LOW;

//设备内存从不足到充足时发出的广播,此广播只能由系统使用，其它APP不可用
    public static final String ACTION_DEVICE_STORAGE_OK = Intent.ACTION_DEVICE_STORAGE_OK;

//发出此广播的地方frameworks\base\services\java\com\android\server\DockObserver.java
    public static final String ACTION_DOCK_EVENT = Intent.ACTION_DOCK_EVENT;

//移动APP完成之后，发出的广播(移动是指:APP2SD)
    public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE;

//正在移动APP时，发出的广播(移动是指:APP2SD)
    public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE;

//Gtalk已建立连接时发出的广播
    public static final String ACTION_GTALK_SERVICE_CONNECTED = Intent.ACTION_GTALK_SERVICE_CONNECTED;

//Gtalk已断开连接时发出的广播
    public static final String ACTION_GTALK_SERVICE_DISCONNECTED = Intent.ACTION_GTALK_SERVICE_DISCONNECTED;

//在耳机口上插入耳机时发出的广播
    public static final String ACTION_HEADSET_PLUG = Intent.ACTION_HEADSET_PLUG;

//改变输入法时发出的广播
    public static final String ACTION_INPUT_METHOD_CHANGED = Intent.ACTION_INPUT_METHOD_CHANGED;

//设备当前区域设置已更改时发出的广播
    public static final String ACTION_LOCALE_CHANGED = Intent.ACTION_LOCALE_CHANGED;

//表示用户和包管理所承认的低内存状态通知应该开始。
    public static final String ACTION_MANAGE_PACKAGE_STORAGE = Intent.ACTION_MANAGE_PACKAGE_STORAGE;

//未正确移除SD卡(正确移除SD卡的方法:设置--SD卡和设备内存--卸载SD卡)，但已把SD卡取出来时发出的广播 ,扩展介质（扩展卡）已经从 SD 卡插槽拔出，但是挂载点 (mount point) 还没解除 (unmount)
    public static final String ACTION_MEDIA_BAD_REMOVAL = Intent.ACTION_MEDIA_BAD_REMOVAL;

//按下"Media Button" 按键时发出的广播,假如有"Media Button" 按键的话(硬件按键)
    public static final String ACTION_MEDIA_BUTTON = Intent.ACTION_MEDIA_BUTTON;

//插入外部储存装置，比如SD卡时，系统会检验SD卡，此时发出的广播?
    public static final String ACTION_MEDIA_CHECKING = Intent.ACTION_MEDIA_CHECKING;

//已拔掉外部大容量储存设备发出的广播（比如SD卡，或移动硬盘）,不管有没有正确卸载都会发出此广播, 用户想要移除扩展介质（拔掉扩展卡）。
    public static final String ACTION_MEDIA_EJECT = Intent.ACTION_MEDIA_EJECT;

//插入SD卡并且已正确安装（识别）时发出的广播, 扩展介质被插入，而且已经被挂载。
    public static final String ACTION_MEDIA_MOUNTED = Intent.ACTION_MEDIA_MOUNTED;

//拓展介质存在，但使用不兼容FS（或为空）的路径安装点检查介质包含在Intent.mData领域。
    public static final String ACTION_MEDIA_NOFS = Intent.ACTION_MEDIA_NOFS;

//外部储存设备已被移除，不管有没正确卸载,都会发出此广播， 扩展介质被移除。
    public static final String ACTION_MEDIA_REMOVED = Intent.ACTION_MEDIA_REMOVED;

//广播：已经扫描完介质的一个目录
    public static final String ACTION_MEDIA_SCANNER_FINISHED = Intent.ACTION_MEDIA_SCANNER_FINISHED;

//请求媒体扫描仪扫描文件并将其添加到媒体数据库。
    public static final String ACTION_MEDIA_SCANNER_SCAN_FILE = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;

//广播：开始扫描介质的一个目录
    public static final String ACTION_MEDIA_SCANNER_STARTED = Intent.ACTION_MEDIA_SCANNER_STARTED;

// 广播：扩展介质的挂载被解除 (unmount)，因为它已经作为 USB 大容量存储被共享。
    public static final String ACTION_MEDIA_SHARED = Intent.ACTION_MEDIA_SHARED;

    public static final String ACTION_MEDIA_UNMOUNTABLE = Intent.ACTION_MEDIA_UNMOUNTABLE;//

// 广播：扩展介质存在，但是还没有被挂载 (mount)
    public static final String ACTION_MEDIA_UNMOUNTED = Intent.ACTION_MEDIA_UNMOUNTED;

    public static final String ACTION_NEW_OUTGOING_CALL = Intent.ACTION_NEW_OUTGOING_CALL;

//成功的安装APK之后//广播：设备上新安装了一个应用程序包。//一个新应用包已经安装在设备上，数据包括包名（最新安装的包程序不能接收到这个广播）
    public static final String ACTION_PACKAGE_ADDED = Intent.ACTION_PACKAGE_ADDED;

//一个已存在的应用程序包已经改变，包括包名
    public static final String ACTION_PACKAGE_CHANGED = Intent.ACTION_PACKAGE_CHANGED;

//清除一个应用程序的数据时发出的广播(在设置－－应用管理－－选中某个应用，之后点清除数据时?)//用户已经清除一个包的数据，包括包名（清除包程序不能接收到这个广播）
    public static final String ACTION_PACKAGE_DATA_CLEARED = Intent.ACTION_PACKAGE_DATA_CLEARED;

//触发一个下载并且完成安装时发出的广播，比如在电子市场里下载应用？
    public static final String ACTION_PACKAGE_INSTALL = Intent.ACTION_PACKAGE_INSTALL;

//成功的删除某个APK之后发出的广播, 一个已存在的应用程序包已经从设备上移除，包括包名（正在被安装的包程序不能接收到这个广播）
    public static final String ACTION_PACKAGE_REMOVED = Intent.ACTION_PACKAGE_REMOVED;

//替换一个现有的安装包时发出的广播（不管现在安装的APP比之前的新还是旧，都会发出此广播？）
    public static final String ACTION_PACKAGE_REPLACED = Intent.ACTION_PACKAGE_REPLACED;

//用户重新开始一个包，包的所有进程将被杀死，所有与其联系的运行时间状态应该被移除，包括包名（重新开始包程序不能接收到这个广播）
    public static final String ACTION_PACKAGE_RESTARTED = Intent.ACTION_PACKAGE_RESTARTED;

//插上外部电源时发出的广播
    public static final String ACTION_POWER_CONNECTED = Intent.ACTION_POWER_CONNECTED;

//已断开外部电源连接时发出的广播
    public static final String ACTION_POWER_DISCONNECTED = Intent.ACTION_POWER_DISCONNECTED;

    public static final String ACTION_PROVIDER_CHANGED = Intent.ACTION_PROVIDER_CHANGED;//

//重启设备时的广播
    public static final String ACTION_REBOOT = Intent.ACTION_REBOOT;

//屏幕被关闭之后的广播
    public static final String ACTION_SCREEN_OFF = Intent.ACTION_SCREEN_OFF;

//屏幕被打开之后的广播
    public static final String ACTION_SCREEN_ON = Intent.ACTION_SCREEN_ON;

//关闭系统时发出的广播
    public static final String ACTION_SHUTDOWN = Intent.ACTION_SHUTDOWN;

//时区发生改变时发出的广播
    public static final String ACTION_TIMEZONE_CHANGED = Intent.ACTION_TIMEZONE_CHANGED;

//时间被设置时发出的广播
    public static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;

//广播：当前时间已经变化（正常的时间流逝）， 当前时间改变，每分钟都发送，不能通过组件声明来接收，只有通过Context.registerReceiver()方法来注册
    public static final String ACTION_TIME_TICK = Intent.ACTION_TIME_TICK;

//一个用户ID已经从系统中移除发出的广播
    public static final String ACTION_UID_REMOVED = Intent.ACTION_UID_REMOVED;

//设备已进入USB大容量储存状态时发出的广播？
    public static final String  ACTION_UMS_CONNECTED = Intent.ACTION_UMS_CONNECTED;

//设备已从USB大容量储存状态转为正常状态时发出的广播？
    public static final String  ACTION_UMS_DISCONNECTED = Intent.ACTION_UMS_DISCONNECTED;

    public static final String  ACTION_USER_PRESENT = Intent.ACTION_USER_PRESENT;//

//设备墙纸已改变时发出的广播
    public static final String  ACTION_WALLPAPER_CHANGED = Intent.ACTION_WALLPAPER_CHANGED;

    private EditText mBroadcastNameEdit;
    private EditText mBroadcastPeriodEdit;
    private Button mSendButton;
    private Button mTimesetButton;
    private Button mWifiButton;
    private Button mModemButton;
    private Button mScreenOnButton;
    private Button mGpsButton;
    private Button mBatteryButton;

    private WifiManager mWm;
    private static final String DEFAULT_PERIOD = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_layout);
        mBroadcastNameEdit = (EditText) findViewById(R.id.broadcast_name_edit);
        mBroadcastPeriodEdit = (EditText) findViewById(R.id.broadcast_period_edit);
        mSendButton = (Button) findViewById(R.id.broadcast_send_button);
        mTimesetButton = (Button) findViewById(R.id.broadcast_time_set);
        mWifiButton = (Button) findViewById(R.id.broadcast_wifi);
        mModemButton = (Button) findViewById(R.id.broadcast_modem);
        mScreenOnButton = (Button) findViewById(R.id.broadcast_screen_on);
        mGpsButton = (Button) findViewById(R.id.broadcast_gps);
        mBatteryButton = (Button) findViewById(R.id.broadcast_battery);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
//                alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "TEST ALARM");
//                alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, 11);
//                alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, 12);
//                alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
//                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(alarmIntent);
                sendBroadcast();
            }
        });

        mTimesetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String period = mBroadcastPeriodEdit.getText().toString();
                        if (TextUtils.isEmpty(period) || period.equals("0")) {
                            period = DEFAULT_PERIOD;
                        }
                        Log.i("ww", "period = " + period);
                        ShellUtils.execCommand("/system/bin/sh /sdcard/timeset.sh " + period + " &", false);
                    }
                }).start();
            }
        });

        mWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String period = mBroadcastPeriodEdit.getText().toString();
                        if (TextUtils.isEmpty(period) || period.equals("0")) {
                            period = DEFAULT_PERIOD;
                        }
                        Log.i("ww", "period = " + period);
                        ShellUtils.execCommand("/system/bin/sh /sdcard/wifi.sh " + period + " &", false);
                    }
                }).start();
            }
        });

        mModemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String period = mBroadcastPeriodEdit.getText().toString();
                        if (TextUtils.isEmpty(period) || period.equals("0")) {
                            period = DEFAULT_PERIOD;
                        }
                        Log.i("ww", "period = " + period);
                        if (!isMobileThreadRun()){
                            setMobileThreadRun(true);
                            mMobleDataThread = new Thread(){
                                boolean status = false;
                                @Override
                                public void run() {
                                    super.run();
                                    while (isMobileThreadRun) {
                                        setMobileDataEnabled(status);
                                        status = !status;
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            };
                            mMobleDataThread.start();

                        } else {
                            setMobileThreadRun(false);
                        }
                        //ShellUtils.execCommand("/system/bin/sh /sdcard/modem.sh " + period + " &", false);
                    }
                }).start();
            }
        });

        mScreenOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String period = mBroadcastPeriodEdit.getText().toString();
                        if (TextUtils.isEmpty(period) || period.equals("0")) {
                            period = DEFAULT_PERIOD;
                        }
                        Log.i("ww", "period = " + period);
                        ShellUtils.execCommand("/system/bin/sh /sdcard/screenon.sh " + period + " &", false);
                    }
                }).start();
            }
        });

        mGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String period = mBroadcastPeriodEdit.getText().toString();
                        if (TextUtils.isEmpty(period) || period.equals("0")) {
                            period = DEFAULT_PERIOD;
                        }
                        Log.i("ww", "period = " + period);
                        if (!isGpsThreadRun()){
                            setGpsThreadRun(true);
                            mGpsThread = new Thread(){
                                boolean status = false;
                                @Override
                                public void run() {
                                    super.run();
                                    while (isGpsThreadRun) {
                                        setGpsEnabled(status);
                                        status = !status;
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            };
                            mGpsThread.start();

                        } else {
                            setGpsThreadRun(false);
                        }
                        //ShellUtils.execCommand("/system/bin/sh /sdcard/location.sh " + period + " &", false);
                    }
                }).start();
            }
        });

        mBatteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String period = mBroadcastPeriodEdit.getText().toString();
                        if (TextUtils.isEmpty(period) || period.equals("0")) {
                            period = DEFAULT_PERIOD;
                        }
                        Log.i("ww", "period = " + period);
                        ShellUtils.execCommand("/system/bin/sh /sdcard/battery.sh " + period + " &", false);
                    }
                }).start();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
//        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
//        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
//        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//
//        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
//        intentFilter.addAction(LocationManager.MODE_CHANGED_ACTION);
//
//        intentFilter.addAction("android.intent.action.ACTION_MOBILE_DATA_ENABLE");
//        intentFilter.addAction("com.android.server.action.NETWORK_STATS_UPDATED");
//        intentFilter.addAction("android.intent.action.ACTION_RAT_CHANGED");
//        intentFilter.addAction("mediatek.intent.action.ACTION_HIDE_NETWORK_STATE");
//
//        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
//        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
//        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        intentFilter.addAction("com.meizu.ww");

        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("ww", "action = " + intent.getAction());
                if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int plugType = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,BatteryManager.BATTERY_PLUGGED_AC);
                    int stats = intent.getIntExtra(BatteryManager.EXTRA_STATUS,BatteryManager.BATTERY_STATUS_CHARGING);
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                    int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
                    long currentTime = SystemClock.elapsedRealtime();
                    if (lastLevel != level) {
                        lastLevel = level;
                        Log.d("ww", "onReceive: duration = " + (currentTime - lastTime)/1000);
                        writeToFile("onReceive: duration = " + (currentTime - lastTime)/1000 + "s");
                        lastTime = currentTime;
                    }
                    writeToFile("onReceive: plugType = " + plugType + " stats = " + stats + " level = " + level + " scale = " + scale + " voltage = " + voltage);
                    Log.d("ww", "onReceive: plugType = " + plugType + " stats = " + stats + " level = " + level + " scale = " + scale + " voltage = " + voltage);
                }

                //Log.i("ww", "extra = " + intent.getExtras().toString());
            }
        }, intentFilter);
    }
    static int lastLevel = 0;
    static long lastTime = SystemClock.elapsedRealtime();

    private void sendBroadcast(){
        String broadcastName = mBroadcastNameEdit.getText().toString();
        String broadcastPeriod = mBroadcastPeriodEdit.getText().toString();
        sendBroadcast(new Intent(broadcastName));
    }

    /**
     * 是否开启 wifi true：开启 false：关闭
     *
     * 一定要加入权限： <uses-permission
     * android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * <uses-permission
     * android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
     *
     *
     * @param isEnable
     */
    public void setWifi(boolean isEnable) {

        //
        if (mWm == null) {
            mWm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            return;
        }

        System.out.println("wifi====" + mWm.isWifiEnabled());
        if (isEnable) {// 开启wifi
            if (!mWm.isWifiEnabled()) {
                mWm.setWifiEnabled(true);
            }
        } else {
            // 关闭 wifi
            if (mWm.isWifiEnabled()) {
                mWm.setWifiEnabled(false);
            }
        }
    }

    /**
     * 设置数据网络开关
     * @param enabled
     * @return
     */
    public boolean setMobileDataEnabled(boolean enabled) {
        Log.i("ww", "setMobileDataEnabled = " + enabled);
        final TelephonyManager mTelManager;
        mTelManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method m = mTelManager.getClass().getDeclaredMethod("getITelephony"); m.setAccessible(true);
            Object telephony = m.invoke(mTelManager);
            m = telephony.getClass().getMethod((enabled ? "enable" : "disable") + "DataConnectivity");
            m.invoke(telephony);
            return true;
        } catch (Exception e) {
            Log.e("", "cannot fake telephony", e);
            return false;
        }
    }

    /**
     * 设置GPS开关状态
     * @param enabled
     */
    public void setGpsEnabled(boolean enabled){

        boolean gpsEnabled = LocationUtils.isLocationEnable(BroadcastActivity.this);
        Log.i("ww", "setGpsEnabled = " + enabled + " gpsEnabled = " + gpsEnabled);
        if (gpsEnabled) {
            if (!enabled) {
                LocationUtils.setLocationMode(BroadcastActivity.this, Settings.Secure.LOCATION_MODE_OFF);
            }
        } else {
            if (enabled) {
                LocationUtils.setLocationMode(BroadcastActivity.this, Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
            }
        }
    }

    public boolean isGpsThreadRun = false;
    public boolean isGpsThreadRun(){
        return isGpsThreadRun;
    }

    public void setGpsThreadRun(boolean isGpsThreadRun){
        this.isGpsThreadRun = isGpsThreadRun;
    }

    public Thread mGpsThread = new Thread(){
        boolean status = false;
        @Override
        public void run() {
            super.run();
            while (isGpsThreadRun) {
                setGpsEnabled(status);
                status = !status;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public boolean isMobileThreadRun = false;
    public boolean isMobileThreadRun(){
        return isMobileThreadRun;
    }

    public void setMobileThreadRun(boolean isMobileThreadRun){
        this.isMobileThreadRun = isMobileThreadRun;
    }

    Thread mMobleDataThread = new Thread(){
        boolean status = false;
        @Override
        public void run() {
            super.run();
            while (isMobileThreadRun) {
                setMobileDataEnabled(status);
                status = !status;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    public static String getSdCardPath() {
        boolean exist = isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdpath = "不适用";
        }
        return sdpath;

    }


    /**
     * 获取默认的文件路径
     *
     * @return
     */
    public static String getDefaultFilePath() {
        String filepath = "";
        File file = new File(Environment.getExternalStorageDirectory(),
                "battery_level.txt");
        if (file.exists()) {
            filepath = file.getAbsolutePath();
        } else {
            filepath = "不适用";
        }
        return filepath;
    }

    public static final String DEFAULT_FILENAME = "battery_level.txt";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void writeToFile(String message) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    DEFAULT_FILENAME);
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
//            String info = " hey, yoo,bitch";
            String dateStr = sdf.format(new Date(System.currentTimeMillis()));
            bw.write(dateStr + ": " + message);
            bw.write("\r\n");
            bw.flush();
            Log.d("ww", "writeToFile: 写入成功 " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
