package com.example.handasy.view;

/**
 * Created by ahmed on 3/25/2017.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomButton extends android.support.v7.widget.AppCompatButton {


    //Overloaded Constructor
    public CustomButton(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font.ttf");
        this.setTypeface(face);
    }

    //Overloaded Constructor
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font.ttf");
        this.setTypeface(face);
    }

    //Overloaded Constructor
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

    }

}
