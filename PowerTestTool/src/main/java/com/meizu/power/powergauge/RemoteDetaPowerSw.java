package com.meizu.power.powergauge;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class RemoteDetaPowerSw implements Parcelable, Cloneable {
    private static String TAG = "PowerGauge";

    public int uid = 0;
    public int power = 0;

    //below data is from stats2map
    public long mobilePackets = 0;

    public int wakelockTime = 0;
    public int alarmTimes = 0;
    public int fullWakeTime = 0;
    public int gpsTime = 0;


    public RemoteDetaPowerSw() {

    }

    public RemoteDetaPowerSw(Parcel source) {
        uid = source.readInt();
        power = source.readInt();
        mobilePackets = source.readLong();
        wakelockTime = source.readInt();
        alarmTimes = source.readInt();
        fullWakeTime = source.readInt();
        gpsTime = source.readInt();

    }

    public static final Creator<RemoteDetaPowerSw> CREATOR = new Creator<RemoteDetaPowerSw>() {

        @Override
        public RemoteDetaPowerSw createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new RemoteDetaPowerSw(source);
        }

        @Override
        public RemoteDetaPowerSw[] newArray(int size) {
            // TODO Auto-generated method stub

            return new RemoteDetaPowerSw[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(uid);
        dest.writeInt(power);
        dest.writeLong(mobilePackets);
        dest.writeInt(wakelockTime);
        dest.writeInt(alarmTimes);
        dest.writeInt(fullWakeTime);
        dest.writeInt(gpsTime);
    }


    public Object clone() {
        Object o = null;
        try {
            o = (RemoteDetaPowerSw) super.clone();
        } catch (CloneNotSupportedException e) {
            Log.e(TAG, e.toString());
        }
        return o;
    }

}
