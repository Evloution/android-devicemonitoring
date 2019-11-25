package com.evloution.devicemonitoring.httprequest;

import com.evloution.devicemonitoring.util.ParamUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class HttpRequestPost {
    private static final int REQUEST_TIMEOUT = 10 * 1000;//设置请求超时10秒钟
    private static final int SO_TIMEOUT = 10 * 1000;  //设置等待数据超时时间10秒钟
    private static HttpClient httpClient;
    private static HttpURLConnection httpURLConnection;

    /**
     * @param path 请求的服务器路径
     * @param list 发送的数据
     * @return 请求的结果
     */
    public static String sendDataPostRequest(String path, List<NameValuePair> list) {
        try {
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(ParamUtil.address + path);
            httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == 200) {
                // 获得 HttpEntity 对象
                HttpEntity httpEntity = httpResponse.getEntity();
                // 获得输入流
                InputStream inputStream = httpEntity.getContent();
                String result = Stringtools.readStrem(inputStream);
                return result;
            } else {
                return "FAILURE";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRORCODE";
        }
    }

    public static String sendHttpPostRequest(String path) {
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(SO_TIMEOUT);
            httpURLConnection.setReadTimeout(REQUEST_TIMEOUT);
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            String data = "sgs";
            outputStream.write(data.getBytes("utf-8"));
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                String result = Stringtools.readStrem(inputStream);
                return result;
            } else {
                return "FAILURE";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRORCODE";
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
