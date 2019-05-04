package com.example.handasy.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.handasy.model.Drawer;
import com.example.handasy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 4/16/2017.
 */

public class Gallery extends Fragment {
    RecyclerView gallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery, container, false);
        gallery = (RecyclerView) view.findViewById(R.id.RecyclerView);
        gallery.setHasFixedSize(true);
        gallery.setItemViewCacheSize(20);
        gallery.setDrawingCacheEnabled(true);
        gallery.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        if(getArguments().getString("kind").equals("project")) {

            Drawer.title.setText("مشاريعنا");
            Drawer.postionSelected = 3;
            Drawer.drawerAdapter.notifyDataSetChanged();
            gallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
            List<Uri> uriList = new ArrayList<>();
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project1"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project2"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project3"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project4"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project5"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project6"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project7"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project1"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project2"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project3"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project4"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project5"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project6"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project7"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project1"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project2"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project3"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project4"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project5"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project6"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/project7"));
            gallery.setAdapter(new ImagesAdapter(uriList));
        }
else
        if(getArguments().getString("kind").equals("team")) {

            Drawer.title.setText("فريق العمل");
            Drawer.postionSelected = 2;
            Drawer.drawerAdapter.notifyDataSetChanged();
            gallery.setLayoutManager(new GridLayoutManager(getContext(), 2));
            List<Uri> uriList = new ArrayList<>();
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team1"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team2"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team3"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team4"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team5"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team6"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team1"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team2"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team3"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team4"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team5"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team6"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team1"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team2"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team3"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team4"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team5"));
            uriList.add(Uri.parse("android.resource://com.example.handasy/drawable/team6"));
            gallery.setAdapter(new ImagesAdapter(uriList));
        }

        return view;
    }

    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

        private List<Uri> imagesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView image;

            public MyViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.imageView);
                image.setPadding(10, 10, 10, 10);
            }
        }


        public ImagesAdapter(List<Uri> imagesList) {
            this.imagesList = imagesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imageview, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {


            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("imageUri", imagesList.get(position).toString());
                    Imageview fragment = new Imageview();
                    fragment.setArguments(bundle);
                    ft.replace(R.id.activity_main_content_fragment3, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
            Picasso.with(getContext()).load(imagesList.get(position)).into(holder.image);

        }

        @Override
        public int getItemCount() {
            return imagesList.size();
        }
    }

}
