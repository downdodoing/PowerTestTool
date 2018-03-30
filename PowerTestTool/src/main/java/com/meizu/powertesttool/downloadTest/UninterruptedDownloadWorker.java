package com.meizu.powertesttool.downloadTest;

import android.content.Context;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.woker.IWorker;

public class UninterruptedDownloadWorker implements IWorker {

    private boolean isWork;
    private Context mContext;

    public UninterruptedDownloadWorker(Context context) {
        this.mContext = context;
    }

    @Override
    public void doWork() {
        isWork = true;
    }

    @Override
    public void cancleWork() {
        isWork = false;
    }

    @Override
    public boolean getWorkStatus() {
        return isWork;
    }

    @Override
    public String getWorkerName() {
        return mContext.getResources().getString(R.string.download_not_stop);
    }
}
