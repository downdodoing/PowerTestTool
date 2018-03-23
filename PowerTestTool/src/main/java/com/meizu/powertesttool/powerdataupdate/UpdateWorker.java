package com.meizu.powertesttool.powerdataupdate;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;
import com.meizu.powertesttool.powerdataupdate.PermissionStore.PackageStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangwen1 on 16-5-11.
 */
public class UpdateWorker implements IWorker{

    private static final String TAG = "UpdateWorker";
    private Context mContext;
    private boolean isWorking;

    public UpdateWorker(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void doWork() {
        isWorking = true;

        String[] projection = new String[] {PackageStore.PKG_NAME, PackageStore.OPERATION_VALUE, PackageStore.USER_SETTING_VALUE, PackageStore.TYPE_VALUE, PackageStore.LEVEL_VALUE};
        String selection = "pkgName = 'e39d2c7de19156b0683cd93e8735f348'";
        Cursor cursor = null;
        try {
            ContentResolver cr = mContext.getContentResolver();
            cursor = cr.query(PackageStore.URI, projection, selection, null,
                    null);
            if (cursor != null && cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String packageName = cursor.getString(cursor.getColumnIndexOrThrow(PackageStore.PKG_NAME));
                    String defaultValue = cursor.getString(cursor.getColumnIndexOrThrow(PackageStore.OPERATION_VALUE));
                    String userSetting = cursor.getString(cursor.getColumnIndexOrThrow(PackageStore.USER_SETTING_VALUE));
                    String level = cursor.getString(cursor.getColumnIndexOrThrow(PackageStore.LEVEL_VALUE));
                    Log.d(TAG, "getDetailList packageName = " + packageName + " defaultValue = " + defaultValue + " userSetting = " + userSetting + " level = " + level);
                    Toast.makeText(mContext, "userSetting = " + userSetting, Toast.LENGTH_SHORT);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }



    }

    @Override
    public void cancleWork() {
        isWorking = false;
    }

    @Override
    public boolean getWorkStatus() {
        Log.i(TAG, "getWorkStatus = " + isWorking);
        return isWorking;    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.power_data_update);
    }


}
