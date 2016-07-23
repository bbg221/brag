package com.example.administrator.chuiniupi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;


public class LoginActivity extends Activity implements PlatformActionListener, View.OnClickListener {
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "27fe7909f8e8";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "3c5264e7e05b8860a9b98b34506cfa6e";
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            PlatformDb db = (PlatformDb) msg.obj;
            Toast.makeText(LoginActivity.this, "userId:" + db.getUserId() + "/userName:" + db.getUserName(), Toast.LENGTH_LONG).show();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化sharesdk,具体集成步骤请看文档：
        ShareSDK.initSDK(this);
        findViewById(R.id.iv_QQ).setOnClickListener(this);
        findViewById(R.id.iv_weixin).setOnClickListener(this);
    }

    //QQ登陆
    public void qqClick(View view) {
        login(QZone.NAME);
    }

    //新浪微博
    public void SinaClick(View view) {
        login(Wechat.NAME);
    }

    private void login(String platformName) {
        Platform platform = ShareSDK.getPlatform(platformName);
        platform.setPlatformActionListener(this);
        //关闭SSO授权
        platform.SSOSetting(true);
        platform.showUser(null);
    }

    //登陆完成
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        //获取用户数据
        PlatformDb db = platform.getDb();
        Message msg = Message.obtain();
        msg.obj = db;
        handler.sendMessage(msg);
    }

    //登陆错误
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(LoginActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_QQ:
                login(QZone.NAME);
                break;
            case R.id.iv_weixin:
                login(Wechat.NAME);
                break;
        }
    }
}