package com.example.handasy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.example.handasy.R;

import com.example.handasy.view.CustomSliderView;

/**
 * Created by ahmed on 4/1/2017.
 */

public class LoginHome extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_home, container, false);
        SliderLayout sliderLayout=(SliderLayout)view.findViewById(R.id.slider);

        CustomSliderView textSliderView = new CustomSliderView(getActivity());
        textSliderView.image(R.drawable.slider1);


        CustomSliderView textSliderView2 = new CustomSliderView(getActivity());
        textSliderView2.image(R.drawable.slider1);



        CustomSliderView textSliderView3 = new CustomSliderView(getActivity());
        textSliderView3.image(R.drawable.slider1);


        sliderLayout.addSlider(textSliderView);
        sliderLayout.addSlider(textSliderView2);
        sliderLayout.addSlider(textSliderView3);
        sliderLayout.setCustomIndicator((PagerIndicator) view.findViewById(R.id.custom_indicator));

        return view;
    }

}
