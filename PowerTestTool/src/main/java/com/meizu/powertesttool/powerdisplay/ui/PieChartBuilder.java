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
package com.meizu.powertesttool.powerdisplay.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.meizu.powertesttool.R;
import com.meizu.powertesttool.powerdisplay.gaugedata.DetailSWBean;
import com.meizu.powertesttool.powerdisplay.gaugedata.GaugeDataManager;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class PieChartBuilder extends Activity {
  /** Colors to be used for the pie slices. */
  private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.RED, Color.GRAY, Color.WHITE, Color.YELLOW };
  /** The main series that will include all the data. */
  private CategorySeries mSeries = new CategorySeries("");
  /** The main renderer for the main dataset. */
  private DefaultRenderer mRenderer = new DefaultRenderer();
  /** Button for adding entered data to the current series. */
  private GraphicalView mChartView;

  private Spinner mSpinner;
  private ArrayAdapter mAdapter;

  private long startTime;
  private long endTime;
  private List<String> mSpinnerList;
  List<DetailSWBean> detailList = null;
  List<DetailSWBean> displayList = null;
  PackageManager pm = null;

  private Context mContext;
  private int mSelectTpye = 0;

  @Override
  protected void onRestoreInstanceState(Bundle savedState) {
    super.onRestoreInstanceState(savedState);
    mSeries = (CategorySeries) savedState.getSerializable("current_series");
    mRenderer = (DefaultRenderer) savedState.getSerializable("current_renderer");
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable("current_series", mSeries);
    outState.putSerializable("current_renderer", mRenderer);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.power_detail_layout);
    mSpinner = (Spinner) findViewById(R.id.power_spinner);
    displayList = new ArrayList<>();
    Intent intent = getIntent();
    if (intent != null) {
      startTime = intent.getLongExtra("start_time", System.currentTimeMillis());
      endTime = intent.getLongExtra("end_time", System.currentTimeMillis() + 12*3600);
    }
    mContext = this;
    mSpinnerList = new ArrayList<>();
    mSpinnerList.add("power");
    mSpinnerList.add("cputime");
    mSpinnerList.add("fgtime");
    mSpinnerList.add("mbdata");
    mSpinnerList.add("mbtime");
    mSpinnerList.add("wifidata");
    mSpinnerList.add("wifitime");
    mSpinnerList.add("wltime");
    mSpinnerList.add("alarmtimes");
    mSpinnerList.add("fullwltime");
    mSpinnerList.add("gpstime");
    mSpinnerList.add("sensortime");

    mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mSpinnerList);
    mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSpinner.setAdapter(mAdapter);


    //mValue = (EditText) findViewById(R.id.xValue);
    mRenderer.setZoomButtonsVisible(false);
    mRenderer.setStartAngle(180);
    mRenderer.setDisplayValues(true);
    mRenderer.setLabelsTextSize(16 * 3);
    //mRenderer.setLegendTextSize(16*3);
    pm = getPackageManager();

    detailList = GaugeDataManager.getInstance(this).loadDbUids(startTime, endTime);

    detailList = fliterLowPower(detailList);

    for (DetailSWBean bean : detailList) {
      displayList.add(bean);
      String packageName = pm.getNameForUid(bean.getUid());
      if (packageName != null) {
          mSeries.add(packageName, bean.getPower());
      } else {
        if (bean.getUid() == 0) {
          mSeries.add("kernel", bean.getPower());
        } else {
          mSeries.add("packageName", bean.getPower());
        }
      }

      SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
      renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

      mRenderer.addSeriesRenderer(renderer);
    }

    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectTpye = position;
        switch (position) {
          case 0:
            //power
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getPower() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getPower());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getPower());
                } else {
                  mSeries.add("packageName", bean.getPower());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer powerrenderer = new SimpleSeriesRenderer();

            powerrenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(powerrenderer);
            mChartView.repaint();
            break;
          case 1:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getCpuTime() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getCpuTime());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getCpuTime());
                } else {
                  mSeries.add("packageName", bean.getCpuTime());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();

            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(renderer);
            mChartView.repaint();
            //cputime
            break;
          case 2:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getFgTime() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getFgTime());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getFgTime());
                } else {
                  mSeries.add("packageName", bean.getFgTime());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer fgrenderer = new SimpleSeriesRenderer();

            fgrenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(fgrenderer);
            mChartView.repaint();
            //fgtime
            break;
          case 3:
            //mbdata
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getMbData() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getMbData());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getMbData());
                } else {
                  mSeries.add("packageName", bean.getMbData());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer mbDataRenderer = new SimpleSeriesRenderer();

            mbDataRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(mbDataRenderer);
            mChartView.repaint();
            break;
          case 4:
//              mbtime
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getMbTime() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getMbTime());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getMbTime());
                } else {
                  mSeries.add("packageName", bean.getMbTime());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer mbTimeRenderer = new SimpleSeriesRenderer();

            mbTimeRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(mbTimeRenderer);
            mChartView.repaint();
            break;
          case 5:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getWifiData() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getWifiData());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getWifiData());
                } else {
                  mSeries.add("packageName", bean.getWifiData());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer wifiDataRenderer = new SimpleSeriesRenderer();

            wifiDataRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(wifiDataRenderer);
            mChartView.repaint();
            break;
          case 6:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getWifiTime() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getWifiTime());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getWifiTime());
                } else {
                  mSeries.add("packageName", bean.getWifiTime());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer wifiTimeRenderer = new SimpleSeriesRenderer();

            wifiTimeRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(wifiTimeRenderer);
            mChartView.repaint();
            break;
          case 7:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getWlTime() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getWlTime());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getWlTime());
                } else {
                  mSeries.add("packageName", bean.getWlTime());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer wlTimeRenderer = new SimpleSeriesRenderer();

            wlTimeRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(wlTimeRenderer);
            mChartView.repaint();
            break;
          case 8:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getAlarmTimes() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getAlarmTimes());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getAlarmTimes());
                } else {
                  mSeries.add("packageName", bean.getAlarmTimes());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer alarmTimesRenderer = new SimpleSeriesRenderer();

            alarmTimesRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(alarmTimesRenderer);
            mChartView.repaint();
            break;
          case 9:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getFullWlTime() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getFullWlTime());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getFullWlTime());
                } else {
                  mSeries.add("packageName", bean.getFullWlTime());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer fullWlTimeRenderer = new SimpleSeriesRenderer();

            fullWlTimeRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(fullWlTimeRenderer);
            mChartView.repaint();
            break;
          case 10:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getGpsTime() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getGpsTime());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getGpsTime());
                } else {
                  mSeries.add("packageName", bean.getGpsTime());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer gpsTimeRenderer = new SimpleSeriesRenderer();

            gpsTimeRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(gpsTimeRenderer);
            mChartView.repaint();
            break;
          case 11:
            mSeries.clear();
            displayList.clear();
            for (DetailSWBean bean : detailList) {
              if (bean.getSensorTime() == 0) {
                continue;
              }
              displayList.add(bean);
              String packageName = pm.getNameForUid(bean.getUid());
              if (packageName != null) {
                mSeries.add(packageName, bean.getSensorTime());
              } else {
                if (bean.getUid() == 0) {
                  mSeries.add("kernel", bean.getSensorTime());
                } else {
                  mSeries.add("packageName", bean.getSensorTime());
                }
              }
            }
            if (mSeries.getItemCount() <= 0) {
              mChartView.repaint();
              break;
            }
            SimpleSeriesRenderer sensorTimeRenderer = new SimpleSeriesRenderer();

            sensorTimeRenderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(sensorTimeRenderer);
            mChartView.repaint();
            break;
          default:
            break;
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });


  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.i("ww", "onResume");
    if (mChartView == null) {
      LinearLayout layout = (LinearLayout) findViewById(R.id.chart);

      mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);

      mRenderer.setClickEnabled(true);
      mChartView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
          if (seriesSelection == null) {
            Log.i("ww", "111");

          } else {

            int position = seriesSelection.getPointIndex();
            Log.i("ww", "setOnLongClickListener position = " + position);

            DetailSWBean bean = displayList.get(position);
            Intent intent = new Intent(PieChartBuilder.this, AppDetailActivity.class);
            intent.putExtra("uid", bean.getUid());
            intent.putExtra("package_name", getPackageNameFormUid(bean.getUid()));
            intent.putExtra("power", bean.getPower());
            intent.putExtra("cpu_time", bean.getCpuTime());
            intent.putExtra("fg_time", bean.getFgTime());
            intent.putExtra("mb_data", bean.getMbData());
            intent.putExtra("mb_time", bean.getMbTime());
            intent.putExtra("wifi_data", bean.getWifiData());
            intent.putExtra("wifi_time", bean.getWifiTime());
            intent.putExtra("wl_time", bean.getWlTime());
            intent.putExtra("alarm_times", bean.getAlarmTimes());
            intent.putExtra("full_wl_time", bean.getFullWlTime());
            intent.putExtra("gps_time", bean.getGpsTime());
            intent.putExtra("sensor_time", bean.getSensorTime());
            mContext.startActivity(intent);
          }

          return false;
        }
      });

      mChartView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
          if (seriesSelection == null) {


            return;
          } else {

            for (int i = 0; i < mSeries.getItemCount(); i++) {
              mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
            }
            mChartView.repaint();

          }
        }
      });

      layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
              LayoutParams.FILL_PARENT));

    } else {

      mChartView.repaint();
    }
  }

  private String getPackageNameFormUid(int uid){
    String packageName = pm.getNameForUid(uid);
    if (packageName != null) {
      return packageName;
    } else {
      if (uid == 0) {
        return "kernel";
      } else {
        return "packageName";
      }
    }
  }

  private List<DetailSWBean> fliterLowPower(List<DetailSWBean> list){
    if (list == null || list.size() <= 1) {
      return list;
    }
    List<DetailSWBean> retList = new ArrayList<>();
    int maxPower = 0;
    for (DetailSWBean bean : list) {
      if (bean.getPower() > maxPower) {
        maxPower = bean.getPower();
      }
    }
    for (DetailSWBean bean : list) {
      if (bean.getPower() > maxPower/1000) {
        retList.add(bean);
      }
    }

    return retList;
  }

}
