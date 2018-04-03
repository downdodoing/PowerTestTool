package com.meizu.powertesttool.unInterruptAwake;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.meizu.powertesttool.R;

public class UnInterruptAwakeActivity extends Activity {
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_interrupt_awake);
    }

    public void clickBnt(View view) {
        switch (view.getId()) {
            case R.id.awake_1:
                if (null != mAlarmManager) {
                    mAlarmManager.cancel(mPendingIntent);
                }
                break;
            case R.id.awake_2:
                if (null != mAlarmManager) {
                    mAlarmManager.cancel(mPendingIntent);
                }
                mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, JobSchedulerReceiver.class), 0);
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 2000, mPendingIntent);
                break;
            default:
                break;
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
