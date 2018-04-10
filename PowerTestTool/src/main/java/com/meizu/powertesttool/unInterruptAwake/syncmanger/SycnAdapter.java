package com.meizu.powertesttool.unInterruptAwake.syncmanger;

import android.accounts.Account;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

public class SycnAdapter extends AbstractThreadedSyncAdapter {

    private Context mContext;
    public static boolean isSycnAdapterRun;

    public SycnAdapter(Context context, boolean autoInitialize) {
        this(context, autoInitialize, false);
    }

    public SycnAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
    }

    /**
     * 框架自动将它放在后台线程中运行
     *
     * @param account
     * @param extras
     * @param authority
     * @param provider
     * @param syncResult
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (isSycnAdapterRun) {

            Calendar calendar = Calendar.getInstance();
            Log.i("SycnAdapter", "SycnAdapter进行唤醒 " + (calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)));

            AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, SyncMangerReceiver.class), 0);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), mPendingIntent);
        }
    }
}
