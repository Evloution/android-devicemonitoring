package com.evloution.devicemonitoring.bean;

import java.util.List;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019-10-30
 * @Email： 15227318030@163.com
 */
public class MarketRootBean {
    private String msg;
    private int code;
    private List<MarketDataBean> marketDataBeanList;

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setData(List<MarketDataBean> marketDataBeanList) {
        this.marketDataBeanList = marketDataBeanList;
    }
    public List<MarketDataBean> getData() {
        return marketDataBeanList;
    }
}
