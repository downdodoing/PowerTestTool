
package com.meizu.powertesttool.powerdisplay.gaugedata;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.meizu.power.IPower;
import com.meizu.power.powergauge.RemoteDetaPowerSw;
import com.meizu.power.powergauge.RemoteDetaTimeStats;
import com.meizu.powertesttool.powerdisplay.gaugestore.Constants;

import java.util.ArrayList;
import java.util.List;


public class GaugeServiceWrapper {

    private static final String TAG = "PowerGaugeClient";
    private static final boolean DEBUG = false;

  //  private static PowerServiceWrapper mInstance;

    private static Context mContext;

    private static IPower mIPower = null;

    private static PowerServiceConnectStateListener mPowerServiceConnectStateListener;


    public GaugeServiceWrapper(Context context) {
        mContext = context.getApplicationContext();
        connectPowerGauge();
    }



    private static void connectPowerGauge(){
        Intent intent = new Intent(Constants.ACTION_START_POWER);
        intent.setPackage(Constants.PACKAGE_NAME_SAFE_CENTER);
        //mContext.bindService(intent, connGauge, Service.BIND_AUTO_CREATE);

    }


    private static ServiceConnection connGauge = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onService Connected");
            mIPower = IPower.Stub.asInterface(service);
            if (mPowerServiceConnectStateListener != null) {
                mPowerServiceConnectStateListener.powerGaugeServiceConnectState(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onService DisConnected");
            mIPower = null;
            if (mPowerServiceConnectStateListener != null) {
                mPowerServiceConnectStateListener.powerGaugeServiceConnectState(false);
            }
        }
    };



    public RemoteDetaTimeStats getNewDetaTimeStats(){
        if(mIPower == null) {
            Log.d(TAG, "power gauge is null");
            connectPowerGauge();
            return new RemoteDetaTimeStats();
        }

        try{
            return mIPower.getRemoteDetaTimeStats();
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, " " +e);
        }
        return new RemoteDetaTimeStats();
    }

    public List<RemoteDetaPowerSw> getRemoteDetaPowerSwStats() {
        if(mIPower == null) {
            connectPowerGauge();
            return new ArrayList<RemoteDetaPowerSw>();
        }

        try{
            return mIPower.getRemoteDetaPowerSw();

        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG," " + e);
        }
        return new ArrayList<RemoteDetaPowerSw>();
    }


    public void setPowerServiceConnectStateListener(PowerServiceConnectStateListener listener) {
        mPowerServiceConnectStateListener = listener;
    }

    public static interface PowerServiceConnectStateListener {
        public void powerGaugeServiceConnectState(boolean isConnected);
    }
}
