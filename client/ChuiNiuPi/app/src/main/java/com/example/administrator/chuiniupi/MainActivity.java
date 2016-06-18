package com.example.administrator.chuiniupi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.chuiniupi.R;

import Frament.GongGaoFrament;
import Frament.PaiHangFrament;
import Frament.WoFrament;
import Frament.YueZhanFrament;
import Frament.ZhanShuFrament;

/**
 * Created by Administrator on 2016/6/8.
 * Fragment管理器
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private RadioGroup radioGroup;
    private GongGaoFrament gogngao;
    private PaiHangFrament pahang;
    private YueZhanFrament yuezhan;
    private WoFrament wo;
    private ZhanShuFrament zhansh;
    private FrameLayout fm;
    private FragmentManager sha;
    private FragmentTransaction ddd;
    private RadioButton bt_gonggao, bt_yuezhan, bt_paihang, bt_zhanshu, bt_wode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duizhan_view);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        bt_gonggao = (RadioButton) findViewById(R.id.iv_gonggao);
        bt_yuezhan = (RadioButton) findViewById(R.id.iv_yuezhan);
        bt_paihang = (RadioButton) findViewById(R.id.iv_paihang);
        bt_zhanshu = (RadioButton) findViewById(R.id.iv_zhanshu);
        bt_wode = (RadioButton) findViewById(R.id.iv_wode);
        //设置监听
        radioGroup.setOnClickListener(this);
        bt_gonggao.setOnClickListener(this);
        bt_yuezhan.setOnClickListener(this);
        bt_paihang.setOnClickListener(this);
        bt_wode.setOnClickListener(this);
        bt_zhanshu.setOnClickListener(this);
        //获得FRAMENT
        gogngao = new GongGaoFrament();//公告
        pahang = new PaiHangFrament();//排行
        yuezhan = new YueZhanFrament();//约战
        wo = new WoFrament();//我的
        zhansh = new ZhanShuFrament();//战书
        //获得Frament碎片管理器
        sha = getSupportFragmentManager();
        ddd = sha.beginTransaction();
        ddd.add(R.id.fm_framen, gogngao).add(R.id.fm_framen, pahang).add(R.id.fm_framen, yuezhan).
                add(R.id.fm_framen, wo).add(R.id.fm_framen, zhansh);
        ddd.show(gogngao).hide(pahang).hide(yuezhan).hide(wo).hide(zhansh);
        ddd.commit();//提交服务器
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_gonggao:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    gogngao = new GongGaoFrament();
                }
                ddd.show(gogngao).hide(pahang).hide(yuezhan).hide(wo).hide(zhansh);
                ddd.commit();
                break;
            case R.id.iv_yuezhan:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    yuezhan = new YueZhanFrament();
                }
                ddd.hide(gogngao).hide(pahang).show(yuezhan).hide(wo).hide(zhansh);
                ddd.commit();
                break;
            case R.id.iv_paihang:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    pahang = new PaiHangFrament();
                }
                ddd.hide(gogngao).show(pahang).hide(yuezhan).hide(wo).hide(zhansh);
                ddd.commit();
                break;
            case R.id.iv_zhanshu:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    zhansh = new ZhanShuFrament();
                }
                ddd.hide(gogngao).hide(pahang).hide(yuezhan).hide(wo).show(zhansh);
                ddd.commit();
                break;
            case R.id.iv_wode:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    wo = new WoFrament();
                }
                ddd.hide(gogngao).hide(pahang).hide(yuezhan).show(wo).hide(zhansh);
                ddd.commit();
                break;
        }
    }
}
