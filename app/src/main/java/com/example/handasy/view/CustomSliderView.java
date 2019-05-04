package com.example.handasy.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.example.handasy.R;

public class CustomSliderView extends BaseSliderView {

    //Constructor
    public CustomSliderView(Context context) {
        super(context);
    }


    public View getView() {

        View v = LayoutInflater.from(this.getContext()).inflate(R.layout.render_type_text, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        LinearLayout frame = (LinearLayout) v.findViewById(R.id.description_layout);
        frame.setBackgroundColor(Color.parseColor("#00FF9800"));

        this.bindEventAndShow(v, target);

        return v;
    }
}