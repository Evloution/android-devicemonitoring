package com.evloution.devicemonitoring.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @Description：解析JSON数据类
 * @Author： Evloution_
 * @Date： 2019-10-23
 * @Email： 15227318030@163.com
 */
public class JSONAnalysisUtil {
    private static JSONObject object = null;
    public static String msg = null;
    public static String code = null;
    public static String data = null;
    public static int  count = 0;

    /**
     * JSON解析出msg
     *
     * @param string
     * @return msg信息
     */
    public static String JSONMsgAnalysis(String string) {
        try {
            object = new JSONObject(string);
            msg = object.optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * JSON解析出code
     *
     * @param string
     * @return code码
     */
    public static String JSONCodeAnalysis(String string) {
        try {
            object = new JSONObject(string);
            code = object.optString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * JSON解析出返回值
     *
     * @param string
     * @return data
     */
    public static String JSONDataAnalysis(String string) {
        try {
            object = new JSONObject(string);
            data = object.optString("data");
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 解析出返回值中的每一项
     * @param string 字符串
     * @param value 要解析的值
     * @return
     */
    public static String getJsonDataValue(String string, String value) {
        String jsonData =null;
        try {
            object = new JSONObject(string);
            jsonData =object.getString(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    /**
     * 解析人员信息
     * @param string
     * @return
     */
    public static String JSONPersonAnalysis(String string, String val) {
        try {
            object = new JSONObject(string);
            data = object.optString(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * JSON解析出返回值
     *
     * @param string
     * @return data
     */
    public static JSONArray JSONDataJsonObject(String string) {
        JSONArray jsonData =null;
        try {
            object = new JSONObject(string);
            jsonData =object.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData;
    }
}
