package com.example.handasy.model;

import android.app.Activity;
import android.view.View;

import com.example.handasy.R;

import com.example.handasy.view.CustomEditText;
import com.example.handasy.view.CustomTextView;

/**
 * Created by ahmed on 3/26/2017.
 */

public class InputText {

    public CustomEditText inputText;
    public CustomTextView errorText;
    public  View view;

    public InputText(Activity context) {
        view = context.getLayoutInflater().inflate(R.layout.input_text, null);
        inputText = (CustomEditText) view.findViewById(R.id.input_text);
        errorText = (CustomTextView) view.findViewById(R.id.error_massege);
        errorText.setText("مطلوب");
        errorText.setVisibility(View.INVISIBLE);
    }
}
