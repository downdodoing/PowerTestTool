package com.meizu.powertesttool.IPC;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.meizu.powertesttool.IPC.service.ServerService;
import com.meizu.powertesttool.R;

public class IPCServerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcserver);

        Intent intent = new Intent(this, ServerService.class);
        startService(intent);
    }

    public void gotoClientClick(View view) {
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ServerService.class));
    }
}
