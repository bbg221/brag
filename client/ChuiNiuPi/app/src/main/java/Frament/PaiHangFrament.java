package Frament;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.chuiniupi.R;

/**
 * Created by Administrator on 2016/6/12.
 */

public class PaiHangFrament extends Fragment {
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity=getActivity();
        View view = inflater.inflate(R.layout.paihang_frament, null);
        return view;
    }
}
