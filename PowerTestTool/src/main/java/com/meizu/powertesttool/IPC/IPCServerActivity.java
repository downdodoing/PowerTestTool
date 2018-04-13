package com.meizu.powertesttool.IPC;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.meizu.powertesttool.IPC.service.ServerService;
import com.meizu.powertesttool.R;

public class IPCServerActivity extends Activity {

    private IMyService.Stub binder;

    private ServiceConnection cnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IMyService.Stub) service;
            for (int i = 0; i < 2; i++) {
                Student stu = new Student();
                stu.sname = "Student" + i;
                stu.age = 20 + i;
                stu.sex = "男";
                stu.sno = i;
                try {
                    binder.addStudent(stu);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcserver);

        Intent intent = new Intent(this, ServerService.class);
        bindService(intent, cnn, BIND_AUTO_CREATE);
    }

    public void gotoClientClick(View view) {
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(cnn);
    }
}
