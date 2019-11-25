package com.evloution.devicemonitoring.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.evloution.devicemonitoring.R;
import com.evloution.devicemonitoring.bean.MarketDataBean;
import com.evloution.devicemonitoring.log.L;

import java.util.List;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019-10-30
 * @Email： 15227318030@163.com
 */
public class WindowAdapter implements AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener {

    private Context context;
    private static final String TAG = "WindowAdapter";
    private List<MarketDataBean> marketDataBeanList;

    public WindowAdapter(Context context) {
        this.context = context;
    }

    public WindowAdapter(Context context, List<MarketDataBean> marketDataBeanList) {
        this.context = context;
        this.marketDataBeanList = marketDataBeanList;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        L.e("marketDataBeanList:" + marketDataBeanList);
        //关联布局
        View view = LayoutInflater.from(context).inflate(R.layout.layout_info_item, null);
        // 设备编码
        TextView device_info_devicecode = (TextView) view.findViewById(R.id.device_info_devicecode);
        // 设备纬度
        TextView device_info_latitude = (TextView) view.findViewById(R.id.device_info_latitude);
        // 设备经度
        TextView device_info_longitude = (TextView) view.findViewById(R.id.device_info_longitude);
        // 设备ip
        // TextView device_info_deviceip = (TextView) view.findViewById(R.id.device_info_deviceip);
        // 设备PM2.5值
        TextView device_info_devicepm25 = (TextView) view.findViewById(R.id.device_info_devicepm25);
        // 设备湿度
        TextView device_info_devicehumidity = (TextView) view.findViewById(R.id.device_info_devicehumidity);
        // 设备温度
        TextView device_info_devicetemperature = (TextView) view.findViewById(R.id.device_info_devicetemperature);

        device_info_devicecode.setText("设备编码：" + marketDataBeanList.get(marker.getPeriod()).getDevicecode());
        device_info_latitude.setText("设备纬度：" + marker.getPosition().latitude + "");
        device_info_longitude.setText("设备经度：" + marker.getPosition().longitude + "");
        device_info_devicepm25.setText("设备PM2.5：" + marketDataBeanList.get(marker.getPeriod()).getDevicePM25());
        device_info_devicehumidity.setText("设备湿度：" + marketDataBeanList.get(marker.getPeriod()).getDevicehumidity());
        device_info_devicetemperature.setText("设备温度：" + marketDataBeanList.get(marker.getPeriod()).getDevicetemperature());
        return view;
    }

    // 如果用自定义的布局，不用管这个方法,返回null即可
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    //绑定信息窗点击事件
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e(TAG, "InfoWindow被点击了");
    }

    // marker 对象被点击时回调的接口
    // 返回 true 则表示接口已响应事件，否则返回false
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e(TAG, "Marker被点击了");
        //marker.showInfoWindow();
        return true;
    }
}
