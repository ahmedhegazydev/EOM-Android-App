package com.example.handasy.helpers;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ahmed on 07/08/17.
 */

public class Helper {


    public void createToast(Context context, String msg, int duration){
        Toast.makeText(context, msg, duration).show();
    }


}
