package service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import de.greenrobot.event.EventBus;
import socket.BragConnClient;
import types.LoginBean;
import util.JsonUtil;

/**
 * @author JackHappiness
 * @date 2016/7/9.
 * @verion 1.0
 * @summary 说明：登录全局Service
 */

public class FirstService extends Service {
    private NotificationManager manger;
    private BragConnClient bcc;

    /**
     * 服务在创建时调用，在创建的时候只执行一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        EventBus.getDefault().register(this);
        MyThread thread = new MyThread();
        thread.start();
        long idsCread= Thread.currentThread().getId();
        String namesCread = Thread.currentThread().getName();
        Log.e("idsCread",idsCread+"");
        Log.e("namesCread",namesCread);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 服务停止时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        manger.cancel(2);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyThread extends Thread{
        public void run(){
            long ids = Thread.currentThread().getId();
            String names = Thread.currentThread().getName();
            Log.e("ids",ids+"");
            Log.e("names",names);
            //你要实现的代码
            bcc = new BragConnClient();
            bcc.start();
        }
    }
    public void onEventBackgroundThread(LoginBean loginType) {
        String json = JsonUtil.toJson(loginType);
        int types = loginType.loginType;
        bcc.sendData(types,json);
        boolean loo=Looper.getMainLooper()==Looper.myLooper();
        long idsback= Thread.currentThread().getId();
        String namesback = Thread.currentThread().getName();
        Log.e("idsback",idsback+"");
        Log.e("namesback",namesback);
    }
}
