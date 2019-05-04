package com.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.handasy.model.Drawer;
import com.example.handasy.fragments.Gallery;
import com.example.handasy.fragments.MyProjects;
import com.example.handasy.fragments.NewProject1;
import com.example.handasy.R;

import com.example.handasy.fragments.About;
import com.example.handasy.fragments.ContactMain;

import java.util.HashMap;
import java.util.UUID;

    /*
        This source code could be used for academic purposes only. Posting on other websites or blogs is only allowed with a dofollow link to the orignal content.
    */

public class ActivityProjectAdd extends FragmentActivity {
    public  static  float d ;
    public static  String projectID;
    public static Button right, left, middle;
    public static ImageView indicator;
    public static HashMap<String,String> projectData;
    public static FrameLayout frameLayout;
    private Drawer drawer;



    @Override
    public void onBackPressed() {
        if(drawer.back())
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        d= getResources().getDisplayMetrics().density;
        frameLayout=(FrameLayout)findViewById(R.id.activity_main_content_fragment3);
        drawer = new Drawer(this,"");



        projectID = UUID.randomUUID().toString();

        right = (Button) findViewById(R.id.rightButton);
        left = (Button) findViewById(R.id.leftButton);
        middle = (Button) findViewById(R.id.next);
        indicator = (ImageView) findViewById(R.id.indicator);

        projectData = new HashMap<>();

        Intent intent = getIntent();
        String kind = "";
        //  kind = intent.getStringExtra("kind");
        kind = "add";
            addProject();

    }

    private void editProject() {

    }

    private void addProject() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        NewProject1 fragment = new NewProject1();
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.commit();
    }


    public void back(View view) {
        super.onBackPressed();
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
        startActivity(new Intent(this, ActivityProjectAdd.class));
    }

    public void logout(View view) {
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ContactMain fragment = new ContactMain();
        ft.replace(R.id.activity_main_content_fragment3, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}
