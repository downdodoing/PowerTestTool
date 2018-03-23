package com.meizu.powertesttool.powerdisplay.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.meizu.powertesttool.powerdisplay.gaugedata.BatteryDisplayBean;

import java.util.List;

/**
 * Created by wangwen1 on 16-4-1.
 */
public class PowerSpinnerAdapter extends BaseAdapter {
    private List<BatteryDisplayBean> mData;
    private Context mContext;

    public PowerSpinnerAdapter(Context context, List<BatteryDisplayBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }


}
