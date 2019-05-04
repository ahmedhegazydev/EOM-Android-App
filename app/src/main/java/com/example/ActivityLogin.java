package com.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.example.handasy.controller.GetData;
import com.example.handasy.fragments.Login;
import com.example.handasy.fragments.LoginHome;
import com.example.handasy.R;
import com.example.handasy.fragments.Signup;

import org.json.JSONObject;

import com.example.handasy.helpers.Helper;
import com.example.handasy.model.DataBase;

/**
 * Created by ahmed on 4/1/2017.
 */

public class ActivityLogin extends FragmentActivity {

    public static String C_ID = "", name = "", mobile = "", userId = "";
    public static boolean login;
    public static String logo = "";
    SharedPreferences mPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {

        login = false;

        mPrefs = getSharedPreferences("data", MODE_PRIVATE);

        if (mPrefs.getString("mobile", "").equals("")) {

            //for the first time the app installed
            //new Helper().createToast(getApplicationContext(), "1", Toast.LENGTH_SHORT);
            login_home();

        } else {

            //new Helper().createToast(getApplicationContext(), "2", Toast.LENGTH_SHORT);
            login_home();
            new GetData(new GetData.AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    try {
                        JSONObject bundle = new JSONObject(result);
                        ActivityLogin.C_ID = bundle.getString("CustmoerId");
                        ActivityLogin.name = bundle.getString("CustmoerName");
                        ActivityLogin.logo = bundle.getString("CustomerPhoto");
                        ActivityLogin.mobile = bundle.getString("Mobile");
                        ActivityLogin.userId = bundle.getString("UserId");
                        startActivity(new Intent(ActivityLogin.this, ActivityMain.class).putExtra("kind", "login"));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, this, true).execute(new DataBase().WebService + "GetEmptByMobileNum", "?mobileNum=" + mPrefs.getString("mobile", ""));
        }

        //check if the user in registered or not
        //for the first time it will be neither login nor signup

        try {
            Intent intent = getIntent();
            String kind = intent.getStringExtra("kind");
            if (kind.equals("login")) {

                new Helper().createToast(getApplicationContext(), "login", Toast.LENGTH_SHORT);

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Login fragment = new Login();
                ft.replace(R.id.activity_main_content_fragment3, fragment);
                ft.commit();

            } else if (kind.equals("signup")) {

                new Helper().createToast(getApplicationContext(), "signup", Toast.LENGTH_SHORT);

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Signup fragment = new Signup();
                ft.replace(R.id.activity_main_content_fragment3, fragment);
                ft.commit();

            }
        } catch (Exception e) {

        }
    }

    private void login_home() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginHome fragment = new LoginHome();
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.commit();
    }


    //xml onclick
    public void login(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Login fragment = new Login();
        ft.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_right, R.anim.translat_left,
                R.anim.translat_left);
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.addToBackStack(null);
        ft.commit();

        //new Helper().createToast(getApplicationContext(), "login btn", Toast.LENGTH_SHORT);

    }


    //xml onclick
    public void signup(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Signup fragment = new Signup();
        ft.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_right, R.anim.translat_left,
                R.anim.translat_left);
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.addToBackStack(null);
        ft.commit();

        //new Helper().createToast(getApplicationContext(), "signup btn", Toast.LENGTH_SHORT);

    }



    public void skip(View view) {
        startActivity(new Intent(ActivityLogin.this, ActivityMain.class).putExtra("kind", "skip"));

    }

}

