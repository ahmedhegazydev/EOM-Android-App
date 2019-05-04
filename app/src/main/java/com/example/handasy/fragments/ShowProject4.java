package com.example.handasy.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handasy.model.Drawer;
import com.example.handasy.R;

import com.example.ActivityShowProject;
import com.example.handasy.view.CustomTextView;

/**
 * Created by ahmed on 3/31/2017.
 */

public class ShowProject4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_project4, container, false);
        CustomTextView state = (CustomTextView) view.findViewById(R.id.state_data);
        CustomTextView notes = (CustomTextView) view.findViewById(R.id.notes_data);

        notes.setText("  " + ActivityShowProject.data.ProjectEngComment + "  ");
        state.setText("  " + ActivityShowProject.data.state + "  ");
        // Drawer.title.setText("");
        Drawer.postionSelected = -1;

        Drawable circel = ContextCompat.getDrawable(getContext(), R.drawable.circle2);
       try {
           circel.setColorFilter(Color.parseColor(ActivityShowProject.data.stateColor), PorterDuff.Mode.MULTIPLY);

        state.setCompoundDrawablesWithIntrinsicBounds(null, null, circel, null);
       }
       catch (Exception e){

       }
        return view;
    }
}
