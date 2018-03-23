package com.meizu.powertesttool.switchanimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;


public class OtherSwitchActivity extends Activity{
    boolean isRun = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHandler.removeMessages(1);
            startActivity(new Intent(OtherSwitchActivity.this, SwitchAnimationActivity.class));
            finish();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            mHandler.removeMessages(1);
            finish();
        }
        Log.i("ww", ""+keyCode);
        return false;

    }
}
