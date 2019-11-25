package com.evloution.devicemonitoring.util;

import android.content.Context;
import android.widget.Toast;


/**
 * @Description：Toast弹出框封装类
 * @Author： Evloution_
 * @Date： 2019-10-23
 * @Email： 15227318030@163.com
 */
public class ToastUtil {

	public static void show(Context context, String info) {
		Toast.makeText(context.getApplicationContext(), info, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context.getApplicationContext(), info, Toast.LENGTH_LONG).show();
	}
}
