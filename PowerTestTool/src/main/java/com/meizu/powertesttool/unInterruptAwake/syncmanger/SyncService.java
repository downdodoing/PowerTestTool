package com.meizu.powertesttool.unInterruptAwake.syncmanger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

    private static SycnAdapter mSycnAdapter;
    private static final Object mSyncAdapterLock = new Object();

    public SyncService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (null == mSycnAdapter) {
            mSycnAdapter = new SycnAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSycnAdapter.getSyncAdapterBinder();
    }
}
