package com.meizu.powertesttool.rainbowcolor;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.meizu.powertesttool.R;

/**
 * Created by wangwen1 on 15-12-23.
 */
public class RainbowActivity extends Activity{
    int index = 1;
    TextView rainbow;
    Handler mHandler = null;
    static final int[] colors = new int[]{R.color.white, R.color.black, R.color.red, R.color.orange, R.color.yellow, R.color.green, R.color.cyan, R.color.bule, R.color.purple};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rainbow_layout);
        rainbow = (TextView)findViewById(R.id.rainbow);
        rainbow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainbow.setBackgroundColor(getResources().getColor(colors[index%9]));
                index++;
            }
        });


        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rainbow.setBackgroundColor(getResources().getColor(colors[index%9]));
                index++;
                mHandler.sendEmptyMessageDelayed(0, 5000);
            }
        };

        mHandler.sendEmptyMessageDelayed(0, 5000);
    }


}
