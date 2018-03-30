package com.meizu.powertesttool;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.meizu.powertesttool.woker.IWorker;

import java.util.List;

public class WorkerAdapter extends BaseAdapter {
    private List<IWorker> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private WorkerSwtichListener mListener;
    private String mWakeUpText;

    public WorkerAdapter(Activity activity, List<IWorker> data) {
        super();
        this.mData = data;
        this.mInflater = activity.getLayoutInflater();
    }

    public void addListener(WorkerSwtichListener listener) {
        mListener = listener;
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
        Log.i("WorkerAdapter", "getView");
        Log.i("WorkerAdapter", "position = " + position);
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_layout, null);
            vh.itemTitle = (TextView) convertView.findViewById(R.id.item_title);
            vh.itemEdit = (EditText) convertView.findViewById(R.id.title_edit);
            vh.itemSwitch = (Switch) convertView.findViewById(R.id.item_switch);
            vh.itemButton = (Button) convertView.findViewById(R.id.item_button);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.itemEdit.setVisibility(View.GONE);
        vh.itemSwitch.setOnCheckedChangeListener(null);
        vh.itemTitle.setText(mData.get(position).getWorkerName());

        if (position == 4 || position == 6 || position == 7 || position == 9 || position == 11 ||
                position == 12 || position == 14 || position == 15 || position == 16 || position == 17) {
            vh.itemSwitch.setVisibility(View.GONE);
            vh.itemButton.setVisibility(View.VISIBLE);
            vh.itemButton.setOnClickListener(new ButtonClickListener(position));
        } else {
            vh.itemSwitch.setVisibility(View.VISIBLE);
            vh.itemButton.setVisibility(View.GONE);
            vh.itemSwitch.setChecked(mData.get(position).getWorkStatus());
            vh.itemSwitch.setOnCheckedChangeListener(new SwitchCheckListener(position));
        }


        if (position == 3) {
            vh.itemEdit.setVisibility(View.VISIBLE);
            vh.itemEdit.removeTextChangedListener(editTextWatcher);
            vh.itemEdit.addTextChangedListener(editTextWatcher);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView itemTitle;
        EditText itemEdit;
        Switch itemSwitch;
        Button itemButton;
    }

    private TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mWakeUpText = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public String getEditContent() {
        if (mWakeUpText != null) {
            return mWakeUpText;
        }
        return "2";
    }


    class SwitchCheckListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        public SwitchCheckListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mListener.onWorkerSwitch(position, isChecked);
        }
    }


    public interface WorkerSwtichListener {
        void onWorkerSwitch(int position, boolean isWork);
    }

    class ButtonClickListener implements View.OnClickListener {
        private int position;

        public ButtonClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            mListener.onWorkerSwitch(position, true);
        }
    }


}
