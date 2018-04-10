package com.meizu.powertesttool.unInterruptAwake.syncmanger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;

    public AuthenticatorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "Syncmanager 唤醒后台 AuthenticatorService", Toast.LENGTH_SHORT).show();
        Log.i("AuthenticatorService", "Syncmanager 唤醒后台 AuthenticatorService");
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
