package com.meizu.powertesttool.cpuload;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.meizu.powertesttool.R;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HighCpuloadActivity extends Activity {
    private static final String TAG = "HighCpuloadActivity";
    private RadioButton mRadioButton20;
    private RadioButton mRadioButton40;
    private RadioButton mRadioButton60;

    public final static int COREPOOLSIZE = 3;
    public final static int MAXNUMPOOLSIZE = 500;
    public final static long KEEPALIVETIME = 0;
    public final static int BLOCKINGQUEUENUM = 40;

    private static ThreadPoolExecutor threadPoolExecutor;
    private static ThreadPoolExecutor threadPoolExecutor1;
    private static ThreadPoolExecutor threadPoolExecutor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_cpu_load);
        mRadioButton20 = findViewById(R.id.cpu_load_radioButton);

        mRadioButton40 = findViewById(R.id.cpu_load_radioButton2);

        mRadioButton60 = findViewById(R.id.cpu_load_radioButton3);

        setCheckBoxClick();
    }

    //设置点击事件
    public void setCheckBoxClick() {
        mRadioButton20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null == threadPoolExecutor) {
                    threadPoolExecutor = new ThreadPoolExecutor(COREPOOLSIZE, MAXNUMPOOLSIZE, KEEPALIVETIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(BLOCKINGQUEUENUM));
                }
                if (isChecked) {
                    for (int i = 0; i < 1; i++) {
                        threadPoolExecutor.execute(new MyThread("线程zzzzzzzzzzzzzzzzzzzzzzzzzz" + i));
                    }
                } else {
                    threadPoolExecutor.shutdownNow();
                    threadPoolExecutor = null;
                }
            }
        });
        mRadioButton40.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null == threadPoolExecutor1) {
                    threadPoolExecutor1 = new ThreadPoolExecutor(COREPOOLSIZE, MAXNUMPOOLSIZE, KEEPALIVETIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(BLOCKINGQUEUENUM));
                }
                if (isChecked) {
                    for (int i = 0; i < 3; i++) {
                        threadPoolExecutor1.execute(new MyThread1("县城ggggggggggggggggggggg " + i));
                    }
                } else {
                    threadPoolExecutor1.shutdownNow();
                    threadPoolExecutor1 = null;
                }
            }
        });
        mRadioButton60.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null == threadPoolExecutor2) {
                    threadPoolExecutor2 = new ThreadPoolExecutor(COREPOOLSIZE, MAXNUMPOOLSIZE, KEEPALIVETIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(BLOCKINGQUEUENUM));
                }
                if (isChecked) {
                    for (int i = 0; i < 53; i++) {
                        threadPoolExecutor2.execute(new MyThread2("县城hhhhhhhhhhhhhhhhhhhhhh " + i));
                    }
                } else {
                    threadPoolExecutor2.shutdownNow();
                    threadPoolExecutor2 = null;
                }
            }
        });
    }

    static class MyThread implements Runnable {

        private String mName;

        public MyThread(String name) {
            this.mName = name;
        }

        @Override
        public void run() {
            Random random = new Random();
            int num = random.nextInt(10);
            int num1 = random.nextInt(10);
            int num2 = random.nextInt(10);
            while (null != threadPoolExecutor) {
                Log.i(TAG, mName + (num * num1 * num * num * num * num * num + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2 + num2) + " ");
            }
        }
    }

    static class MyThread1 implements Runnable {

        private String mName;

        public MyThread1(String name) {
            this.mName = name;
        }

        @Override
        public void run() {
            Random random = new Random();
            int num = random.nextInt(10);
            int num1 = random.nextInt(10);
            int num2 = random.nextInt(10);
            while (null != threadPoolExecutor1)
                Log.i(TAG, mName + (num * num1 + num2) + " ");
        }
    }

    static class MyThread2 implements Runnable {

        private String mName;

        public MyThread2(String name) {
            this.mName = name;
        }

        @Override
        public void run() {
            Random random = new Random();
            int num = random.nextInt(10);
            int num1 = random.nextInt(10);
            int num2 = random.nextInt(10);
            while (null != threadPoolExecutor2)
                Log.i(TAG, mName + (num * num1 + num2) + " ");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != threadPoolExecutor) {
            threadPoolExecutor.shutdownNow();
            threadPoolExecutor = null;
        }
        if (null != threadPoolExecutor1) {
            threadPoolExecutor1.shutdownNow();
            threadPoolExecutor1 = null;
        }
        if (null != threadPoolExecutor2) {
            threadPoolExecutor2.shutdownNow();
            threadPoolExecutor2 = null;
        }
    }
}
