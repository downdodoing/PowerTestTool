package com.meizu.powertesttool.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wangwen1 on 17-11-7.
 */

public class BootRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ww", "onReceive: action = " + intent.getAction());
    }
}
