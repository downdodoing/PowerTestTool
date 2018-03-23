package com.meizu.powertesttool.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.meizu.powertesttool.R;

/**
 * Created by wangwen1 on 17-12-9.
 */

public class GpsListenerActivity extends Activity{
    private static final String TAG = "GpsListenerActivity";
    private Button mStartButton;
    private Button mCancelButton;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_listener_layout);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//      if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        //只监听gps状态变化
//          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 1f, locationListener);
//      }else{
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 1f,locationListener);
//      }

//      Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if(location != null){
//          checkDistance(location);
//        }


    }






//    private void checkDistance(Location location) {
//        if (location != null) {
//            float[] results = new float[1];
//            for (Store store : stores) {
//                Location.distanceBetween(location.getLatitude(),
//                        location.getLongitude(), store.getLatitude(),
//                        store.getLongitude(), results);
//                float result=(results[0] / 1000);//km
//                if (result < distance) {
//
//
//                    showNotification(store);
//
//                    stopSelf();//不要频繁的提醒
//                    break;
//                }
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
//            locationManager.removeUpdates(locationListener);
            locationManager=null;
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        public void onLocationChanged(Location location) {
            // log it when the location changes
            Log.d(TAG,"Moving " + location);
            if (location != null) {
//                checkDistance(location);
            }
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        public void onProviderDisabled(String provider) {
        }

        //  Provider被enable时触发此函数，比如GPS被打开
        public void onProviderEnabled(String provider) {
        }

        // Provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

}
