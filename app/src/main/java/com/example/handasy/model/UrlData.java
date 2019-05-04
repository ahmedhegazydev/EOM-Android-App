package com.example.handasy.model;

/**
 * Created by ahmed on 4/24/2017.
 */

public class UrlData {
    String string="?";
    public void add(String s)
    {
        if(string.equals("?"))
            string+=s;
        else
            string+="&"+s;
    }
    public String get()
    {
        return string;
    }
}
