package Frament;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.chuiniupi.R;
import com.example.administrator.chuiniupi.GameSceneActivity;

import static com.example.administrator.chuiniupi.R.id.lv_list;

/**
 * Created by Administrator on 2016/6/12.
 * 约战界面
 */

public class CombatFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private ListView listvie;
    private ImageView suijikaishi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     activity =getActivity();
        View view = inflater.inflate(R.layout.yuezhan_frament, null);
        listvie =(ListView)view.findViewById(lv_list);
        suijikaishi=(ImageView)view.findViewById(R.id.iv_suijikaishi);
        //设置监听
        suijikaishi.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_suijikaishi:
                startActivity(new Intent(activity, GameSceneActivity.class));
                break;
        }
    }
}
