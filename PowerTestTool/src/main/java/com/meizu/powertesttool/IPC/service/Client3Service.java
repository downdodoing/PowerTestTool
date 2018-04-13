package com.meizu.powertesttool.IPC.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.meizu.powertesttool.IPC.IMyService;
import com.meizu.powertesttool.IPC.Student;

import java.util.ArrayList;
import java.util.List;

public class Client3Service extends Service {
    private List<Student> mStudents = new ArrayList<>();

    public Client3Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IMyService.Stub mBinder = new IMyService.Stub() {
        @Override
        public List<Student> getStudent() throws RemoteException {
            return mStudents;
        }

        @Override
        public void addStudent(Student stu) throws RemoteException {
            if (!mStudents.contains(stu)) {
                mStudents.add(stu);
            }
        }
    };
}
