package com.example.handasy.model;
import android.net.Uri;

/**
 * Created by ahmed on 4/26/2017.
 */
public class Image_Data {
    public Uri path;
    public String Id;
    public int orintation;

    public Image_Data(Uri p, String i,int o){
        path=p;
        Id=i;
        orintation=o;
    }
}