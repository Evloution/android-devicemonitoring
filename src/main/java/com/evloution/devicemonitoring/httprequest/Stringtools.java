package com.evloution.devicemonitoring.httprequest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Stringtools {

	public static String readStrem(InputStream is) {
		try {
			// 获取一个字节数组输出流的对象
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// 创建一个缓冲区 大小1024字节
			byte[] by = new byte[1024];
			// 设置返回值
			int len = -1;
			// 循环遍历读取输入流的东西 并将数据写入一个字符数组
			while ((len = is.read(by)) != -1) {
				bos.write(by, 0, len);
			}
			is.close();
			bos.close();
			// 最后将字符数组转换为字符串并且返回
			return new String(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			String errorStr = "获取数据失败";
			return errorStr;
		}
	}
}
