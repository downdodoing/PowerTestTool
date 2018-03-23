package com.meizu.powertesttool.alarmtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wangwen1 on 16-4-14.
 */
public class TestBroadReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ww", "TestBroadReceiver received");
        if (intent == null) {
            Log.i("ww", "intent is null");
        } else {
            Log.i("ww", intent.toString());
        }
    }
}
