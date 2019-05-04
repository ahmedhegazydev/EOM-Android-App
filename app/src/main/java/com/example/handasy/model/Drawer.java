package com.example.handasy.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ActivityLogin;
import com.example.handasy.fragments.About;
import com.example.handasy.fragments.ContactMain;

import com.example.handasy.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.example.handasy.fragments.Gallery;
import com.example.handasy.fragments.HomeSkip;
import com.example.handasy.fragments.MyProjects;
import com.example.handasy.view.CustomTextView;
import com.example.handasy.fragments.HomeLogin;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ahmed on 4/4/2017.
 */

public class Drawer {
    FragmentActivity activity;
    public static CustomAdapter drawerAdapter;
    ImageView menu, twiter, facebook, google, logo;
    CustomTextView name, mobile, logout;
    RelativeLayout drawer_page;
    String kind = "";
    DrawerLayout drawerLayout;
    public static int postionSelected = 0;
    public static TextView title;
    ListView listView;

    public void invisible() {
        logo.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        mobile.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (drawerLayout.isDrawerOpen(drawer_page))
                    drawerLayout.closeDrawer(drawer_page);
                if (position != postionSelected)
                    click(position);
                postionSelected = position;
                drawerAdapter.notifyDataSetChanged();
            }
        });

    }

    public Drawer(final FragmentActivity activit, String kind) {
        this.kind = kind;
        this.activity = activit;
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawerLayout);
        logo = (ImageView) activity.findViewById(R.id.logo);
        title = (TextView) activit.findViewById(R.id.title);
        title.setText("البراهيم للاستشارات الهندسية");
        drawer_page = (RelativeLayout) activity.findViewById(R.id.drawer);
        drawer_page.setBackgroundColor(Color.parseColor("#2c2c2c"));
        name = (CustomTextView) activity.findViewById(R.id.name);
        name.setText(ActivityLogin.name);
        mobile = (CustomTextView) activity.findViewById(R.id.mobile);
        mobile.setText(ActivityLogin.mobile);
        logout = (CustomTextView) activity.findViewById(R.id.log);
        twiter = (ImageView) activity.findViewById(R.id.twiter);
        google = (ImageView) activity.findViewById(R.id.googlePlus);
        facebook = (ImageView) activity.findViewById(R.id.facebook);
        listView = (ListView) activity.findViewById(R.id.list_item);
        menu = (ImageView) activity.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(drawer_page))
                    drawerLayout.closeDrawer(drawer_page);
                else
                    drawerLayout.openDrawer(drawer_page);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPrefs = activity.getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.clear();
                prefsEditor.commit();
                MyProjects.projectList.clear();
                ActivityLogin.C_ID = "";
                ActivityLogin.name = "";
                ActivityLogin.mobile = "";

                activity.startActivity(new Intent(activity, ActivityLogin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });


        try {
            Picasso.with(activity).load(ActivityLogin.logo).into(logo, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    logo.setImageResource(R.drawable.logo);
                }
            });
        } catch (Exception e) {

        }

        ArrayList<DataDrawer> arrayList = new ArrayList<>();

        DataDrawer home = new DataDrawer();
        home.logo = R.drawable.drawerhome;
        home.title = "الصفحة الرئيسيه";
        arrayList.add(home);

        DataDrawer call = new DataDrawer();
        call.logo = R.drawable.drawercall;
        call.title = "اتصل بنا";
        arrayList.add(call);

        DataDrawer team = new DataDrawer();
        team.logo = R.drawable.drawerteam;
        team.title = "فريق العمل";
        arrayList.add(team);

        DataDrawer work = new DataDrawer();
        work.logo = R.drawable.drawerprojects;
        work.title = "مشاريعنا";
        arrayList.add(work);

        DataDrawer about = new DataDrawer();
        about.logo = R.drawable.drawerabout;
        about.title = "عن المكتب";
        arrayList.add(about);

        DataDrawer help = new DataDrawer();
        help.logo = R.drawable.drawerhelp;
        help.title = "مساعدة";
        arrayList.add(help);

        drawerAdapter = new CustomAdapter(arrayList, activit);
        listView.setAdapter(drawerAdapter);
        listView.setDividerHeight(0);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (drawerLayout.isDrawerOpen(drawer_page))
                    drawerLayout.closeDrawer(drawer_page);
                if (position != postionSelected)
                    click(position);
                postionSelected = position;
                drawerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void click(int position) {
        if (position == 0) {

            if (kind.equals("")) {
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                HomeLogin fragment = new HomeLogin();
                ft.addToBackStack(null);
                ft.replace(R.id.activity_main_content_fragment3, fragment);
                ft.commit();
            } else {
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                HomeSkip fragment = new HomeSkip();
                ft.addToBackStack(null);
                ft.replace(R.id.activity_main_content_fragment3, fragment);
                ft.commit();
            }

        }
        if (position == 1) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ContactMain fragment = new ContactMain();
            ft.replace(R.id.activity_main_content_fragment3, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (position == 2) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Gallery fragment = new Gallery();
            Bundle bundle = new Bundle();
            bundle.putString("kind", "team");
            fragment.setArguments(bundle);
            ft.replace(R.id.activity_main_content_fragment3, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (position == 3) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Gallery fragment = new Gallery();
            Bundle bundle = new Bundle();
            bundle.putString("kind", "project");
            fragment.setArguments(bundle);
            ft.replace(R.id.activity_main_content_fragment3, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (position == 4) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            About fragment = new About();
            ft.replace(R.id.activity_main_content_fragment3, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    public boolean back() {

        try {
            if (drawerLayout.isDrawerOpen(drawer_page)) {

                drawerLayout.closeDrawer(drawer_page);
                return false;
            } else
                return true;
        } catch (Exception e) {
        }
        return true;
    }


    public class CustomAdapter extends ArrayAdapter<DataDrawer> {

        private ArrayList<DataDrawer> dataSet;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            CustomTextView txtName;
            ImageView logo;
            View yellowWall = null;
        }

        public CustomAdapter(ArrayList<DataDrawer> data, Context context) {
            super(context, R.layout.drawer_row, data);
            this.dataSet = data;
            this.mContext = context;

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            DataDrawer dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag


            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.drawer_row, parent, false);
                viewHolder.txtName = (CustomTextView) convertView.findViewById(R.id.text);
                viewHolder.logo = (ImageView) convertView.findViewById(R.id.logo);
                viewHolder.yellowWall = (View) convertView.findViewById(R.id.yellowWall);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.txtName.setText(dataModel.title);
            viewHolder.logo.setImageResource(dataModel.logo);

            if (position == postionSelected) {
                viewHolder.txtName.setTextColor(Color.parseColor("#d4af36"));
                viewHolder.logo.setImageTintList(ColorStateList.valueOf(Color.parseColor("#d4af36")));
                convertView.setBackgroundColor(Color.parseColor("#2f2e2c"));
                viewHolder.yellowWall.setVisibility(View.VISIBLE);
            } else {
                viewHolder.txtName.setTextColor(Color.parseColor("#7f7f7f"));
                viewHolder.logo.setImageTintList(ColorStateList.valueOf(Color.parseColor("#7f7f7f")));
                convertView.setBackgroundColor(Color.parseColor("#2c2c2c"));
                viewHolder.yellowWall.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    private class DataDrawer {
        int logo;
        String title;
    }
}
