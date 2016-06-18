package com.example.administrator.chuiniupi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Administrator on 2016/4/8.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener{
    public BaseActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        lodLayout();
        findView();
        getData();
        setData();
        onClick();
    }
    protected abstract void lodLayout();
    protected abstract void findView();
    protected abstract void getData();
    protected abstract void setData();
    protected abstract void onClick();
}
