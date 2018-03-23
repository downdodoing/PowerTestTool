/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.achartengine;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.powerdisplay.gaugedata.BatteryDisplayBean;
import com.meizu.powertesttool.powerdisplay.gaugedata.GaugeDataManager;
import com.meizu.powertesttool.powerdisplay.ui.BatteryLevelSelectDialog;

import org.achartengine.chart.AbstractChart;

import java.util.List;

/**
 * An activity that encapsulates a graphical view of the chart.
 */
public class GraphicalActivity extends Activity{
  /** The encapsulated graphical view. */
  private GraphicalView mView;
  /** The chart to be drawn. */
  private AbstractChart mChart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle extras = getIntent().getExtras();

    mChart = (AbstractChart) extras.getSerializable(ChartFactory.CHART);
    mView = new GraphicalView(this, mChart);
    String title = extras.getString(ChartFactory.TITLE);
    if (title == null) {
      requestWindowFeature(Window.FEATURE_NO_TITLE);
    } else if (title.length() > 0) {
      setTitle(title);
    }
    setContentView(mView);
//    setContentView(R.layout.power_battry_level_layout);
    WindowManager mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//    params.horizontalMargin = 100;
//    params.verticalMargin = 100;
//    addContentView(mView, params);

    params.flags =
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
    ;

    //调整悬浮窗显示的停靠位置为左侧置顶
    params.gravity = Gravity.LEFT | Gravity.TOP;

    // 以屏幕左上角为原点，设置x、y初始值
    params.x = 0;
    params.y = 0;


    List<BatteryDisplayBean> list = GaugeDataManager.getInstance(this).loadBatteryList();

    final BatteryLevelSelectDialog dialog = new BatteryLevelSelectDialog(GraphicalActivity.this, list);

    LayoutInflater inflater = LayoutInflater.from(getApplication());
    RelativeLayout mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.power_battry_level_layout, null);
    addContentView(mFloatLayout, params);
//    mFloatLayout.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        dialog.show();
//      }
//    });
    Button mSelectButton = (Button) mFloatLayout.findViewById(R.id.select_level);
    mSelectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.show();
      }
    });

  }



}