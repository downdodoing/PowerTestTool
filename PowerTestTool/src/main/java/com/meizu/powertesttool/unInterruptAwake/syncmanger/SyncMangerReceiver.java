package com.meizu.powertesttool.unInterruptAwake.syncmanger;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.os.Bundle;
import android.util.Log;

public class SyncMangerReceiver extends BroadcastReceiver {
    public static final String AUTHORITY = "com.meizu.powertestTool.content";
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;

    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;

    @Override
    public void onReceive(Context context, Intent intent) {
        Account account = new Account("dummyaccount", "com.meizu");
        //ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, 1);


        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(account, AUTHORITY, settingsBundle);

    }
}
