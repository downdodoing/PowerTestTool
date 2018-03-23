package com.meizu.powertesttool.powerdisplay.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.powerdisplay.gaugedata.BatteryDisplayBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwen1 on 16-4-1.
 */
public class BatteryLevelSelectDialog extends AlertDialog {

    private Context mContext;
    private List<BatteryDisplayBean> mData;
    private Spinner mStartSpinner;
    private Spinner mEndSpinner;
    private Button mConfirmButton;


    private List<String> mStartList;
    private List<String> mEndList;
    private ArrayAdapter mStartAdapter;
    private ArrayAdapter mEndAdapter;

    private int mStartPosition;
    private int mEndPosition;


    public BatteryLevelSelectDialog(Context context, List<BatteryDisplayBean> data) {
        super(context);
        this.mContext = context;
        this.mData = data;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_level_choose_layout);
        mStartSpinner = (Spinner) findViewById(R.id.battery_start_spinner);
        mEndSpinner = (Spinner) findViewById(R.id.battery_end_spinner);
        mConfirmButton = (Button) findViewById(R.id.confirm_level_select);

        mStartList = new ArrayList<>();
        mEndList = new ArrayList<>();
        final int size = mData.size();
        for (int i = 0; i < size ; i++) {
            mStartList.add("" + (i+1));
        }
        mStartAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, mStartList);
        mStartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStartSpinner.setAdapter(mStartAdapter);

        mEndAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, mEndList);
        mEndAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEndSpinner.setAdapter(mEndAdapter);

        mStartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEndList.clear();
                for (int i = (position); i < size; i++) {
                    mEndList.add("" + (i + 1));
                }

                mStartPosition = position;
                if (mStartPosition>mEndPosition) {
                    mEndPosition = mStartPosition;
                    mEndSpinner.setVerticalScrollbarPosition(0);
                }
                mEndAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEndSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEndPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ww", "start position = " + mStartPosition + " end positon = " + (mStartPosition + mEndPosition));
                Intent intent = new Intent(mContext, PieChartBuilder.class);
                intent.putExtra("start_time",mData.get(mStartPosition).getTime());
                intent.putExtra("end_time",mData.get((mStartPosition+mEndPosition)).getTime());
                mContext.startActivity(intent);
            }
        });

    }
}
