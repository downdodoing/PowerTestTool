package com.meizu.powertesttool.unInterruptAwake;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.meizu.powertesttool.MainActivity;
import com.meizu.powertesttool.R;
import com.meizu.powertesttool.unInterruptAwake.jobScheduler.JobSchedulerReceiver;
import com.meizu.powertesttool.unInterruptAwake.jobScheduler.service.UnInterruptAwakeService;
import com.meizu.powertesttool.unInterruptAwake.syncmanger.SycnAdapter;
import com.meizu.powertesttool.unInterruptAwake.syncmanger.SyncMangerReceiver;

import org.w3c.dom.Text;

/**
 * AlarmManager中持有一个唤醒锁，可以确保任务运行结束
 */
public class UnInterruptAwakeActivity extends Activity {
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    public static final String TAG = "AwakeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_interrupt_awake);
        createSyncAccount();
    }

    public void clickBnt(View view) {
        switch (view.getId()) {
            case R.id.awake_1:
                if (!SycnAdapter.isSycnAdapterRun) {
                    SycnAdapter.isSycnAdapterRun = true;
                    UnInterruptAwakeService.isJobServiceRun = false;

                    mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, SyncMangerReceiver.class), 0);
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), mPendingIntent);
                }
                break;
            case R.id.awake_2:
                if (!UnInterruptAwakeService.isJobServiceRun) {
                    UnInterruptAwakeService.isJobServiceRun = true;
                    SycnAdapter.isSycnAdapterRun = false;

                    mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, JobSchedulerReceiver.class), 0);
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), mPendingIntent);
                }
                break;
            default:
                break;
        }
    }

    public void createSyncAccount() {
        Account account = new Account("dummyaccount", "com.meizu");

        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            Log.i(TAG, "createSyncAccount:   添加成功");
        } else {
            Log.i(TAG, "createSyncAccount:  添加失败");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
