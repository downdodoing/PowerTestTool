package com.meizu.powertesttool.downloadTest.interfaceI;

public interface IDownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPause();

    void onCanceled();
}
