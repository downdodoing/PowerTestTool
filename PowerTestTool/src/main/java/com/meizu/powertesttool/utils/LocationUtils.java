package com.meizu.powertesttool.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.meizu.flyme.AppOpsManagerFlyme;

import java.util.ArrayList;
import java.util.List;


public class LocationUtils {
    private static final String TAG = "LocationUtils";
    private static final boolean DEBUG = true;

    private static final int[] highPowerLocationAppOp = new int[] {AppOpsManagerFlyme.OP_MONITOR_HIGH_POWER_LOCATION};

    public static List<Integer> getActiveGpsUidSets() {
        try {
            List<Integer> runningPackages = new ArrayList<>();

            List<Object> packages = AppOpsManagerFlyme.getPackagesForOps(highPowerLocationAppOp);

            if (packages != null) {
                final int numPackages = packages.size();
                for (int packageInd = 0; packageInd < numPackages; packageInd++) {
                    Object packageOp = packages.get(packageInd);

                    List<Object> opEntries = AppOpsManagerFlyme.PackageOps.getOps(packageOp);
                    if (opEntries == null) {
                        Log.d(TAG, "opEntries == null" );
                        continue;
                    }
                    //if (DEBUG) Log.d(TAG, "opEntries size = " + opEntries.size() );

                    final int numOps = opEntries.size();
                    for (int opInd = 0; opInd < numOps; opInd++) {
                        Object opEntry = opEntries.get(opInd);

                        if (AppOpsManagerFlyme.OpEntry.isRunning(opEntry)) {
                            runningPackages.add(AppOpsManagerFlyme.PackageOps.getUid(packageOp));
                        }
                    }
                }
            }

            if (DEBUG) Log.d(TAG, "runningPackages = " + runningPackages);
            return runningPackages;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isLocationEnable(Context context) {
        boolean bEnable = false;
        if (getLocationMode(context) != Settings.Secure.LOCATION_MODE_OFF) {
            bEnable = true;
        }
        return  bEnable;
    }

    public static int getLocationMode(Context context) {
        ContentResolver cr = context.getContentResolver();
        try {
            return Settings.Secure.getInt(cr, Settings.Secure.LOCATION_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean setLocationMode(Context context, int mode) {
        ContentResolver cr = context.getContentResolver();
        try {
            return Settings.Secure.putInt(cr, Settings.Secure.LOCATION_MODE, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
