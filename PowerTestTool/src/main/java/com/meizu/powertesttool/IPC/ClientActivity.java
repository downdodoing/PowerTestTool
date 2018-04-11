package com.meizu.powertesttool.IPC;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.meizu.powertesttool.IPC.service.ServerService;
import com.meizu.powertesttool.R;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends Activity {
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
        }
    };

    public void showData(List<Student> students) {
        for (int i = 0; i < mStudent.size(); i++) {
            Student student = students.get(i);
            Toast.makeText(this, "name=" + student.sname + ",age=" + student.age + ",size=" + students.size(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Intent intent = new Intent(ClientActivity.this, ServerService.class);
        bindService(intent, cnn, BIND_AUTO_CREATE);
    }

    public void bntClick(View view) {
        switch (view.getId()) {
            case R.id.getdata:
                try {
                    mStudent = mIMyService.getStudent();
                    showData(mStudent);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.adddata:
                Student student = new Student();
                student.age = 10;
                student.sname = "小明";
                student.sno = 22222;
                student.sex = "女";
                try {
                    mIMyService.addStudent(student);
                    Toast.makeText(this, "增加成功", Toast.LENGTH_SHORT).show();
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
