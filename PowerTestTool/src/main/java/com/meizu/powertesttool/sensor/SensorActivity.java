package com.meizu.powertesttool.sensor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.meizu.powertesttool.R;

import java.util.List;

/**
 * Created by wangwen1 on 16-2-1.
 */
public class SensorActivity extends Activity{

    private static final String TAG = "SensorActivity";


    List<SensorBean> list;
    SensorTestManager sensorTestManager;
    SensorAdapter sensorAdapter;
    ListView sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorTestManager = new SensorTestManager();
        list = sensorTestManager.getSensors(this);
        sensorAdapter = new SensorAdapter(this, list);
        sensorList = (ListView) findViewById(R.id.wroker_listview);
        sensorList.setAdapter(sensorAdapter);



    }



}
