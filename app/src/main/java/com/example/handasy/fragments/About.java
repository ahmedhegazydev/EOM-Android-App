package com.example.handasy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ActivityMain;
import com.example.handasy.model.Drawer;
import com.example.handasy.R;

/**
 * Created by ahmed on 3/25/2017.
 */

public class About extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);
        Drawer.title.setText("عن المكتب");
       if(ActivityMain.active){ ActivityMain.option.setImageResource(R.drawable.back);
        ActivityMain.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });}

        Drawer.postionSelected = 4;
        Drawer.drawerAdapter.notifyDataSetChanged();

        return view;
    }
}
