package com.ht.devutils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 定位工具类使用指南、
 * 第一步：请先在高德地图进行创建应用，注意包名跟sha1值，放入到清单文件
 *
 *      <meta-data
 *          android:name="com.amap.api.v2.apikey"
 *          android:value="8eaf6c4ae657b7a39b01755c78a5ea45" />   <!-- 记得更换key -->
 *      <!-- 定位需要的服务 -->
 *      <service android:name="com.amap.api.location.APSService" />
 *
 * 第二步：在Application里面要进行初始化，LocationUtils.initLocation(this);
 * 第三步：调用的时候，调getLocation（）这个方法
 * Created by huangtao on 2017/3/17.
 */

public class LocationUtils
{
    private static final String TAG = "LocationUtils";
    private static AMapLocationClient locationClient = null;

    public static void initLocation(Context context)
    {
        //初始化client
        locationClient = new AMapLocationClient(context);
        //设置定位参数
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setGpsFirst(false);
        locationOption.setOnceLocation(true);
        locationOption.setOnceLocationLatest(true);
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    static AMapLocationListener locationListener = new AMapLocationListener()
    {
        @Override
        public void onLocationChanged(AMapLocation loc)
        {
            if (mLocationListener != null)
            {
                if (loc.getErrorCode() == 0)
                {
                    StringBuffer sb = new StringBuffer("{");
                    sb.append("\"address\":\"" + loc.getAddress() + "\",");
                    sb.append("\"longitude\":\"" + loc.getLongitude() + "\",");
                    sb.append("\"latitude\":\"" + loc.getLatitude() + "\",");
                    sb.append("\"accuracy\":\"" + loc.getAccuracy() + "米\",");
                    sb.append("\"country\":\"" + loc.getCountry() + "\",");
                    sb.append("\"province\":\"" + loc.getProvince() + "\",");
                    sb.append("\"city\":\"" + loc.getCity() + "\",");
                    sb.append("\"district\":\"" + loc.getDistrict() + "\",");
                    sb.append("\"cityCode\":\"" + loc.getCityCode() + "\",");
                    sb.append("\"poiName\":\"" + loc.getPoiName() + "\"}");
                    Log.e(TAG, "onLocationChanged: " + sb.toString());
                    mLocationListener.onReceiveLocation(sb.toString());
                }
                else
                {
                    Log.e(TAG, "onLocationChanged: "+ loc.getErrorInfo());
                    mLocationListener.onReceiveLocation(loc.getErrorInfo());
                }
            }
        }
    };

    private static LocationListener mLocationListener = null;

    public interface LocationListener
    {
        void onReceiveLocation(String location);
    }

    public static void getLocation(LocationListener listener)
    {
        mLocationListener = listener;
        locationClient.startLocation();
    }

    public static void stopLoacation()
    {
        if (locationClient != null && locationClient.isStarted())
        {
            locationClient.stopLocation();
        }
    }
}
