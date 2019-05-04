package com.example.handasy.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.handasy.model.Drawer;
import com.example.handasy.R;
import com.example.handasy.fragments.ContactData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 4/13/2017.
 */

public class ContactMain extends Fragment {
    TabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_main, container, false);

        Drawer.title.setText("اتصل بنا");
        Drawer.postionSelected = 1;
        Drawer.drawerAdapter.notifyDataSetChanged();

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        TabLayout.Tab tab = tabs.getTabAt(1);
        tab.select();
        changeTabsFont();


        return view;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        Bundle Bktef = new Bundle();
        Bktef.putString("mobile", "9668341750");
        Bktef.putString("branchName", "فرع القطيف");
        Bktef.putString("fax", "007385303");
        Bktef.putString("email", "info@l-tecgroub.com");
        Bktef.putString("address", "القطيف");
        Bktef.putString("facebook", "https://m.facebook.com/nabil.itec");
        Bktef.putString("Zoom", "9");
        Bktef.putString("Lat", "49.978010415507015");
        Bktef.putString("Lng", "26.390701013346458");


        Bundle Bdmam = new Bundle();
        Bdmam.putString("mobile", "9668341750");
        Bdmam.putString("branchName", "فرع الدمام");
        Bdmam.putString("fax", "007385303");
        Bdmam.putString("email", "info@l-tecgroub.com");
        Bdmam.putString("address", "الدمام");
        Bdmam.putString("facebook", "https://m.facebook.com/nabil.itec");
        Bdmam.putString("Zoom", "9");
        Bdmam.putString("Lat", "49.978010415507015");
        Bdmam.putString("Lng", "26.390701013346458");


        ContactData dmam = new ContactData();
        dmam.setArguments(Bdmam);

        ContactData ktef = new ContactData();
        ktef.setArguments(Bktef);
        Adapter adapter = new Adapter(this);
        adapter.addFragment(ktef, "فرع القطيف");
        adapter.addFragment(dmam, "فرع الدمام");
        viewPager.setAdapter(adapter);

    }

    class Adapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(Fragment manager) {
            super(manager.getChildFragmentManager());
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
                    Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "font.ttf");
                    ((TextView) tabViewChild).setTypeface(face);
                }
            }
        }
    }


}
