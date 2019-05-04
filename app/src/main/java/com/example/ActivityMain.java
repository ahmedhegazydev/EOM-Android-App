package com.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.handasy.model.Drawer;
import com.example.handasy.fragments.Gallery;
import com.example.handasy.fragments.HomeSkip;
import com.example.handasy.fragments.MyProjects;
import com.example.handasy.R;

import com.example.handasy.fragments.About;
import com.example.handasy.fragments.ContactMain;

import com.example.handasy.view.CustomTextView;
import com.example.handasy.fragments.HomeLogin;

import butterknife.BindView;
import butterknife.ButterKnife;

    /*
        This source code could be used for academic purposes only. Posting on other websites or blogs is only allowed with a dofollow link to the orignal content.
    */

public class ActivityMain extends FragmentActivity {
    public static ImageView option;
    public static CustomTextView title;
    public static boolean active = true;
    String kind;
    Drawer drawer;
//    @BindView(R.id.ivLogOut) ImageView ivLogout = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
//        ivLogout.setImageDrawable(resize(getResources().getDrawable(R.drawable.entersign), 45, 45));


        option = (ImageView) findViewById(R.id.option);
        title = (CustomTextView) findViewById(R.id.title);
        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        if (kind.equals("skip")) {
            drawer = new Drawer(this, "skip");
            drawer.invisible();
        } else {
            {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MyProjects fragment = new MyProjects();
                ft.replace(R.id.activity_main_content_fragment3, fragment);
                ft.commit();
            }
            drawer = new Drawer(this, "");
        }
        Home();
    }



    private Drawable resize(Drawable image, int w, int h) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, w, h, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
    @Override
    public void onBackPressed() {
        if (drawer.back()) {
            super.onBackPressed();
        }
    }


    private void Home() {

        if (kind.equals("login")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            HomeLogin fragment = new HomeLogin();
            ft.replace(R.id.activity_main_content_fragment3, fragment);
            ft.commit();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            HomeSkip fragment = new HomeSkip();
            ft.replace(R.id.activity_main_content_fragment3, fragment);
            ft.commit();
        }
    }

    public void signup(View view) {
        startActivity(new Intent(ActivityMain.this, ActivityLogin.class).putExtra("kind", "signup"));
    }

    public void login(View view) {
        startActivity(new Intent(ActivityMain.this, ActivityLogin.class).putExtra("kind", "login"));
    }

    public void myProjects(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MyProjects fragment = new MyProjects();
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void newproject(View view) {
        startActivity(new Intent(ActivityMain.this, ActivityProjectAdd.class));
    }

    public void logOut(View view) {
        SharedPreferences mPrefs = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
        try {
            MyProjects.projectList.clear();
        } catch (Exception e) {

        }
        ActivityLogin.C_ID = "";
        ActivityLogin.name = "";
        ActivityLogin.mobile = "";
        startActivity(new Intent(this, ActivityLogin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public void teamWork(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Gallery fragment = new Gallery();
        Bundle bundle = new Bundle();
        bundle.putString("kind", "team");
        fragment.setArguments(bundle);
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void about(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        About fragment = new About();
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void projects(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Gallery fragment = new Gallery();
        Bundle bundle = new Bundle();
        bundle.putString("kind", "project");
        fragment.setArguments(bundle);
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void call(View view) {
        title.setText("اتصل بنا");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ContactMain fragment = new ContactMain();
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void back(View view) {
        super.onBackPressed();
    }

    public void google(View view) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }

    public void twitter(View view) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }

    public void facebook(View view) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        active = false;
        super.onStop();
    }

    @Override
    public void onResume() {
        active = false;
        super.onResume();
    }

    @Override
    public void onStart() {
        Drawer.postionSelected = 0;
        if (kind.equals("skip"))
            drawer = new Drawer(this, "skip");
        else
            drawer = new Drawer(this, "");
        super.onStart();
    }


}
