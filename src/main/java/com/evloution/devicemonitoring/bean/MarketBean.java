package com.evloution.devicemonitoring.bean;

import android.os.Bundle;

import com.evloution.devicemonitoring.R;
import com.evloution.devicemonitoring.model.DotInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019-10-30
 * @Email： 15227318030@163.com
 */
public class MarketBean {
    private String deviceId; // 设备id
    private String deviceCode; // 设备编码
    private String deviceIP; // 设备IP
    private double deviceLatitude; // 设备纬度
    private double deviceLongitude; // 设备经度
    private double devicePM25; // PM2.5
    private double deviceHumidity; // 湿度
    private double deviceTemperature; // 温度
    private String createdate;
    private String useraccount;

    public MarketBean(String deviceIP, double deviceLatitude, double deviceLongitude, double devicePM25, double deviceHumidity, double deviceTemperature) {
        this.deviceIP = deviceIP;
        this.deviceLatitude = deviceLatitude;
        this.deviceLongitude = deviceLongitude;
        this.devicePM25 = devicePM25;
        this.deviceHumidity = deviceHumidity;
        this.deviceTemperature = deviceTemperature;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public double getDeviceLatitude() {
        return deviceLatitude;
    }

    public void setDeviceLatitude(double deviceLatitude) {
        this.deviceLatitude = deviceLatitude;
    }

    public double getDeviceLongitude() {
        return deviceLongitude;
    }

    public void setDeviceLongitude(double deviceLongitude) {
        this.deviceLongitude = deviceLongitude;
    }

    public double getDevicePM25() {
        return devicePM25;
    }

    public void setDevicePM25(double devicePM25) {
        this.devicePM25 = devicePM25;
    }

    public double getDeviceHumidity() {
        return deviceHumidity;
    }

    public void setDeviceHumidity(double deviceHumidity) {
        this.deviceHumidity = deviceHumidity;
    }

    public double getDeviceTemperature() {
        return deviceTemperature;
    }

    public void setDeviceTemperature(double deviceTemperature) {
        this.deviceTemperature = deviceTemperature;
    }

    public static List<MarketBean> initData() {
        List<MarketBean> marketBeanList = new ArrayList<>();
        /*marketBeanList.add(new MarketBean("192.168.1.0", 37.08529, 114.51331, 1.8, 46.8, 29.5));
        marketBeanList.add(new MarketBean("192.168.1.1", 38.04535, 114.48974, 1.8, 46.8, 29.5));
        marketBeanList.add(new MarketBean("192.168.1.2", 37.26532, 114.56252, 1.8, 46.8, 29.5));
        marketBeanList.add(new MarketBean("192.168.1.3", 37.25532, 114.46252, 1.8, 46.8, 29.5));
        marketBeanList.add(new MarketBean("192.168.1.4", 38.25532, 114.36252, 1.8, 46.8, 29.5));
        */return marketBeanList;
    }

    public static List<MarketBean> showData(MarketBean marketBean) {
        List<MarketBean> marketBeanList = new ArrayList<>();
        marketBeanList.add(marketBean);
        return marketBeanList;
    }

    @Override
    public String toString() {
        return "MarketBean{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceCode='" + deviceCode + '\'' +
                ", deviceIP='" + deviceIP + '\'' +
                ", deviceLatitude=" + deviceLatitude +
                ", deviceLongitude=" + deviceLongitude +
                ", devicePM25=" + devicePM25 +
                ", deviceHumidity=" + deviceHumidity +
                ", deviceTemperature=" + deviceTemperature +
                ", createdate='" + createdate + '\'' +
                ", useraccount='" + useraccount + '\'' +
                '}';
    }
}
