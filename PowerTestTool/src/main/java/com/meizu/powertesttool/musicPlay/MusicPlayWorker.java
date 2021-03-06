package com.meizu.powertesttool.musicPlay;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

public class MusicPlayWorker implements IWorker {

    private boolean isWorker;
    private Context mContext;

    public MusicPlayWorker(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void doWork() {
        isWorker = true;
    }

    @Override
    public void cancleWork() {
        isWorker = false;
    }

    @Override
    public boolean getWorkStatus() {
        return isWorker;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.music_play);
    }
}
