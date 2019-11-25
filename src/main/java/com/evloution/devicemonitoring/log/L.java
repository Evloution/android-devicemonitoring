package com.evloution.devicemonitoring.log;

import android.util.Log;

/**
 * @Description：log日志输出
 * @Author： Evloution_
 * @Date： 2019/9/20
 * @Email： 15227318030@163.com
 */
public class L {
    private static boolean debug = true;
    public static String TAG;

    public static void e(String msg) {
        if (debug)
            Log.e(TAG, msg);
    }

    public static void i(String msg) {
        if (debug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (debug)
            Log.d(TAG, msg);
    }
}
