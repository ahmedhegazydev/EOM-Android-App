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

public class HomeLogin extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepagelogin, container, false);
        Drawer.title.setText("البراهيم للاستشارات الهندسية");
        ActivityMain.option.setImageResource(0);
        Drawer.postionSelected = 0;
        Drawer.drawerAdapter.notifyDataSetChanged();

        return view;
    }
}