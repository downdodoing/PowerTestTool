package com.meizu.powertesttool.IPC;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.meizu.powertesttool.IPC.service.Client3Service;
import com.meizu.powertesttool.IPC.service.Client4Service;
import com.meizu.powertesttool.R;

import java.util.ArrayList;
import java.util.List;

public class Client5Activity extends Activity {

    private IMyService mIMyService;
    List<Student> mStudent = new ArrayList<>();

    private ServiceConnection cnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIMyService = IMyService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIMyService = null;
            Log.i("ClientActivity", "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client5);

        Intent intent = new Intent(this, Client4Service.class);
        bindService(intent, cnn, BIND_AUTO_CREATE);
    }

    public void showData(List<Student> students) {
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            Toast.makeText(this, "name=" + student.sname + ",age=" + student.age + ",size=" + students.size(), Toast.LENGTH_SHORT).show();
        }
    }

    public void bntClick(View view) {
        switch (view.getId()) {
            case R.id.getData:
                try {
                    mStudent = mIMyService.getStudent();
                    showData(mStudent);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(cnn);
    }
}
