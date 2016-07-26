package com.example.administrator.chuiniupi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import Frament.NoticeFragment;
import Frament.RankingFragment;
import Frament.MeFrament;
import Frament.CombatFragment;
import Frament.GauntletFragment;
import popuwindow.MorePopWindow;

/**
 * Created by Administrator on 2016/6/8.
 * Fragment管理器
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private RadioGroup radioGroup;
    private NoticeFragment gogngao;
    private RankingFragment pahang;
    private CombatFragment yuezhan;
    private MeFrament wo;
    private GauntletFragment zhansh;
    private FrameLayout fm;
    private FragmentManager sha;
    private FragmentTransaction ddd;
    private RadioButton bt_gonggao, bt_yuezhan, bt_paihang, bt_zhanshu, bt_wode;
    private LinearLayout share;

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
        share=(LinearLayout)findViewById(R.id.ll_share);
        //设置监听
        share.setOnClickListener(this);
        radioGroup.setOnClickListener(this);
        bt_gonggao.setOnClickListener(this);
        bt_yuezhan.setOnClickListener(this);
        bt_paihang.setOnClickListener(this);
        bt_wode.setOnClickListener(this);
        bt_zhanshu.setOnClickListener(this);
        //获得FRAMENT
        gogngao = new NoticeFragment();//公告
        pahang = new RankingFragment();//排行
        yuezhan = new CombatFragment();//约战
        wo = new MeFrament();//我的
        zhansh = new GauntletFragment();//战书
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
                    gogngao = new NoticeFragment();
                }
                ddd.show(gogngao).hide(pahang).hide(yuezhan).hide(wo).hide(zhansh);
                ddd.commit();
                break;
            case R.id.iv_yuezhan:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    yuezhan = new CombatFragment();
                }
                ddd.hide(gogngao).hide(pahang).show(yuezhan).hide(wo).hide(zhansh);
                ddd.commit();
                break;
            case R.id.iv_paihang:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    pahang = new RankingFragment();
                }
                ddd.hide(gogngao).show(pahang).hide(yuezhan).hide(wo).hide(zhansh);
                ddd.commit();
                break;
            case R.id.iv_zhanshu:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    zhansh = new GauntletFragment();
                }
                ddd.hide(gogngao).hide(pahang).hide(yuezhan).hide(wo).show(zhansh);
                ddd.commit();
                break;
            case R.id.iv_wode:
                ddd = sha.beginTransaction();
                if (ddd == null) {
                    wo = new MeFrament();
                }
                ddd.hide(gogngao).hide(pahang).hide(yuezhan).show(wo).hide(zhansh);
                ddd.commit();
                break;
            case R.id.ll_share:
                MorePopWindow morePopWindow = new MorePopWindow(MainActivity.this);
                morePopWindow.showPopupWindow(share);
                break;
        }
    }
}
