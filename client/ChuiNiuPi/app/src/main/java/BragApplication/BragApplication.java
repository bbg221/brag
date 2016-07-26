package BragApplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import service.FirstService;

/**
 * Created by Administrator on 2016/7/24.
 * 配置全局上下文。
 */

public class BragApplication extends Application {
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        servers();
    }

    private void servers() {
        Intent intent=new Intent();
        intent.setClass(this, FirstService.class);
        startService(intent);
    }

}
