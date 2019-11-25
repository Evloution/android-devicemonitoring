package com.evloution.devicemonitoring.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019-10-30
 * @Email： 15227318030@163.com
 */
public class MarketDataBean {

    private Date createdate;
    private String devicecode;
    private String devicehumidity;
    private String deviceid;
    private String deviceIP;
    private double devicelatitude;
    private double devicelongitude;
    private String devicePM25;
    private String devicetemperature;
    private String useraccount;

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
    public Date getCreatedate() {
        return createdate;
    }

    public void setDevicecode(String devicecode) {
        this.devicecode = devicecode;
    }
    public String getDevicecode() {
        return devicecode;
    }

    public void setDevicehumidity(String devicehumidity) {
        this.devicehumidity = devicehumidity;
    }
    public String getDevicehumidity() {
        return devicehumidity;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }
    public String getDeviceIP() {
        return deviceIP;
    }

    public double getDevicelatitude() {
        return devicelatitude;
    }

    public void setDevicelatitude(double devicelatitude) {
        this.devicelatitude = devicelatitude;
    }

    public double getDevicelongitude() {
        return devicelongitude;
    }

    public void setDevicelongitude(double devicelongitude) {
        this.devicelongitude = devicelongitude;
    }

    public void setDevicePM25(String devicePM25) {
        this.devicePM25 = devicePM25;
    }
    public String getDevicePM25() {
        return devicePM25;
    }

    public void setDevicetemperature(String devicetemperature) {
        this.devicetemperature = devicetemperature;
    }
    public String getDevicetemperature() {
        return devicetemperature;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }
    public String getUseraccount() {
        return useraccount;
    }

    public static List<MarketDataBean> showData(MarketDataBean marketBean) {
        List<MarketDataBean> marketBeanList = new ArrayList<>();
        marketBeanList.add(marketBean);
        return marketBeanList;
    }
}
