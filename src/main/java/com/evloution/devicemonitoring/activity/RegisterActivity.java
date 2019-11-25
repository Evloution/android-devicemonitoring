package com.evloution.devicemonitoring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.evloution.devicemonitoring.R;
import com.evloution.devicemonitoring.log.L;
import com.evloution.devicemonitoring.util.JSONAnalysisUtil;
import com.evloution.devicemonitoring.util.ToastUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019-11-07
 * @Email： 15227318030@163.com
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    // 用户账号输入框
    private EditText activity_register_username;
    // 用户密码输入框
    private EditText activity_register_password;
    // 提交按钮
    private Button activity_register_submit_btn;

    private String userName;
    private String userPassword;

    private OkHttpClient okHttpClient;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1: // 说明请求失败
                    ToastUtil.show(RegisterActivity.this, "连接失败");
                    break;
                case 0: // 请求成功但数据有误
                    ToastUtil.show(RegisterActivity.this, (String) msg.obj);
                    break;
                case 1: // 请求成功
                    ToastUtil.show(RegisterActivity.this, "注册成功");
                    L.e("请求成功后的数据：" + msg.obj);
                    String institutionid = JSONAnalysisUtil.getJsonDataValue((String) msg.obj, "institutionid");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("institutionid", institutionid);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        activity_register_username = findViewById(R.id.activity_register_username);
        activity_register_password = findViewById(R.id.activity_register_password);
        activity_register_submit_btn = findViewById(R.id.activity_register_submit_btn);
    }

    private void initData() {
        activity_register_submit_btn.setOnClickListener(this);
    }

    private void initEvent() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_register_submit_btn: // 提交按钮的点击事件
                getSendData();
                break;
        }
    }

    private void getSendData() {
        userName = activity_register_username.getText().toString().trim();
        userPassword = activity_register_password.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.show(this, "请输入账号");
        } else if (TextUtils.isEmpty(userPassword)) {
            ToastUtil.show(this, "请输入密码");
        } else {
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            formEncodingBuilder.add("individualUserName", userName);
            formEncodingBuilder.add("individualPassword", userPassword);
            RequestBody requestBody = formEncodingBuilder.build();
            Request request = new Request.Builder().url("http://192.168.0.114/user/individualRegister").post(requestBody).build();
            executeRequest(request);
        }
    }

    // 对请求结果的封装
    private void executeRequest(Request request) {
        // 将request封装为call
        Call call = okHttpClient.newCall(request);
        // 执行call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                L.e("onFailure：" + e.getMessage());
                e.printStackTrace();
                if (e instanceof SocketTimeoutException) {
                    // 判断超时异常
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(RegisterActivity.this, "连接超时，请重新发送");
                        }
                    });
                }
                if (e instanceof ConnectException) {
                    // 判断连接异常，
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(RegisterActivity.this, "连接异常，请重新发送");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                L.e("onResponse：" + result);
                if (response.body() != null) {
                    response.body().close();
                }
                Message message = new Message();
                if ("ERRORCODE".equals(result) || "FAILURE".equals(result)) {
                    // 走这里说明请求失败
                    message.what = -1;
                    handler.sendMessage(message);
                } else {
                    String code = JSONAnalysisUtil.JSONCodeAnalysis(result);
                    // 走这里说明请求成功
                    if ("1".equals(code)) {
                        // 登陆成功
                        message.what = 1;
                        message.obj = JSONAnalysisUtil.JSONDataAnalysis(result);
                        handler.sendMessage(message);
                    } else {
                        // 登陆失败
                        message.what = 0;
                        message.obj = JSONAnalysisUtil.JSONMsgAnalysis(result);
                        handler.sendMessage(message);
                    }
                }
            }
        });
    }
}