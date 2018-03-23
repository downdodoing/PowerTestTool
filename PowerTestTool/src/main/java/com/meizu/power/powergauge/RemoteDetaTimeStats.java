package com.meizu.power.powergauge;

import android.os.Parcel;
import android.os.Parcelable;

public class RemoteDetaTimeStats implements Parcelable  {
    public int mDetaTypeBatRealSecs = 0;
    public int mDetaTypeBatUpSecs = 0;
    public int mDetaTypeScreenOnSecs = 0;

    public int mDetaTypeWifiSecs   = 0;
    public int mDetaTypeCellSecs = 0;

    public int mDetaTypePhoneSecs  = 0;
    public int mDetaTypeWeakSecs  = 0;

    public RemoteDetaTimeStats(){
        mDetaTypeBatRealSecs = 0;
        mDetaTypeBatUpSecs = 0;
        mDetaTypeScreenOnSecs = 0;

        mDetaTypeWifiSecs   = 0;
        mDetaTypeCellSecs = 0;

        mDetaTypePhoneSecs  = 0;
        mDetaTypeWeakSecs  = 0;
    }

    private RemoteDetaTimeStats(Parcel source) {
        mDetaTypeBatRealSecs = source.readInt();
        mDetaTypeBatUpSecs = source.readInt();
        mDetaTypeScreenOnSecs = source.readInt();

        mDetaTypeWifiSecs   = source.readInt();
        mDetaTypeCellSecs = source.readInt();

        mDetaTypePhoneSecs  = source.readInt();
        mDetaTypeWeakSecs  = source.readInt();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mDetaTypeBatRealSecs);
        dest.writeInt(mDetaTypeBatUpSecs);
        dest.writeInt(mDetaTypeScreenOnSecs);

        dest.writeInt(mDetaTypeWifiSecs);
        dest.writeInt(mDetaTypeCellSecs);

        dest.writeInt(mDetaTypePhoneSecs);
        dest.writeInt(mDetaTypeWeakSecs);
    }

    public static final Creator<RemoteDetaTimeStats> CREATOR = new Creator<RemoteDetaTimeStats>() {

        @Override
        public RemoteDetaTimeStats createFromParcel(Parcel source) {
            return new RemoteDetaTimeStats(source);
        }

        @Override
        public RemoteDetaTimeStats[] newArray(int size) {
            return new RemoteDetaTimeStats[size];
        }

    };

    public void reset(){
        mDetaTypeBatRealSecs = 0;
        mDetaTypeBatUpSecs = 0;
        mDetaTypeScreenOnSecs = 0;

        mDetaTypeWifiSecs   = 0;
        mDetaTypeCellSecs = 0;

        mDetaTypePhoneSecs  = 0;
        mDetaTypeWeakSecs  = 0;
    }
}
