package com.meizu.powertesttool.powerdisplay.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.meizu.powertesttool.powerdisplay.gaugedata.BatteryDisplayBean;
import com.meizu.powertesttool.powerdisplay.gaugedata.GaugeDataManager;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwen1 on 16-3-31.
 */
public class PowerDisplayBarChart extends AbstractDemoChart{
    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Sales stacked bar chart";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The monthly sales for the last 2 years (stacked bar chart)";
    }

    /**
     * Executes the chart demo.
     *
     * @param context the context
     * @return the built intent
     */
    public Intent execute(Context context) {
        String[] titles = new String[] { "耗电量", "电量" };
        List<double[]> values = new ArrayList<double[]>();
        List<BatteryDisplayBean> powerList = GaugeDataManager.getInstance(context).loadBatteryList();
        List<Double> list1 = new ArrayList<>();
        List<Double> list2 = new ArrayList<>();
        List<List<Double>> values1 = new ArrayList<>();
        for (BatteryDisplayBean bean : powerList){
            list1.add(new Double(bean.getReabattime()/10));
            list2.add(new Double(bean.getLevel()));
        }
        values1.add(list1);
        values1.add(list2);
        values.add(new double[] { 180, 182, 280, 888, 666, 180, 222, 170, 620, 1401,
                6520, 452 });
        values.add(new double[] { 90, 89, 88, 87, 86, 85, 84, 83, 82, 81,
                80, 79 });
        int[] colors = new int[] { Color.BLUE, Color.CYAN };
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        setChartSettings(renderer, "", "编号", "", 0.5,
                12.5, 0, 1500, Color.GRAY, Color.LTGRAY);
        ((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setDisplayChartValues(true);
        ((XYSeriesRenderer) renderer.getSeriesRendererAt(1)).setDisplayChartValues(true);
        ((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setChartValuesTextSize(16 * 3);
        ((XYSeriesRenderer) renderer.getSeriesRendererAt(1)).setChartValuesTextSize(16 * 3);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setXLabelsAlign(Align.LEFT);
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setPanEnabled(true, false);

        // renderer.setZoomEnabled(false);
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);

//        return ChartFactory.getBarChartIntent(context, buildBarDataset(titles, values), renderer,
//                Type.STACKED);

        //GaugeDataManager.getInstance(context).loadDbUids(powerList.get(0).getTime(), powerList.get(powerList.size()-1).getTime());
        return ChartFactory.getBarChartIntent(context, buildBarDataset1(titles, values1), renderer,
                Type.STACKED);
    }


}
