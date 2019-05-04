package com.example;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.handasy.model.Drawer;
import com.example.handasy.fragments.Gallery;
import com.example.handasy.fragments.MyProjects;
import com.example.handasy.model.ProjectData;
import com.example.handasy.R;
import com.example.handasy.fragments.ShowProject1;
import com.example.handasy.fragments.ShowProject2;
import com.example.handasy.fragments.ShowProject3;
import com.example.handasy.fragments.ShowProject4;

import com.example.handasy.fragments.About;
import com.example.handasy.fragments.ContactMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.handasy.model.DataBase;
import com.example.handasy.model.Image_Data;
import com.example.handasy.view.CustomTextView;
import com.example.handasy.controller.GetData;

import static com.example.handasy.fragments.MyProjects.activity;
import static com.example.handasy.fragments.MyProjects.projectList;

/**
 * Created by ahmed on 3/16/2017.
 */

public class ActivityShowProject extends FragmentActivity {
    TabLayout tabs;
    ViewPager viewPager;
    String id;
    CustomTextView title;
    private Drawer drawer;
    Dialog dialog;
    public static ProjectData data;

    @Override
    public void onBackPressed() {
        if (drawer.back()) {
            super.onBackPressed();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.show();
        drawer = new Drawer(this, "");
        data = new ProjectData();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getData();
        title = (CustomTextView) findViewById(R.id.title);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (TabLayout) findViewById(R.id.result_tabs);


    }

    private void getData() {
        new GetData(new GetData.AsyncResponse() {
            @Override
            public void processFinish(String outputs) {
                try {
                    JSONObject output = new JSONObject(outputs);

                    data.projectId = output.getString("ProjectId");
                    data.title = output.getString("ProjectTitle");
                    data.Lat = output.getString("LatPrj");
                    data.Lng = output.getString("LngPrj");
                    data.Zoom = output.getString("ZoomPrj");
                    data.GroundId = output.getString("GroundId");
                    data.PlanId = output.getString("PlanId");
                    data.Space = output.getString("Space");
                    data.SakNum = output.getString("SakNum");
                    data.DataSake = output.getString("DataSake");
                    data.LicenceNum = output.getString("LicenceNum");
                    data.DateLicence = output.getString("DateLicence");
                    data.Notes = output.getString("Notes");
                    if (output.getString("ProjectStatusID").equals("7"))
                        data.delete = "true";

                    data.logoUrl = output.getString("EmpImage");
                    data.mobile = output.getString("EmpMobile");
                    data.eng = output.getString("EmpName");
                    data.JobName = output.getString("JobName");

                    data.ProjectTypeId = output.getString("ProjectTypeId");
                    data.kind = output.getString("ProjectTypeName");
                    data.ProjectEngComment = output.getString("ProjectEngComment");

                    data.BranchID = output.getString("BranchID");
                    data.BranchName = output.getString("BranchName");
                    data.BranchZoom = output.getString("ZoomBranch");
                    data.BranchLat = output.getString("LatBranch");
                    data.BranchLng = output.getString("LngBranch");
                    data.stateColor = output.getString("ProjectStatusColor");
                    data.state = output.getString("ProjectStatusName");
                    projectImages(data.projectId);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, this, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new DataBase().WebService + "GetProjectByProjectId", "?projectId=" + id);
    }

    private void projectImages(String projectId) throws JSONException {

        new GetData(new GetData.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject output = jArray.getJSONObject(i);
                        if (output.getInt("ProjectsImageType") == 1)
                            data.SkImages.add(new Image_Data(Uri.parse(output.getString("ProjectsImagePath")), output.getString("ProjectsImageID"), Integer.parseInt(output.getString("ProjectsImageRotate"))));
                        else
                            data.BldyaImages.add(new Image_Data(Uri.parse(output.getString("ProjectsImagePath")), output.getString("ProjectsImageID"), Integer.parseInt(output.getString("ProjectsImageRotate"))));
                    }
                    setupViewPager(viewPager);
                    tabs.setupWithViewPager(viewPager);
                    TabLayout.Tab tab = tabs.getTabAt(3);
                    tab.select();
                    changeTabsFont();
                    title.setText(data.title);
                    dialog.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, activity, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new DataBase().WebService + "GetImageprojectByProjID", "?projectId=" + projectId);

    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ShowProject4(), "الحالة");
        adapter.addFragment(new ShowProject3(), "المهندس");
        adapter.addFragment(new ShowProject2(), "مرفقات");
        adapter.addFragment(new ShowProject1(), "البيانات");
        viewPager.setAdapter(adapter);


    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    Typeface face = Typeface.createFromAsset(getAssets(), "font.ttf");
                    ((TextView) tabViewChild).setTypeface(face);
                }
            }
        }
    }

    public void back(View view) {
        super.onBackPressed();
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
            projectList.clear();
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


    public void myProjects(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MyProjects fragment = new MyProjects();
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
