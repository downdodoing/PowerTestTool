package com.meizu.powertesttool.powerdisplay.gaugedata;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.Log;

import com.meizu.power.powergauge.RemoteDetaPowerSw;
import com.meizu.power.powergauge.RemoteDetaTimeStats;
import com.meizu.powertesttool.powerdisplay.gaugestore.GaugeBatLevelStore;
import com.meizu.powertesttool.powerdisplay.gaugestore.GaugeHWStore;
import com.meizu.powertesttool.powerdisplay.gaugestore.GaugeIndexStore;
import com.meizu.powertesttool.powerdisplay.gaugestore.GaugeSWStore;
import com.meizu.powertesttool.powerdisplay.gaugestore.PowerAppStore;
import com.meizu.powertesttool.powerdisplay.gaugestore.PowerStore;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GaugeDataManager {

    private static final String TAG = "PowerGaugeClient";
    public static final int WRONG_VALUE = -1;


    private static final int MAX_DUR = 12 * 60; // mins
    private static final int OS_UID = 0;
    private static final int ANDROID_UID = 1000;

    private static GaugeDataManager mInstance =null;

    private Context mContext = null;
    private GaugeServiceWrapper mServiceRemote = null;
    private boolean isFreezeVersion = false;

    private GaugeDataManager(Context context) {
        mContext = context.getApplicationContext();
        mServiceRemote  = new GaugeServiceWrapper(mContext);
        mServiceRemote.setPowerServiceConnectStateListener(new GaugeServiceWrapper.PowerServiceConnectStateListener() {

            @Override
            public void powerGaugeServiceConnectState(boolean isConnected) {
                updateDetaGaugeStats(isConnected);
            }

        });
        isFreezeVersion = isFreezeVersion(mContext);

    }


    public static GaugeDataManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GaugeDataManager.class) {
                if (mInstance == null) {
                    mInstance = new GaugeDataManager(context);
                }
            }
        }
        return mInstance;
    }

    public synchronized void release() {
        mInstance = null;
        mServiceRemote.setPowerServiceConnectStateListener(null);

        mUids.clear();
        mWakelockSecsMap.clear();
        mAlarmStatsMap.clear();
        mForceScreenOnMap.clear();
        mHwPowerPerMap.clear();
        mSwPowerPerMap.clear();
    }

    // for batlevel curve draw
    public static class BatItem {
        public long utcTime = 0;
        public int level = 0;
        public int pluggedType = -1;
    }


    public static class SwEleDetail {
        // for key
        public static final int APP_STATS   =    0;
        public static final int WAKELOCK_TIME =  1; //GaugeSWStore.WAKELOCK_TIME;
        public static final int ALARM_TIMES =    2; //GaugeSWStore.ALARM_TIMES;
        public static final int ALARM_FREQ  =    3; //"alarm_freq";
        public static final int MOBILE_DATA =    4; //GaugeSWStore.MOBILE_DATA;
        public static final int GPS_TIME    =    5;  //GaugeSWStore.GPS_TIME;
        public static final int POWER_PER   =    6; //"power_per";
        public static final int WAKE_ALIGN  =    7;  //"wake_align";
        public static final int FORCESCREEN_TIME = 8;
        public static final int WIFI_DATA = 9;

        //for align value
        public static final int  WAKE_ALIGN_ALLOW = 1;
        public static final int  WAKE_ALIGN_NOT_ALLOW = 0;
        public static final int  WAKE_ALIGN_NONE = -1;
        //for app stats
        public static final long APP_REMOVED  = 0;
        public static final long APP_KEPT     = 1;

    }


    private static class DetaTimeStats {
        public int detaBatRealSecs = 0;
        public int detaBatScreenOnSecs = 0;
        public int detaTypeWifiSecs = 0;
        public int detaTypeCellSecs = 0;
        public int detaTypePhoneSecs = 0;
        public int detaTypeWeakSecs = 0;

        public DetaTimeStats(){
        }

    }

    private List<Integer> mUids = new ArrayList<Integer>();
    private ArrayMap<Integer,Integer> mWakelockSecsMap =  new ArrayMap<Integer,Integer>();          // seconds > 0
    private ArrayMap<Integer, Integer> mAlarmStatsMap = new ArrayMap<Integer,Integer>();        // alarm times > 0
    private ArrayMap<Integer,Integer> mForceScreenOnMap = new ArrayMap<Integer,Integer>();          //  time > 0
    private ArrayMap<Integer,Integer> mHwPowerPerMap     = new ArrayMap<Integer, Integer>();           // percent/1000 >=1/1000
    private ArrayMap<Integer,Integer> mSwPowerPerMap     = new ArrayMap<Integer,Integer>();

    private ArrayMap<Integer, Integer>mHwCommonUsageMap = new ArrayMap<Integer, Integer>();

    private ArrayMap<Integer, Integer>mGpsSecsMap  = new ArrayMap<Integer, Integer>();
    private ArrayMap<Integer, Long>mMobileMap  = new ArrayMap<Integer, Long>();

    private ArrayMap<Integer,RemoteDetaPowerSw> mDetaPowerSwMap = new ArrayMap<Integer,RemoteDetaPowerSw>();

    private int mDuration = 0;
    private int mBatRealtimeSecs = 0;
    private int mBatScreenOnSecs = 0;
    private int mCellWeakSecs =  0;

    private DetaTimeStats mDetaTimeStats = new DetaTimeStats();

    private int mScreenOffTotalWakeSecs = 0;
    private int mScreenOnTotalWakeSecs  = 0;

    private int mBatOffSecsFromLevel = 0;
    private int mBatOnSecsFromLevel = 0;


    //@return  retrun batlevel list data for batlevel curve line
    public List<BatItem> getDbBatStats(int duration) {
        List<BatItem>batList = loadDbBatStats(duration);

        if(batList == null ){
            batList = new ArrayList<BatItem>();
        }
        if (batList.isEmpty()) {
            processScreenSecsStatsFromBat(batList, null, 0);
            return batList;
        } else {
            BatItem prevItem = loadRecentDbBatPoint(duration);

            if (prevItem.utcTime != -1 && prevItem.level != -1) {
                Log.i(TAG, "add prev deta time to on off time");
                processScreenSecsStatsFromBat(batList, prevItem, duration);
            } else {
                processScreenSecsStatsFromBat(batList, null, 0);
            }

            batList.add(0, prevItem);

            return batList;
        }
    }


    /*********************** for isolated refresh stats**********************/
    // must to be first called
    public void refreshUidStats(int duration) {
        mDuration = duration;
        mUids = loadDbUids(duration);

    }

    public void refreshRemoteDetaTimeStats()  {
        ProcessRemoteDetaTimeStats();
    }

    public void refreshRemoteDetaSwStats() {
        processRemoteDetaSwStats();
    }

    //黑屏页面需要先刷 remote deta sw 然后刷 wake alarm
    public void refreshWakeAlarmStats(int duration, int batOffTime) {
        ArrayMap<Integer,Integer> wakeTimeMap = loadAllWakelockTime(duration);
        mScreenOffTotalWakeSecs = rescaleTimeMap(wakeTimeMap,batOffTime);   //100mills to secs
        mWakelockSecsMap  =    wakeTimeMap;
        mAlarmStatsMap    =    loadAlarmStatsMap(duration);


    }

    public void refreshForceScreenStats(int duration, int batOnTime){
        ArrayMap<Integer,Integer> wakeTimeMap = loadForceScreenOnMap(duration);
        mScreenOnTotalWakeSecs = rescaleTimeMap(wakeTimeMap,batOnTime);   //100mills to secs
        mForceScreenOnMap      = wakeTimeMap;

    }


    public void refreshPerStats(int duration){
        processPowerPercentMaps(duration);
    }


    public void refreshHwStats(int duration){
        processHwInfoMap(duration);
    }

    public void refreshGpsStats(int duration){
        mGpsSecsMap   = loadAllGpsSecs(duration);
    }

    public void refreshMobileStats(int duration) {
        mMobileMap    = loadAllMobileData(duration);
    }

        /*
    * function:统计每个uid消耗电量详情
    * uid：应用所属的uid
    * duration：统计的时间长度
    * return :key为耗电项目名称，value为该项目的数值
    * */

    public ArrayMap<Integer,Long> getSwPowerDetail( int uid){


        ArrayMap<Integer,Long> eledata=new ArrayMap<Integer,Long>();
        ArrayMap<Integer,Long> data = loadSomeSwElementsDb(uid, mDuration);

        if (data.containsKey(SwEleDetail.APP_STATS))
            eledata.put(SwEleDetail.APP_STATS,data.get(SwEleDetail.APP_STATS));
        else
            eledata.put(SwEleDetail.APP_STATS,SwEleDetail.APP_REMOVED);



        if (data.containsKey(SwEleDetail.WAKELOCK_TIME)) {
            long wakeTime = data.get(SwEleDetail.WAKELOCK_TIME);
            if (wakeTime >= 0) {
                eledata.put(SwEleDetail.WAKELOCK_TIME, data.get(SwEleDetail.WAKELOCK_TIME));
            } else {
                //for OS_UID
                Log.d(TAG, "uid  is "+uid);
                eledata.put(SwEleDetail.WAKELOCK_TIME, (long) 0);
            }
        }
        else
            eledata.put(SwEleDetail.WAKELOCK_TIME, (long) 0);

        if (data.containsKey(SwEleDetail.ALARM_TIMES))
            eledata.put(SwEleDetail.ALARM_TIMES, data.get(SwEleDetail.ALARM_TIMES));
        else
            eledata.put(SwEleDetail.ALARM_TIMES, (long) 0);



        if (data.containsKey(SwEleDetail.MOBILE_DATA))
            eledata.put(SwEleDetail.MOBILE_DATA,data.get(SwEleDetail.MOBILE_DATA));
        else
            eledata.put(SwEleDetail.MOBILE_DATA,(long)0);

        if (data.containsKey(SwEleDetail.GPS_TIME))
            eledata.put(SwEleDetail.GPS_TIME,data.get(SwEleDetail.GPS_TIME));
        else
            eledata.put(SwEleDetail.GPS_TIME,(long)0);

        if (data.containsKey(SwEleDetail.FORCESCREEN_TIME))
            eledata.put(SwEleDetail.FORCESCREEN_TIME, data.get(SwEleDetail.FORCESCREEN_TIME));
        else
            eledata.put(SwEleDetail.FORCESCREEN_TIME, (long) 0);

        if (data.containsKey(SwEleDetail.WIFI_DATA))
            eledata.put(SwEleDetail.WIFI_DATA,data.get(SwEleDetail.WIFI_DATA));
        else
            eledata.put(SwEleDetail.WIFI_DATA,(long)0);

        eledata.put(SwEleDetail.ALARM_FREQ, (long) 0);
        eledata.put(SwEleDetail.POWER_PER, (long) getSwPowerPercent(uid));
        eledata.put(SwEleDetail.WAKE_ALIGN, (long) getAlignType(uid));

        return eledata;
    }

    /*********************** for isolated refresh stats**********************/
    /*********************** for xiaotao **********************/

    public void refreshScreenOffAllStats(int duration){
        refreshUidStats(duration);
        refreshRemoteDetaTimeStats();
        refreshWakeAlarmStats(duration, getBatScreenOffSecs() * 10);  //secs to 100 mills
        refreshPerStats(duration);
    }


    //黑屏页面需要先刷 remote deta sw 然后刷 wake alarm
    public void refreshScreenOnAllStats(int duration) {
        refreshUidStats(duration);
        refreshRemoteDetaTimeStats();
        refreshForceScreenStats(duration, getBatScreenOnSecs() * 10);
        refreshPerStats(duration);
    }


    public void refreshPowerRank(int duration) {
        getDbBatStats(duration);
        refreshUidStats(duration);
        refreshPerStats(duration);
        refreshRemoteDetaTimeStats();
        refreshHwStats(duration);
    }


    /*********************** for xiaotao **********************/


    // @return  : get wake stats when screen off but system wake
    public ArrayMap<Integer,Integer>  getWakeSecsMapScreenOff() {
        return mWakelockSecsMap;
    }


    public int getTotalWakeSecsScreenOff() {
        return mScreenOffTotalWakeSecs;
    }


    public int getUidWakeSecs(int uid){
        if (mWakelockSecsMap == null || mWakelockSecsMap.isEmpty() || !mWakelockSecsMap.containsKey(uid))
            return 0;

        return mWakelockSecsMap.get(uid);
    }


    // @return  : return secs for screen on and on battery
    public int getBatScreenOnSecs() {
        return reScaleSecsFromBat(true);
    }


    // @return  : return secs for screen off and on battery
    public int getBatScreenOffSecs() {
        return reScaleSecsFromBat(false);

    }

    public int getWifiCommonUsageSecs() {
        return getCommonUsageSecs(PowerHWDetail.WIFI);
    }

    public int getCellCommonUsageSecs() {
        return getCommonUsageSecs(PowerHWDetail.CELL);
    }

    public int getPhoneCommonUsageSecs(){
        return getCommonUsageSecs(PowerHWDetail.PHONE);
    }

    public int getCellWeakSecs(){
        int weaksecs =  mCellWeakSecs + mDetaTimeStats.detaTypeWeakSecs;
        if (weaksecs > mDuration * 60)
            weaksecs = mDuration * 60;

        return weaksecs;
    }

    private HashMap<Object , Object> loadAllBattery(){
        ContentResolver cr = mContext.getContentResolver();
        String[] projection = new String[] {
                GaugeBatLevelStore.TIME, GaugeBatLevelStore.LEVEL, GaugeBatLevelStore.PLUGGED
        };
        String[] selectionArgs = null;
        Cursor cursor = null;
        HashMap<Object , Object> hashMap = null;
        try {

            cursor = cr.query(GaugeBatLevelStore.URI, projection, null, selectionArgs, null);

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();
                return null;
            }
            hashMap = new HashMap<>();
            while (cursor.moveToNext()) {
                GaugeBatteryBean bean = new GaugeBatteryBean();
                bean.setTime(cursor.getLong(cursor.getColumnIndexOrThrow(GaugeBatLevelStore.TIME)));
                bean.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(GaugeBatLevelStore.LEVEL)));
                bean.setPlugType(cursor.getInt(cursor.getColumnIndexOrThrow(GaugeBatLevelStore.PLUGGED)));
                hashMap.put(bean.getTime(), bean);

                Log.i("ww", "" + bean.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return hashMap;
    }

    private List<GaugeStatsIndexBean> loadAllStatsIndex(){
        ContentResolver cr = mContext.getContentResolver();
        String[] projection = new String[] {
                GaugeIndexStore.INDEX, GaugeIndexStore.TIME, GaugeIndexStore.REABATTIME
        };
        String[] selectionArgs = null;
        Cursor cursor = null;
        List<GaugeStatsIndexBean> list = null;
        try {

            cursor = cr.query(GaugeIndexStore.URI, projection, null, selectionArgs, null);

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();
                return null;
            }
            list = new ArrayList<>();
            while (cursor.moveToNext()) {
                GaugeStatsIndexBean bean = new GaugeStatsIndexBean();
                bean.setInd(cursor.getInt(cursor.getColumnIndexOrThrow(GaugeIndexStore.INDEX)));
                bean.setTime(cursor.getLong(cursor.getColumnIndexOrThrow(GaugeIndexStore.TIME)));
                bean.setReabattime(cursor.getLong(cursor.getColumnIndexOrThrow(GaugeIndexStore.REABATTIME)));
                list.add(bean);

                Log.i("ww", "" + bean.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return list;
    }

    public List<BatteryDisplayBean> loadBatteryList(){
        List<BatteryDisplayBean> list = null;
        List<GaugeStatsIndexBean> statsList = loadAllStatsIndex();
        if (statsList == null) return null;
        HashMap<Object , Object> batteryMap = loadAllBattery();
        if (batteryMap == null) return null;

        list = new ArrayList<>();
        int lastLevel = 0;
        for (GaugeStatsIndexBean bean : statsList) {
            if (batteryMap.containsKey(bean.getTime())) {
                int level = ((GaugeBatteryBean) batteryMap.get(((long) bean.getTime()))).getLevel();
                if (lastLevel == level) {
                    continue;
                }
                int plugType = ((GaugeBatteryBean) batteryMap.get(((long) bean.getTime()))).getPlugType();
                if (plugType == 1 || plugType == 2 || plugType == 3) {
                    BatteryDisplayBean displayBean = new BatteryDisplayBean();
                    displayBean.setTime(bean.getTime());
                    displayBean.setInd(bean.getInd());
                    displayBean.setReabattime(bean.getReabattime());
                    displayBean.setLevel(level);
                    displayBean.setPlugType(plugType);
                    Log.i("ww", "" + displayBean.toString());
                    lastLevel = level;
                    list.add(displayBean);
                }
            }

//            if (batteryMap.containsKey((long)statsMap.get(GaugeIndexStore.TIME))){
//                BatteryDisplayBean bean = new BatteryDisplayBean();
//                bean.setInd((int)statsMap.get(GaugeIndexStore.INDEX));
//                bean.setTime((long)statsMap.get(GaugeIndexStore.TIME));
//                bean.setLevel((int)batteryMap.get());
//            }
        }

        return list;
    }

    public void loadAllBatteryq(){
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = GaugeIndexStore.URI;
        uri     = Uri.withAppendedPath(uri, "common");
        uri     = Uri.withAppendedPath(uri, GaugeBatLevelStore.TABLE_NAME);
        String projStr = GaugeBatLevelStore.TABLE_NAME+ "." +GaugeBatLevelStore.TIME;


        String[] projection = new String[] {
                projStr, GaugeBatLevelStore.TABLE_NAME+"."+GaugeBatLevelStore.LEVEL
        };

        String selection = GaugeIndexStore.TABLE_NAME+"."+GaugeIndexStore.TIME + " = " + GaugeBatLevelStore.TABLE_NAME+ "." +GaugeBatLevelStore.TIME;
        //String selection = null;
        String[] selectionArgs = null;

//        String[] selectionArgs = new String[] {
//                String.valueOf(startTime),String.valueOf(endTime)
//        };

        Cursor cursor = null;
        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, null);

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();

                return;
            }

            if (cursor.moveToFirst()) {
                ArrayList<Integer> uids=new ArrayList<Integer>();;
                do{
                    Log.i("ww", "time = " + cursor.getInt(0) + " battery level = " + cursor.getLong(1));
                    uids.add(cursor.getInt(0));
                    //uids.add();
                }while(cursor.moveToNext());

                return;
            }
        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return;
    }

    public List<Integer> loadAllDbUids() {

        //long currentTimePoint  = System.currentTimeMillis();
        //long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

        ContentResolver cr = mContext.getContentResolver();
        Uri uri = GaugeIndexStore.URI;
        uri     = Uri.withAppendedPath(uri, "common");
        uri     = Uri.withAppendedPath(uri, GaugeSWStore.TABLE_NAME);
        String projStr = GaugeSWStore.TABLE_NAME+ "." +GaugeSWStore.UID;


        String[] projection = new String[] {
                projStr, GaugeIndexStore.TABLE_NAME+"."+GaugeIndexStore.TIME, GaugeSWStore.TABLE_NAME+"."+GaugeSWStore.POWER
        };

        //String selection = GaugeIndexStore.TABLE_NAME+"."+GaugeIndexStore.TIME + " between ? and ? ";
        String selection = null;
        String[] selectionArgs = null;

//        String[] selectionArgs = new String[] {
//                String.valueOf(startTime),String.valueOf(endTime)
//        };

        Cursor cursor = null;
        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, null);

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();

                return new ArrayList<Integer>();
            }

            if (cursor.moveToFirst()) {
                ArrayList<Integer> uids=new ArrayList<Integer>();;
                do{
                    Log.i("ww", "uid = " + cursor.getInt(0) + " time = " + cursor.getLong(1) + " power = " + cursor.getLong(2));
                    uids.add(cursor.getInt(0));
                    //uids.add();
                }while(cursor.moveToNext());

                return uids;
            }
        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return new ArrayList<Integer>();
    }


    public List<DetailSWBean> loadDbUids(long startTime, long endTime) {

        //long currentTimePoint  = System.currentTimeMillis();
        //long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

        ContentResolver cr = mContext.getContentResolver();
        Uri uri = GaugeIndexStore.URI;
        uri     = Uri.withAppendedPath(uri, "common");
        uri     = Uri.withAppendedPath(uri, GaugeSWStore.DETAIL_TABLE_NAME);
        String projStr = GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.UID;
        List<DetailSWBean> list = null;
        String[] projection = new String[] {
                projStr, GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.CPU_TIME, GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.FG_TIME,
                GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.MOBILE_DATA, GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.MOBILE_TIME,
                GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.WIFI_DATA, GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.WIFI_TIME,
                GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.WAKELOCK_TIME, GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.ALARM_TIMES,
                GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.FULL_WAKE_TIME, GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.GPS_TIME,
                GaugeSWStore.DETAIL_TABLE_NAME+ "." +GaugeSWStore.SENSOR_TIME
        };

        String selection = GaugeIndexStore.TABLE_NAME+"."+GaugeIndexStore.TIME + " between ? and ? ";

        String[] selectionArgs = new String[] {
                String.valueOf(startTime),String.valueOf(endTime)
        };
        Log.i("ww", "startTime = " + startTime + " endTime = " + endTime);

        Cursor cursor = null;
        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, null);
            Log.i("ww", "cursor.count = " + cursor.getCount());
            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();

                return new ArrayList<DetailSWBean>();
            }
            list = new ArrayList<>();
            while (cursor.moveToNext()) {
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.UID));
                int cpuTime = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.CPU_TIME));
                int fgTime = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.FG_TIME));
                int mbData = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.MOBILE_DATA));
                int mbTime = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.MOBILE_TIME));
                int wifiData = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.WIFI_DATA));
                int wifiTime = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.WIFI_TIME));
                int wlTime = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.WAKELOCK_TIME));
                int alarmTimes = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.ALARM_TIMES));
                int fullwlTime = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.FULL_WAKE_TIME));
                int gpsTime = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.GPS_TIME));
                int sensorTime = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.SENSOR_TIME));
                DetailSWBean bean = new DetailSWBean();
                bean.setUid(uid);
                boolean isReplace = false;
                for (DetailSWBean swBean : list) {
                    if (swBean.getUid() == uid) {
                        cpuTime += swBean.getCpuTime();
                        fgTime += swBean.getFgTime();
                        mbTime += swBean.getMbTime();
                        mbData += swBean.getMbData();
                        wifiData += swBean.getWifiData();
                        wifiTime += swBean.getWifiTime();
                        wlTime += swBean.getWlTime();
                        alarmTimes += swBean.getAlarmTimes();
                        fullwlTime += swBean.getFullWlTime();
                        gpsTime += swBean.getGpsTime();
                        sensorTime += swBean.getSensorTime();
                        Log.i("ww", "remove uid = " + swBean.getUid() + " uid = " + uid);
                        //list.remove(swBean); update not delete
                        swBean.setCpuTime(cpuTime);
                        swBean.setFgTime(fgTime);
                        swBean.setMbData(mbData);
                        swBean.setMbTime(mbTime);
                        swBean.setWifiData(wifiData);
                        swBean.setWifiTime(wifiTime);
                        swBean.setWlTime(wlTime);
                        swBean.setAlarmTimes(alarmTimes);
                        swBean.setFullWlTime(fullwlTime);
                        swBean.setGpsTime(gpsTime);
                        swBean.setSensorTime(sensorTime);
                        isReplace = true;
                    }
                }
                if (!isReplace){
                    bean.setCpuTime(cpuTime);
                    bean.setFgTime(fgTime);
                    bean.setMbData(mbData);
                    bean.setMbTime(mbTime);
                    bean.setWifiData(wifiData);
                    bean.setWifiTime(wifiTime);
                    bean.setWlTime(wlTime);
                    bean.setAlarmTimes(alarmTimes);
                    bean.setFullWlTime(fullwlTime);
                    bean.setGpsTime(gpsTime);
                    bean.setSensorTime(sensorTime);

                    list.add(bean);
                    //Log.i("ww", "" + bean.toString());
                }

            }


        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }

        HashMap<Object, Object> hashMap = loadPowerUids(startTime, endTime);
        for (DetailSWBean bean : list){
            bean.setPower((int) hashMap.get(bean.getUid()));
            Log.i("ww", "" + bean.toString());
        }
        return list;
    }

    private HashMap<Object, Object> loadPowerUids(long startTime, long endTime){
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = GaugeIndexStore.URI;
        uri     = Uri.withAppendedPath(uri, "common");
        uri     = Uri.withAppendedPath(uri, GaugeSWStore.TABLE_NAME);
        String projStr = GaugeSWStore.TABLE_NAME+ "." +GaugeSWStore.UID;
        String[] projection = new String[] {
                projStr, GaugeSWStore.TABLE_NAME+ "." +GaugeSWStore.POWER
        };

        String selection = GaugeIndexStore.TABLE_NAME+"."+GaugeIndexStore.TIME + " between ? and ? ";

        String[] selectionArgs = new String[] {
                String.valueOf(startTime),String.valueOf(endTime)
        };

        Cursor cursor = null;
        HashMap<Object, Object> hashMap = null;
        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, null);

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();

                return null;
            }
            hashMap = new HashMap<>();
            while (cursor.moveToNext()) {
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.UID));
                int power = cursor.getInt(cursor.getColumnIndexOrThrow(GaugeSWStore.POWER));
                Log.i("ww", "uid = " + uid + " power = " + power);
                if (hashMap.get(uid) == null) {
                    hashMap.put(uid, power);
                } else {
                    int powerOri = (int)hashMap.get(uid);
                    power += powerOri;
                    hashMap.put(uid, power);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        Log.i("ww", "" + hashMap.toString());
        return hashMap;
    }

    public List<Integer> loadDbUids(int duration) {

        long currentTimePoint  = System.currentTimeMillis();
        long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

        ContentResolver cr = mContext.getContentResolver();
        Uri uri = GaugeIndexStore.URI;
        uri     = Uri.withAppendedPath(uri, "common");
        uri     = Uri.withAppendedPath(uri, GaugeSWStore.TABLE_NAME);
        String projStr = GaugeSWStore.TABLE_NAME+ "." +GaugeSWStore.UID;

        String[] projection = new String[] {
                projStr
        };

        String selection = GaugeIndexStore.TABLE_NAME+"."+GaugeIndexStore.TIME + " between ? and ? ";

        String[] selectionArgs = new String[] {
                String.valueOf(pastTimePoint),String.valueOf(currentTimePoint)
        };

        Cursor cursor = null;
        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, null);

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();

                return new ArrayList<Integer>();
            }

            if (cursor.moveToFirst()) {
                ArrayList<Integer> uids=new ArrayList<Integer>();;
                do{
                    uids.add(cursor.getInt(0));
                }while(cursor.moveToNext());

                return uids;
            }
        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return new ArrayList<Integer>();
    }


    /*
    * get alarmfreq times/hour;
    * @ uid for app uid
    * */
    public ArrayMap<Integer, Integer>getAlarmStatsMap(){
        return mAlarmStatsMap;
    }

    public int getUidAlarmStats(int uid){

        if (mAlarmStatsMap == null || mAlarmStatsMap.isEmpty() || !mAlarmStatsMap.containsKey(uid))
            return 0;
        return  mAlarmStatsMap.get(uid);
    }

    public ArrayMap<Integer,Integer> getSwPowerPercentMap(){
        return mSwPowerPerMap;

    }


    public ArrayMap<Integer,Integer> getHwPowerPercentMap(){
        return mHwPowerPerMap;

    }

    public int getSwPowerPercent(int uid) {
        if (mSwPowerPerMap == null || mSwPowerPerMap.isEmpty() || !mSwPowerPerMap.containsKey(uid))
            return 0;

        return mSwPowerPerMap.get(uid);

    }

    public int getHwPowerPercent(int type) {
        if (mHwPowerPerMap == null || mHwPowerPerMap.isEmpty() || !mHwPowerPerMap.containsKey(type))
            return 0;

        return mHwPowerPerMap.get(type);
    }

    /*
    * function:判断某个app是否在白名单中
    * packname:包名字
    * return :该应用是否有黑屏后台运行权限
    * */
    public boolean isRunBackAllow(String packagename) {

        if (packagename == null)
            return false;

        String[] projection = new String[] {
                PowerAppStore.PACKAGE_NAME, PowerAppStore.DEFAULT,
                PowerAppStore.APP_LEVEL, PowerAppStore.USR_SETTING,
                PowerAppStore.USR_BLACK
        };

        String selection = null;
        String[] selectionArgs = null;

        selection = PowerAppStore.PACKAGE_NAME+ " = ? ";
        selectionArgs = new String[]{
              Str2MD5(packagename)
        };

        Cursor cursor = null;
        ContentResolver cr = mContext.getContentResolver();
        try {

            cursor = cr.query(PowerAppStore.URI, projection, selection, selectionArgs,
                    PowerAppStore.PACKAGE_NAME);

        }catch (Exception e){
            if (isProductInternational()) {
                return true;
            }else {
                return false;
            }
        }




        if(!isProductInternational()) {

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();
                return false;
            }

            String pkg = "";
            int oRun =   0;
            int level =  0;
            int uRun =   0;
            int ub =     0;

            try {

                cursor.moveToFirst();
                pkg = cursor.getString(0);
                oRun = cursor.getInt(1);
                level = cursor.getInt(2);
                uRun = cursor.getInt(3);
                ub = cursor.getInt(4);

                Log.d(TAG, "china lookup: pkg = "+pkg + " oRun= " +oRun + " level " + level + " uRun " + uRun + " ub " + ub);

            } catch (Exception e) {
                Log.e(TAG, " " + e);
                return false;
            }

            if (isFreezeVersion) {
                if (oRun == 1 && uRun == 1 && ub == 0) {
                    return true;
                }

                if (oRun == 0 && uRun == 1 && ub == 0) {
                    return true;
                }

                return false;

            } else {

                if (oRun == 1 && uRun == 1 && ub == 0) {
                    return true;
                }
                if (oRun == 1 && uRun == 0 && ub == 0) {
                    return true;
                }

                if (oRun == 0 && uRun == 1 && ub == 0) {
                    return true;
                }
                return false;
            }


        } else {

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null)
                    cursor.close();
                return true;
            }

            String pkg = "";
            int oRun =   0;
            int level =  0;
            int uRun =   0;
            int ub =     0;

            try {

                cursor.moveToFirst();
                pkg = cursor.getString(0);
                oRun = cursor.getInt(1);
                level = cursor.getInt(2);
                uRun = cursor.getInt(3);
                ub = cursor.getInt(4);

                Log.d(TAG, "inter lookup: pkg = "+pkg + " oRun= " +oRun + " level " + level + " uRun " + uRun + " ub " + ub);

            } catch (Exception e) {
                Log.e(TAG, " " + e);
                return true;
            }

            if (oRun == 1 && uRun == 1 && ub == 0) {
                return true;
            }
            if (oRun == 1 && uRun == 0 && ub == 0) {
                return true;
            }

            if (oRun == 0 && uRun == 1 && ub == 0) {
                return true;
            }

            return false;
        }

    }

    private static Boolean isProductInternational(){
        try {
            Class<?> c = Class.forName("android.os.BuildExt");
            Method method = c.getDeclaredMethod("isProductInternational");
            method.setAccessible(true);
            return (Boolean) method.invoke(c);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    private static boolean isFreezeVersion(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            Class<?> amClass = am.getClass();
            Method[] methods = amClass.getMethods();
            boolean hasFreeze = false;
            for(Method method : methods) {
                if (method.getName().equals("forceFreezePackage")){
                    hasFreeze = true;
                    break;
                }
            }
            return hasFreeze;

        }catch (Exception e) {
            Log.e(TAG, " " + e);
        }

        return false;
    }



    public void setRunBackStats(String packageName, boolean ifAllow) {

        if(isAppExist(packageName)) {
            updateRunBack(packageName,ifAllow);
        } else {
            addToRunBack(packageName,ifAllow);
        }

    }


    /*
    * retun :返回各个uid对应的强制亮屏时间集合
    * */
    public ArrayMap<Integer,Integer> getForceScreenOnMap(){
        return mForceScreenOnMap;
    }



    //@return Return total force screen on seconds
    public int getTotalForceScreenOnSecs() {
        return mScreenOnTotalWakeSecs;
    }

    //@return Return total force screen on seconds
    public int getForceScreenOnSecs(int uid) {
        if (mForceScreenOnMap == null || mForceScreenOnMap.isEmpty() || !mForceScreenOnMap.containsKey(uid))
            return 0;

        return mForceScreenOnMap.get(uid);
    }

    public int getAlignType(int uid) {
        return SwEleDetail.WAKE_ALIGN_NONE;
    }

    // private apis for provider muti table data get
    private static class MutiTable {


        private static Cursor getDurElementSumCursor(Context context, String typeUidColumn,  int typeUid, String dataTable,
                                                     String columnName, int duration) {

            long currentTimePoint  = System.currentTimeMillis();
            long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

            ContentResolver cr = context.getContentResolver();
            Uri uri = GaugeIndexStore.URI;
            uri = Uri.withAppendedPath(uri,"common");
            uri = Uri.withAppendedPath(uri,dataTable);

            String projStr = "sum" + "(" + dataTable + "." + columnName + ")";

            String[] projection = new String[] {
                    projStr
            };

            String selection = GaugeIndexStore.TABLE_NAME +"." + GaugeIndexStore.TIME + " between ? and ? and " + dataTable + "." +typeUidColumn + " = " + String.valueOf(typeUid) ;

            String[] selectionArgs = new String[] {
                    String.valueOf(pastTimePoint),String.valueOf(currentTimePoint)
            };

            Cursor cursor = null;

            try {

                cursor = cr.query(uri, projection, selection, selectionArgs, null);

            } catch (Exception e) {
                Log.e(TAG, " " + e);
            }

            return cursor;
        }

        private static Cursor getDurGroupListCursor(Context context, String typeUidColumn, String dataTable,
                                                     String columnName, int duration) {

            long currentTimePoint  = System.currentTimeMillis();
            long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

            ContentResolver cr = context.getContentResolver();
            Uri uri = GaugeIndexStore.URI;
            uri = Uri.withAppendedPath(uri,"common");
            uri = Uri.withAppendedPath(uri,dataTable);

            String projStr = dataTable + "." + typeUidColumn  + "," +  " sum" + "(" + dataTable + "." + columnName + ")";

            String[] projection = new String[] {
                    projStr
            };

            String selection = GaugeIndexStore.TABLE_NAME +"." + GaugeIndexStore.TIME + " between ? and ? group by " + dataTable + "." +typeUidColumn;

            String[] selectionArgs = new String[] {
                    String.valueOf(pastTimePoint),String.valueOf(currentTimePoint)
            };

            Cursor cursor = null;

            try {

                cursor = cr.query(uri, projection, selection, selectionArgs, null);

            } catch (Exception e) {
                Log.e(TAG, " " + e);
            }

            return cursor;
        }


        //@param  dataTable  GaugeSWStore.DETAIL_TABLE_NAME
        //@param  typeUidColummn   GaugeSWStore.UID
        //@param  typeUid           uid
        //@param  elementType       GaugeSWStore.MOBILE_DATA
        //@param  duration  mins
        public static int getDurElementInt(Context context, String typeUidColumn,  int typeUid, String dataTable,
                                           String columnName, int duration) {
 /*           if (duration> MAX_DUR)
                duration = MAX_DUR;*/

            if (duration <=0) {
                return WRONG_VALUE;
            }

            Cursor cursor = getDurElementSumCursor(context, typeUidColumn,  typeUid, dataTable, columnName, duration);

            try {
                if (cursor == null || cursor.getCount() < 1) {
                    Log.e(TAG, "load items return sum wrong values" + WRONG_VALUE);
                    return WRONG_VALUE;
                }

                if (cursor.moveToFirst()) {
                    int totalData = cursor.getInt(0);//
                    return totalData;
                }
            }catch (Exception e) {
                Log.e(TAG, " "+e);
            } finally {
                if (cursor!=null) {
                    cursor.close();
                }
            }

            return WRONG_VALUE;
        }

        //select stats_sw.uid, sum(stats_sw.power) as sum from stats_sw, stats_index where  stats_sw.ind = stats_index.ind  and stats_index.time between 0 and 1443009807160 and stats_sw.uid = 1001
        //@param  dataTable  GaugeSWStore.DETAIL_TABLE_NAME
        //@param  typeUidColummn   GaugeSWStore.UID
        //@param  typeUid           uid
        //@param  elementType       GaugeSWStore.MOBILE_DATA
        //@param duration    mins
        public static long getDurElementLong(Context context, String typeUidColumn,  int typeUid,
                                             String dataTable, String columnName, int duration) {
             /* if (duration> MAX_DUR)
                duration = MAX_DUR;*/

            if (duration <=0) {
                return WRONG_VALUE;
            }

            Cursor cursor = getDurElementSumCursor(context, typeUidColumn,  typeUid, dataTable, columnName, duration);

            try {
                if (cursor == null || cursor.getCount() < 1) {
                    Log.e(TAG, "load items return sum wrong values" + WRONG_VALUE);
                    return WRONG_VALUE;
                }

                if (cursor.moveToFirst()) {
                    long totalData = cursor.getLong(0);//
                    return totalData;
                }
            }catch (Exception e) {
                Log.e(TAG, " "+e);
            } finally {
                if (cursor!=null) {
                    cursor.close();
                }
            }
            return WRONG_VALUE;
        }


        public static ArrayMap<Integer,Integer> getDurGroupSumMapInt(Context context, String typeUidColumn,
                                                                String dataTable, String columnName, int duration) {
             /* if (duration> MAX_DUR)
                duration = MAX_DUR;*/

            ArrayMap sumMap = new ArrayMap<Integer,Integer>();

            if (duration <=0) {
                return sumMap;
            }

            Cursor cursor = getDurGroupListCursor(context, typeUidColumn, dataTable, columnName, duration);

            if (cursor == null || cursor.getCount() < 1) {
                Log.e(TAG, "load items return wrong sum list");
                if(cursor != null)
                    cursor.close();
                return sumMap;
            }

            try {

                while (cursor.moveToNext()) {
                    sumMap.put(cursor.getInt(0), cursor.getInt(1));
                }
            } catch (Exception e){
                Log.e(TAG, " " +e);
            } finally{
                if(cursor != null)
                    cursor.close();
            }


            return sumMap;
        }

        public static ArrayMap<Integer,Long> getDurGroupSumMapLong(Context context, String typeUidColumn,
                                                             String dataTable, String columnName, int duration) {
             /* if (duration> MAX_DUR)
                duration = MAX_DUR;*/

            ArrayMap sumMap = new ArrayMap<Integer,Long>();

            if (duration <=0) {
                return sumMap;
            }

            Cursor cursor = getDurGroupListCursor(context, typeUidColumn, dataTable, columnName, duration);

            if (cursor == null || cursor.getCount() < 1) {
                Log.e(TAG, "load items return wrong sum list");
                if(cursor != null)
                    cursor.close();
                return sumMap;
            }

            try {

                while (cursor.moveToNext()) {
                    sumMap.put(cursor.getInt(0), cursor.getLong(1));
                }
            } catch (Exception e){
                Log.e(TAG, " " +e);
            } finally{
                if(cursor != null)
                    cursor.close();
            }


            return sumMap;
        }

    }

/*
*    function:统计指定的一段时间内各个软件耗电量，
*    @durantion :统计的时间长度
*   @return：key是uid，value是该uid对应的总耗电量
* */

    private  ArrayMap<Integer,Integer> loadDbSwPowers(int duration){

        ArrayMap<Integer,Integer>powerMap = loadAllUidInfoInt(duration,GaugeSWStore.TABLE_NAME, GaugeSWStore.POWER);
        return powerMap;
    }

    /*
    *  function: 统计各个硬件在一段时间内的耗电量
    *  duration：统计时间长度
    *  return：key是硬件的type类型，value是该type类型的硬件耗电量
    * */

    private ArrayMap<Integer,Integer> loadDbHwPowers(int duration){

        Map<Integer,Integer> mapDb = MutiTable.getDurGroupSumMapInt(mContext, GaugeHWStore.TYPE, GaugeHWStore.TABLE_NAME, GaugeHWStore.POWER, duration) ;

        if (mapDb == null || mapDb.isEmpty())
            return new ArrayMap<Integer, Integer>();

        ArrayMap<Integer, Integer>dataMap = new ArrayMap<Integer,Integer>();

        for(int type = PowerHWDetail.IDLE; type < PowerHWDetail.TYPE_SIZE ; type++ ) {
            if (mapDb.containsKey(type)) {
                int value = mapDb.get(type);
                if (value > 0) {
                    dataMap.put(type, value);
                }
            }
        }

        return dataMap;
    }

    private int getTotalPower(Map<Integer,Integer> swPowerMap, Map<Integer,Integer> hwPowerMap) {
        int hwToatalPower=0;
        if(hwPowerMap!=null && hwPowerMap.size()>0){
            Iterator iter = hwPowerMap.keySet().iterator();
            while (iter.hasNext()) {
                hwToatalPower+=hwPowerMap.get(iter.next()).intValue();
            }
        }

        int swTotalPower=0;
        if(swPowerMap!=null && swPowerMap.size()>0){
            Iterator iter = swPowerMap.keySet().iterator();
            while (iter.hasNext()) {
                swTotalPower+=swPowerMap.get(iter.next()).intValue();
            }
        }

        int powerSum=swTotalPower + hwToatalPower;

        return powerSum;
    }



    /*
* function:获得每个UID对应的总耗电量占所有耗电量的百分比
* duration：统计的时间长度
* return：key为uid，value为千分比
* */
    private ArrayMap<Integer,Integer> loadSwPowerPercentMap(Map<Integer,Integer> swPowerMap, int powerSum){
        if(powerSum <= 0)
            return new ArrayMap<Integer,Integer>();

        ArrayMap<Integer,Integer>perMap = new ArrayMap<Integer,Integer>();
        Set entries = swPowerMap.entrySet();

        if(entries != null && !entries.isEmpty()) {
            Iterator iterator = entries.iterator();
            while(iterator.hasNext()) {
                Map.Entry<Integer,Integer> entry = (Map.Entry<Integer,Integer>)iterator.next();
                int uid = entry.getKey();
                int value = entry.getValue();
                int percent = (int)((long)value*10000/powerSum);
                percent = (percent + 5)/10;      // round
                if(percent >= 1){
                    perMap.put(uid, percent);
                }
            }
        }
        return perMap;
    }


    /*
    * function：获得每个Type类型的硬件的耗电量百分比
    * duration:统计的时间长度
    * return :key为硬件类型，value为耗电量千分比
    *
    * */

    private ArrayMap<Integer,Integer> loadHwPowerPercentMap(Map<Integer,Integer> hwPowerMap, int powerSum){

        if(powerSum <= 0)
            return new ArrayMap<Integer,Integer>();

        ArrayMap<Integer,Integer>perMap = new ArrayMap<Integer,Integer>();
        Set entries = hwPowerMap.entrySet();

        if(entries != null && !entries.isEmpty()) {
            Iterator iterator = entries.iterator();
            while(iterator.hasNext()) {
                Map.Entry<Integer,Integer> entry = (Map.Entry<Integer,Integer>)iterator.next();
                int type = entry.getKey();
                int value = entry.getValue();
                int percent = (int)((long)value*10000/powerSum);
                percent = (percent + 5)/10;      // round
                if(percent >= 1){
                    perMap.put(type, percent);
                }
            }
        }
        return perMap;

    }

    //@function get sw percent map  and hw percent map
    private void processPowerPercentMaps(int duration){

        ArrayMap<Integer,Integer> swPowerMap  = loadDbSwPowers(duration);
        ArrayMap<Integer,Integer> hwPowerMap  = loadDbHwPowers(duration);

        //because os_uid and android_uid have minus calculation in proces stats. so need to process minus value situation
        if (swPowerMap.containsKey(OS_UID) && swPowerMap.get(OS_UID)<0)
            swPowerMap.put(OS_UID, 0);

        if (swPowerMap.containsKey(ANDROID_UID) && swPowerMap.get(ANDROID_UID) < 0)
            swPowerMap.put(ANDROID_UID, 0);

        int powerSum = getTotalPower(swPowerMap, hwPowerMap);

        if (powerSum <= 0){
            Log.e(TAG, "total powe is less than 0");
            mSwPowerPerMap.clear();
            mHwPowerPerMap.clear();
            return;
        }

        mSwPowerPerMap    = loadSwPowerPercentMap(swPowerMap, powerSum);
        mHwPowerPerMap    = loadHwPowerPercentMap(hwPowerMap, powerSum);

    }

    //@return  : load bat stats to list and return
    // @param duration: duration time for stats
    private List<BatItem> loadDbBatStats(int duration) {

        List<BatItem> batList = new ArrayList<BatItem>();

        long currentTimePoint  = System.currentTimeMillis();
        long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

        ContentResolver cr = mContext.getContentResolver();
        Uri uri = GaugeBatLevelStore.URI;
        String projStr =  GaugeBatLevelStore.TIME + "," + GaugeBatLevelStore.LEVEL + ","  + GaugeBatLevelStore.PLUGGED;

        String[] projection = new String[] {
                projStr
        };

        String selection = GaugeBatLevelStore.TABLE_NAME +"." + GaugeBatLevelStore.TIME + " between ? and ?";

        String[] selectionArgs = new String[] {
                String.valueOf(pastTimePoint), String.valueOf(currentTimePoint)
        };

        Cursor cursor = null;
        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, GaugeBatLevelStore.TIME);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BatItem item = new BatItem();
                    item.utcTime = cursor.getLong(cursor.getColumnIndex(GaugeBatLevelStore.TIME));
                    item.level = cursor.getInt(cursor.getColumnIndex(GaugeBatLevelStore.LEVEL));
                    item.pluggedType = cursor.getInt(cursor.getColumnIndex(GaugeBatLevelStore.PLUGGED));
                    batList.add(item);

                }
            }

        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }

        return batList;
    }

    //@return  : load bat stats to list and return
    // @param duration: duration time for stats
    private BatItem loadRecentDbBatPoint(int duration) {

        long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

        ContentResolver cr = mContext.getContentResolver();
        Uri uri = GaugeBatLevelStore.URI;
        String projStr =  "max (" + GaugeBatLevelStore.TIME + ") as max, " + GaugeBatLevelStore.LEVEL + ","  + GaugeBatLevelStore.PLUGGED;

        String[] projection = new String[] {
                projStr
        };

        // this point will be just used for replace some point. so "=" is needed.
        String selection = GaugeBatLevelStore.TABLE_NAME +"." + GaugeBatLevelStore.TIME + " <=?";

        String[] selectionArgs = new String[] {
                String.valueOf(pastTimePoint)
        };

        Cursor cursor = null;
        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, GaugeBatLevelStore.TIME);

            if (cursor != null && cursor.getCount() > 0) {
                if(cursor.moveToFirst()) {
                    BatItem item = new BatItem();
                    item.utcTime = cursor.getLong(cursor.getColumnIndex("max"));
                    item.level = cursor.getInt(cursor.getColumnIndex(GaugeBatLevelStore.LEVEL));
                    item.pluggedType = cursor.getInt(cursor.getColumnIndex(GaugeBatLevelStore.PLUGGED));
                    return item;
                }
            } else {
                BatItem item = new BatItem();
                item.utcTime = -1;
                item.level   = -1;
                item.pluggedType = PowerPlugType.POWEROFF;
                return item;
            }

        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }

        BatItem item = new BatItem();
        item.utcTime = -1;
        item.level   = -1;
        item.pluggedType = PowerPlugType.POWEROFF;
        return item;
    }


    //get screen usage secs from batstats. not used now.
    private void processScreenSecsStats(int duration) {

        mBatRealtimeSecs = loadBatRealSecs(duration);
        mBatScreenOnSecs = loadBatScreenOnSecs(duration);

    }

    // get screen usage secs from batlevel curve
    private void processScreenSecsStatsFromBat(List<BatItem> batList, BatItem prevItem, int dur ) {

        long offTime = 0;
        long onTime  = 0;
        if (batList != null && batList.size() >=2) {
            for (int i = 0; i<batList.size()-1; i++) {
                BatItem begItem = batList.get(i);
                BatItem endItem = batList.get(i+1);

                if (begItem.pluggedType == PowerPlugType.SCREENOFF || begItem.pluggedType == PowerPlugType.SCREENON) {
                    if (begItem.pluggedType == PowerPlugType.SCREENOFF) {
                        offTime += endItem.utcTime - begItem.utcTime;
                    }

                    if (begItem.pluggedType == PowerPlugType.SCREENON) {
                        onTime  += endItem.utcTime - begItem.utcTime;
                    }
                }
            }

            if (prevItem !=null && prevItem.utcTime!=-1 &&  prevItem.level != -1) {
                long detaTime = batList.get(0).utcTime - (System.currentTimeMillis() - dur * 60 * 1000);

                if (detaTime > 0) {

                    if (prevItem.pluggedType == PowerPlugType.SCREENOFF) {
                        offTime += detaTime;
                    }

                    if (prevItem.pluggedType == PowerPlugType.SCREENON) {
                        onTime += detaTime;
                    }
                }
            }
        }

        mBatOffSecsFromLevel = (int)(offTime/1000);
        mBatOnSecsFromLevel = (int)(onTime/1000);

    }


    private void ProcessRemoteDetaTimeStats(){
        RemoteDetaTimeStats remoteStats = mServiceRemote.getNewDetaTimeStats();
        changeDetaTimeStats(remoteStats);
    }

    private void changeDetaTimeStats(RemoteDetaTimeStats remoteStats){
        mDetaTimeStats.detaBatRealSecs = remoteStats.mDetaTypeBatRealSecs;
        mDetaTimeStats.detaBatScreenOnSecs = remoteStats.mDetaTypeScreenOnSecs;
        mDetaTimeStats.detaTypeWifiSecs = remoteStats.mDetaTypeWifiSecs;
        mDetaTimeStats.detaTypeCellSecs = remoteStats.mDetaTypeCellSecs;
        mDetaTimeStats.detaTypePhoneSecs = remoteStats.mDetaTypePhoneSecs;
        mDetaTimeStats.detaTypeWeakSecs = remoteStats.mDetaTypeWeakSecs;
    }

    private void processRemoteDetaSwStats(){
        mDetaPowerSwMap = convertGaugeListToMap(mServiceRemote.getRemoteDetaPowerSwStats());

    }

    private void updateDetaGaugeStats(boolean isConnected) {
        if(isConnected) {
            Log.d(TAG, " on connected update");
            RemoteDetaTimeStats remoteStats = mServiceRemote.getNewDetaTimeStats();
            changeDetaTimeStats(remoteStats);

           // mDetaPowerSwMap = convertGaugeListToMap(mServiceRemote.getRemoteDetaPowerSwStats());
        }
    }

    //no need mUids
    private ArrayMap<Integer,RemoteDetaPowerSw> convertGaugeListToMap(List<RemoteDetaPowerSw> list){
        ArrayMap<Integer,RemoteDetaPowerSw> map = new ArrayMap<Integer,RemoteDetaPowerSw>();

        if (list == null || list.isEmpty()) {
            Log.d(TAG, "remote list is empty");
            return map;
        }

        for(RemoteDetaPowerSw item : list){
            map.put(item.uid, item);
        }
        return map;
    }

    private int reScaleSecsFromBat(boolean ifReturnOn){

        int detaOnSecs   = mDetaTimeStats.detaBatScreenOnSecs;
        int detaRealSecs = mDetaTimeStats.detaBatRealSecs;

        int detaOffSecs  = (detaRealSecs >= detaOnSecs) ? detaRealSecs - detaOnSecs : 0;

        int onSecs     =   mBatOnSecsFromLevel;
        int offSecs    =   mBatOffSecsFromLevel;

        if (detaOnSecs >= 0 && detaOffSecs >= 0) {
            onSecs = mBatOnSecsFromLevel + detaOnSecs;
            offSecs = mBatOffSecsFromLevel + detaOffSecs;
        }

        if(ifReturnOn)
            return onSecs;
        else
            return offSecs;

    }

    // function for get realtime when device on battery
    //@param duration: duration time for stats
    private int loadBatRealSecs(int duration) {
        long currentTimePoint  = System.currentTimeMillis();
        long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

        ContentResolver cr = mContext.getContentResolver();

        Uri uri = GaugeIndexStore.URI;
        String projStr =  "sum("+GaugeIndexStore.TABLE_NAME + "." +GaugeIndexStore.REABATTIME + ")";
        String[] projection = new String[] {
                projStr
        };

        String selection =  GaugeIndexStore.TIME + " between ? and ?";

        String[] selectionArgs = new String[] {
                String.valueOf(pastTimePoint), String.valueOf(currentTimePoint)
        };

        Cursor cursor = null;

        int totalBatRealtime = 0;
        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, null);

            if (cursor != null && cursor.getCount() > 0) {
                if(cursor.moveToFirst()){
                    totalBatRealtime = cursor.getInt(0);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }

        return getNomilizedSecs(totalBatRealtime);
    }

    // @param  dration : duration mins for stats
    // @return  : return secs for screen on and on battery
    private int loadBatScreenOnSecs(int duration) {

        int screenOnTime = (int) MutiTable.getDurElementInt(mContext, GaugeHWStore.TYPE,
                PowerHWDetail.SCREEN, GaugeHWStore.TABLE_NAME, GaugeHWStore.TIME, duration);

        if (screenOnTime == WRONG_VALUE)
            return 0;
        else
            return getNomilizedSecs(screenOnTime);
    }


    private long loadMobileData(int uid, int duration){
        long data = MutiTable.getDurElementLong(mContext, GaugeSWStore.UID, uid, GaugeSWStore.DETAIL_TABLE_NAME, GaugeSWStore.MOBILE_DATA, duration);
        if (data == WRONG_VALUE)
            return 0;

        return data;
    }


    private ArrayMap<Integer, Long>loadAllMobileData(int duration){
        if(mUids == null || mUids.isEmpty())
            return new ArrayMap<Integer, Long>();

        ArrayMap<Integer, Long> mobileMap = loadAllUidInfoLong(duration, GaugeSWStore.MOBILE_DATA);

        Set entries = mobileMap.entrySet();

        if(entries != null && !entries.isEmpty()) {
            Iterator iterator = entries.iterator();
            while(iterator.hasNext()) {
                Map.Entry<Integer,Long> entry = (Map.Entry<Integer,Long>)iterator.next();
                int uid = entry.getKey();
                long mobileData = entry.getValue();
                if (mobileData > 0) {
                    mobileMap.put(uid, mobileData);
                }
            }
        }

        return mobileMap;
    }


    private int loadGpsSecs(int uid, int duration, int detaTime){
        int gpsTime = MutiTable.getDurElementInt(mContext, GaugeSWStore.UID, uid, GaugeSWStore.DETAIL_TABLE_NAME, GaugeSWStore.GPS_TIME, duration);
        if (gpsTime == WRONG_VALUE)
            return 0;
        return getNomilizedSecs(gpsTime + detaTime);
    }

    

    private ArrayMap<Integer,Integer> loadAllGpsSecs(int duration) {

        ArrayMap<Integer, Integer>gpsMap  = loadAllUidInfoSecs(duration, GaugeSWStore.GPS_TIME);
        return gpsMap;
    }

    //@return Return wl time for uid .  unit is second
    private int loadWakelockSecs(int uid, int duration, int deta) {
        int wlTime = MutiTable.getDurElementInt(mContext, GaugeSWStore.UID, uid, GaugeSWStore.DETAIL_TABLE_NAME, GaugeSWStore.WAKELOCK_TIME, duration);

        int wakeSecs = getNomilizedSecs(wlTime + deta);
        return wakeSecs;
    }


    // 100mills unit
    private ArrayMap<Integer,Integer> loadAllWakelockTime(int duration) {

        ArrayMap<Integer,Integer> timeMap = loadAllUidInfoInt(duration, GaugeSWStore.DETAIL_TABLE_NAME, GaugeSWStore.WAKELOCK_TIME);
        return timeMap;
    }

    //@return Return wl time for uid .  unit is second
    private int loadForceScreenSecs(int uid, int duration, int deta) {
        int wlTime = MutiTable.getDurElementInt(mContext, GaugeSWStore.UID, uid, GaugeSWStore.DETAIL_TABLE_NAME, GaugeSWStore.FULL_WAKE_TIME, duration);

        int wakeSecs = getNomilizedSecs(wlTime + deta);
        return wakeSecs;
    }


    // get alarmfreq times/hour;
    // @ uid for app uid
    // @ duration for calculate duration
    // get alarmfreq times/hour;
    private int loadUidAlarmStats(int uid, int duration){

        int alarmTimes = 0;

        alarmTimes = (int) MutiTable.getDurElementLong(mContext, GaugeSWStore.UID, uid, GaugeSWStore.DETAIL_TABLE_NAME, GaugeSWStore.ALARM_TIMES, duration);

        if (alarmTimes == WRONG_VALUE || alarmTimes == 0)
            return 0;

        return alarmTimes;

    }


    //@return Return alarm stats map
    private ArrayMap<Integer, Integer> loadAlarmStatsMap(int duration) {

        ArrayMap<Integer, Integer> alarmStats = loadAllUidInfoInt(duration, GaugeSWStore.DETAIL_TABLE_NAME, GaugeSWStore.ALARM_TIMES);
        return alarmStats;
    }



    /*
    * function :查询出屏幕强制亮屏时间
    * durantion:查询时间长度
    * retun :返回各个uid对应的强制亮屏时间
    * */

    private ArrayMap<Integer,Integer> loadForceScreenOnMap(int duration){

        ArrayMap<Integer,Integer>retMap = loadAllUidInfoInt(duration, GaugeSWStore.DETAIL_TABLE_NAME, GaugeSWStore.FULL_WAKE_TIME);

        return retMap;

    }

    private int rescaleTimeMap(ArrayMap<Integer, Integer> map, int maxTime) {
        if (map.containsKey(OS_UID)){
            if (map.get(OS_UID) < 0)
                map.put(OS_UID, 0);
        }
        int totalTime = 0;
        Set entries = map.entrySet();
        if(entries != null && !entries.isEmpty()) {
            Iterator iterator = entries.iterator();
            while(iterator.hasNext()) {
                Map.Entry<Integer,Integer> entry = (Map.Entry<Integer,Integer>)iterator.next();
                totalTime += entry.getValue();
            }
        }

        //rescale
        if (totalTime > maxTime) {
            entries = map.entrySet();
            if(entries != null && !entries.isEmpty()) {
                Iterator iterator = entries.iterator();
                while(iterator.hasNext()) {
                    Map.Entry<Integer,Integer> entry = (Map.Entry<Integer,Integer>)iterator.next();
                    int uid = entry.getKey();
                    int time = entry.getValue();

                    long itemTimeLong = (long) time * (long) maxTime / (long) totalTime;
                    int itemTimeInt = (int) (itemTimeLong);
                    map.put(uid, itemTimeInt);

                }
            }
        }

        //need to be removed uids
        ArrayList<Integer> rmUids = new ArrayList<Integer>();
        entries = map.entrySet();
        if(entries != null && !entries.isEmpty()) {
            Iterator iterator = entries.iterator();
            while(iterator.hasNext()) {
                Map.Entry<Integer,Integer> entry = (Map.Entry<Integer,Integer>)iterator.next();
                int uid = entry.getKey();
                int time = entry.getValue();
                int secs = getNomilizedSecs(time);
                map.put(uid, secs);
                if (secs <= 0)
                    rmUids.add(uid);
            }
        }

        for (int uid : rmUids){
            map.remove(uid);
        }

        int totalSecs = 0;
        entries = map.entrySet();
        if(entries != null && !entries.isEmpty()) {
            Iterator iterator = entries.iterator();
            while(iterator.hasNext()) {
                Map.Entry<Integer,Integer> entry = (Map.Entry<Integer,Integer>)iterator.next();
                totalSecs += entry.getValue();
            }
        }
        return totalSecs;
    }


    // for hardware info
    private void processHwInfoMap(int duration) {
        mHwCommonUsageMap = loadHwCommonUsageMap(duration);
        mCellWeakSecs = loadCELLWeakSecs(duration);
    }

    //for usage time
    private ArrayMap<Integer, Integer> loadHwCommonUsageMap(int duration) {

        ArrayMap<Integer,Integer> mapDb = MutiTable.getDurGroupSumMapInt(mContext, GaugeHWStore.TYPE, GaugeHWStore.TABLE_NAME, GaugeHWStore.TIME, duration) ;

        if (mapDb == null || mapDb.isEmpty())
            return new ArrayMap<Integer, Integer>();

        ArrayMap<Integer, Integer>dataMap = new ArrayMap<Integer,Integer>();

        for(int type = PowerHWDetail.IDLE; type < PowerHWDetail.TYPE_SIZE ; type++ ) {
            if (type == PowerHWDetail.SCREEN)
                continue;

            if (mapDb.containsKey(type)) {
                int time = mapDb.get(type);
                time = getNomilizedSecs(time);
                if (time > 0) {
                    dataMap.put(type, time);
                }
            }
        }

        return dataMap;
    }

    private int loadCELLWeakSecs(int duration) {
        return loadHwExtraSecs(PowerHWDetail.CELL, duration);
    }

    // @return : return wifi usage secs
    // only for wifi mobiel phone!!!
    private int getCommonUsageSecs(int type) {
        int secs = 0;
        if (mHwCommonUsageMap == null || mHwCommonUsageMap.isEmpty() || !mHwCommonUsageMap.containsKey(type))
            return 0;

        secs = mHwCommonUsageMap.get(type);

        if (type == PowerHWDetail.WIFI)
            secs += mDetaTimeStats.detaTypeWifiSecs;
        else if (type == PowerHWDetail.CELL)
            secs += mDetaTimeStats.detaTypeCellSecs;
        else if (type == PowerHWDetail.PHONE)
            secs += mDetaTimeStats.detaTypePhoneSecs;
        else
            return 0;

        if (secs > mDuration * 60)
            secs = mDuration * 60;

        return secs;
    }


    //for hw common usage time
    private int loadHwUsageSecs(int type, int duration){
        int usageTime = MutiTable.getDurElementInt(mContext, GaugeHWStore.TYPE, type, GaugeHWStore.TABLE_NAME, GaugeHWStore.TIME,duration);

        if (usageTime == WRONG_VALUE)
            return 0;

        return getNomilizedSecs(usageTime);   //100ms to secondes;
    }


    //for hw act time
    private int loadHwExtraSecs(int type, int duration) {
        int extraTime = MutiTable.getDurElementInt(mContext, GaugeHWStore.TYPE, type, GaugeHWStore.TABLE_NAME, GaugeHWStore.TIME_EXT,duration);

        if (extraTime == WRONG_VALUE)
            return 0;

        return getNomilizedSecs(extraTime);   //100ms to secondes;
    }


    //SwEleDetail.WAKELOCK_TIME
    //SwEleDetail.ALARM_TIMES
    //SwEleDetail.ALARM_FREQ
    //SwEleDetail.MOBILE_DATA
    //SwEleDetail.GPS_TIME
    //SwEleDetail.FORCESCREEN_TIME  FULL_WAKE_TIME

    private ArrayMap<Integer,Long> loadSomeSwElementsDb(int uid, int duration){

        ArrayMap<Integer,Long> eleMap = new ArrayMap<Integer, Long>();
        long currentTimePoint  = System.currentTimeMillis();
        long pastTimePoint = System.currentTimeMillis() - duration * 60 * 1000;

        ContentResolver cr = mContext.getContentResolver();
        Uri uri = GaugeIndexStore.URI;
        uri = Uri.withAppendedPath(uri,"common");
        uri = Uri.withAppendedPath(uri,GaugeSWStore.DETAIL_TABLE_NAME);

        StringBuilder projStr = new StringBuilder();

        projStr.append("sum" + "(" + GaugeSWStore.DETAIL_TABLE_NAME + "." + GaugeSWStore.WAKELOCK_TIME+") as wake" );
        projStr.append(",sum(" + GaugeSWStore.DETAIL_TABLE_NAME +"." + GaugeSWStore.ALARM_TIMES +") as alarm" );
        projStr.append(",sum(" + GaugeSWStore.DETAIL_TABLE_NAME +"." + GaugeSWStore.MOBILE_DATA +") as mobile" );
        projStr.append(",sum(" + GaugeSWStore.DETAIL_TABLE_NAME +"." + GaugeSWStore.GPS_TIME +") as gps" );
        projStr.append(",sum(" + GaugeSWStore.DETAIL_TABLE_NAME +"." + GaugeSWStore.FULL_WAKE_TIME+") as fullwake"  );
        projStr.append(",sum(" + GaugeSWStore.DETAIL_TABLE_NAME +"." + GaugeSWStore.WIFI_DATA +") as wifi" );

        String projString = projStr.toString();


        String[] projection = new String[] {
                projString
        };

        String selection = GaugeIndexStore.TABLE_NAME +"." + GaugeIndexStore.TIME +
                " between ? and ? and " + GaugeSWStore.DETAIL_TABLE_NAME + "." +GaugeSWStore.UID + " = " + String.valueOf(uid) ;

        String[] selectionArgs = new String[] {
                String.valueOf(pastTimePoint),String.valueOf(currentTimePoint)
        };

        Cursor cursor = null;

        try {

            cursor = cr.query(uri, projection, selection, selectionArgs, null);
            if (cursor == null || cursor.getCount()<1) {
                Log.e(TAG, "get app detail failed");
                eleMap.put(SwEleDetail.APP_STATS, SwEleDetail.APP_REMOVED);
                return eleMap;
            }

            if (cursor.moveToFirst()) {

                int waketime = cursor.getInt(cursor.getColumnIndex("wake"));
                int wakesec = getNomilizedSecs(waketime);
                int alarm = cursor.getInt(cursor.getColumnIndex("alarm"));
                long mobiledata = cursor.getLong(cursor.getColumnIndex("mobile"));
                int gps = cursor.getInt(cursor.getColumnIndex("gps"));
                int gpssec = getNomilizedSecs(gps);
                int fullwake = cursor.getInt(cursor.getColumnIndex("fullwake"));
                int fullwakesec = getNomilizedSecs(fullwake);
                long wifidata = cursor.getLong(cursor.getColumnIndex("wifi"));

                eleMap.put(SwEleDetail.APP_STATS, SwEleDetail.APP_KEPT);
                eleMap.put(SwEleDetail.WAKELOCK_TIME, (long) wakesec);
                eleMap.put(SwEleDetail.ALARM_TIMES, (long) alarm);
                eleMap.put(SwEleDetail.MOBILE_DATA, mobiledata);
                eleMap.put(SwEleDetail.GPS_TIME, (long) gpssec);
                eleMap.put(SwEleDetail.FORCESCREEN_TIME, (long) fullwakesec);
                eleMap.put(SwEleDetail.WIFI_DATA, wifidata);
            }


        } catch (Exception e) {
            Log.e(TAG, " " + e);
        } finally {
            if (cursor!=null)
                cursor.close();
        }

        return eleMap;


    }

    private ArrayMap<Integer, Integer> loadAllUidInfoSecs(int duration, String columnName){
        //List<Integer> mUids =  loadDbUids(duration);
        if(mUids == null || mUids.isEmpty())
            return new ArrayMap<Integer, Integer>();

        ArrayMap<Integer,Integer> mapDb = MutiTable.getDurGroupSumMapInt(mContext, GaugeSWStore.UID, GaugeSWStore.DETAIL_TABLE_NAME, columnName, duration) ;

        if (mapDb == null || mapDb.isEmpty())
            return new ArrayMap<Integer, Integer>();

        ArrayMap<Integer, Integer>infoStats = new ArrayMap<Integer,Integer>();

        for(int uid : mUids) {
            if (mapDb.containsKey(uid)) {
                int time = mapDb.get(uid);
                time     = getNomilizedSecs(time);
                if (time > 0) {
                    infoStats.put(uid, time);
                }
            }
        }
        return infoStats;
    }

    private ArrayMap<Integer, Integer> loadAllUidInfoInt(int duration, String table, String columnName){

        if(mUids == null || mUids.isEmpty())
            return new ArrayMap<Integer, Integer>();

        ArrayMap<Integer,Integer> mapDb = MutiTable.getDurGroupSumMapInt(mContext, GaugeSWStore.UID, table, columnName, duration) ;

        if (mapDb == null || mapDb.isEmpty())
            return new ArrayMap<Integer, Integer>();

        ArrayMap<Integer, Integer>infoStats = new ArrayMap<Integer,Integer>();

        for(int uid : mUids) {
            if (mapDb.containsKey(uid)) {
                int value = mapDb.get(uid);
                if (value > 0) {
                    infoStats.put(uid, value);
                }
            }
        }
        return infoStats;
    }

    private ArrayMap<Integer, Long> loadAllUidInfoLong(int duration, String columnName){

        if(mUids == null || mUids.isEmpty())
            return new ArrayMap<Integer, Long>();

        ArrayMap<Integer,Long> mapDb = MutiTable.getDurGroupSumMapLong(mContext, GaugeSWStore.UID, GaugeSWStore.DETAIL_TABLE_NAME, columnName, duration);

        if (mapDb == null || mapDb.isEmpty())
            return new ArrayMap<Integer, Long>();

        ArrayMap<Integer, Long>infoStats = new ArrayMap<Integer,Long>();

        for(int uid : mUids) {
            if (mapDb.containsKey(uid)) {
                long value = mapDb.get(uid);
                if (value > 0) {
                    infoStats.put(uid, value);
                }
            }
        }
        return infoStats;
    }


    private boolean isAppExist(String pkgName) {

        String pkgNameMd5 = Str2MD5(pkgName);
        String[] projection = new String[] {
                PowerAppStore.PACKAGE_NAME
        };
        String selection = PowerAppStore.PACKAGE_NAME + " = ? ";
        String[] selectionArgs = new String[] {
                pkgNameMd5
        };

        ContentResolver cr = mContext.getContentResolver();

        Cursor cursor = null;
        try {
            cursor = cr.query(PowerAppStore.URI, projection, selection, selectionArgs,
                    PowerAppStore.PACKAGE_NAME);

            if (cursor == null || cursor.getCount() < 1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, " "+e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return true;
    }


    private void addToRunBack( String packageName, boolean ifAllow) {

        if (!isProductInternational()) {
            if (ifAllow) {
                addToAppWhiteBlackList(mContext, packageName, PowerStore.INT_FALSE, PowerStore.INT_TRUE);
            } else {
                addToAppWhiteBlackList(mContext, packageName, PowerStore.INT_TRUE, PowerStore.INT_FALSE);

            }
        } else {
            if (ifAllow) {
                addToAppWhiteBlackList(mContext, packageName, PowerStore.INT_TRUE, PowerStore.INT_TRUE);
            } else {
                addToAppWhiteBlackList(mContext, packageName, PowerStore.INT_TRUE, PowerStore.INT_FALSE);
            }
        }



    }

    private int addToAppWhiteBlackList(Context context, String packageName, int oflag, int flag) {
        ContentValues values = new ContentValues();
        values.put(PowerAppStore.PACKAGE_NAME, Str2MD5(packageName));

        if (oflag == PowerStore.INT_TRUE && flag == PowerStore.INT_TRUE)                             //11->0
            values.put(PowerAppStore.USR_BLACK, PowerStore.INT_FALSE);
        if (oflag == PowerStore.INT_TRUE && flag == PowerStore.INT_FALSE)                            //10->1
            values.put(PowerAppStore.USR_BLACK, PowerStore.INT_TRUE);
        if (oflag == PowerStore.INT_FALSE && flag == PowerStore.INT_FALSE)                           //00->0
            values.put(PowerAppStore.USR_BLACK, PowerStore.INT_FALSE);
        if (oflag == PowerStore.INT_FALSE && flag == PowerStore.INT_TRUE)
            values.put(PowerAppStore.USR_BLACK, PowerStore.INT_FALSE);

        values.put(PowerAppStore.DEFAULT, oflag);
        values.put(PowerAppStore.USR_SETTING, flag);
        values.put(PowerAppStore.APP_LEVEL, PowerStore.INT_NULL);
        values.put(PowerAppStore.APP_TYPE, PowerStore.INT_NULL);

        ContentResolver cr = context.getContentResolver();

        Uri uri = cr.insert(PowerAppStore.URI, values);

        if (uri == null)
            return -1;

        return (int) ContentUris.parseId(uri);
    }

    private int updateRunBack(String packageName, boolean ifAllow) {

        ContentValues values = new ContentValues();
        String selection = PowerAppStore.PACKAGE_NAME + " = ? ";
        String[] selectionArgs = new String[] {
                Str2MD5(packageName)
        };

        if(ifAllow) {
            values.put(PowerAppStore.USR_SETTING, PowerStore.INT_TRUE);
            values.put(PowerAppStore.USR_BLACK, PowerStore.INT_FALSE);
        }
        else {
            values.put(PowerAppStore.USR_SETTING, PowerStore.INT_FALSE);
            values.put(PowerAppStore.USR_BLACK, PowerStore.INT_TRUE);
        }

        ContentResolver cr = mContext.getContentResolver();

        return cr.update(PowerAppStore.URI, values, selection, selectionArgs);

    }



    private String Str2MD5(String str) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }


    // 100mills round to secs
    private int getNomilizedSecs(int time) {
        int secs = (time + 5)/10;
        return secs;
    }




}
