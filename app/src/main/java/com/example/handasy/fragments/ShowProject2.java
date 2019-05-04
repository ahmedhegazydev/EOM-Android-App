package com.example.handasy.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.handasy.model.Drawer;
import com.example.handasy.model.ProjectData;
import com.example.handasy.R;
import com.example.handasy.view.CustomButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import com.example.ActivityShowProject;
import com.example.handasy.model.Image_Data;

/**
 * Created by ahmed on 3/30/2017.
 */

public class ShowProject2 extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    RecyclerView skImages, bldyaImages;
    ProjectData data;
    CustomButton locationMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_project2, container, false);
        data = ActivityShowProject.data;
        skImages = (RecyclerView) view.findViewById(R.id.RecyclerSk);
        bldyaImages = (RecyclerView) view.findViewById(R.id.RecyclerBldya);
        locationMap = (CustomButton) view.findViewById(R.id.textButton);
        //   Drawer.title.setText("");
        Drawer.postionSelected = -1;
        skImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        bldyaImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        skImages.setAdapter(new ImagesAdapter(data.SkImages));
        bldyaImages.setAdapter(new ImagesAdapter(data.BldyaImages));

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mapView();
        return view;
    }

    private void mapView() {

        LatLng location = null;
        Float Zoom = null;
        final Double Lat = Double.parseDouble(data.Lat),
                Lng = Double.parseDouble(data.Lng);
        Zoom = Float.parseFloat(data.Zoom);
        location = new LatLng(Lat, Lng);
        final CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(Zoom).build();

        final LatLng finalLocation = location;
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.addMarker(new MarkerOptions().position(finalLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                locationMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Lat, Lng, "The Location");
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    }
                });
            }
        });
    }


    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

        private List<Image_Data> imagesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView delete, image;

            public MyViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.image);
                delete = (ImageView) view.findViewById(R.id.delete);
            }
        }


        public ImagesAdapter(List<Image_Data> imagesList) {
            this.imagesList = imagesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.new_image_row, parent, false);

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
                    bundle.putString("imageUri", imagesList.get(position).path.toString());
                    Imageview fragment = new Imageview();
                    fragment.setArguments(bundle);
                    ft.add(R.id.activity_main_content_fragment3, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
            holder.delete.setVisibility(View.INVISIBLE);
            Log.e("imageUrl:", imagesList.get(position).path.toString());

            switch (imagesList.get(position).orintation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    Picasso.with(getContext()).load(imagesList.get(position).path.toString().replaceAll(" ", "%20")).fit().rotate(90).into(holder.image);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    Picasso.with(getContext()).load(imagesList.get(position).path.toString().replaceAll(" ", "%20")).fit().rotate(180).into(holder.image);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    Picasso.with(getContext()).load(imagesList.get(position).path.toString().replaceAll(" ", "%20")).fit().rotate(270).into(holder.image);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    Picasso.with(getContext()).load(imagesList.get(position).path.toString().replaceAll(" ", "%20")).fit().rotate(0).into(holder.image);

                default:
                    Picasso.with(getContext()).load(imagesList.get(position).path.toString().replaceAll(" ", "%20")).fit().rotate(0).into(holder.image);

                    break;

            }

        }

        @Override
        public int getItemCount() {
            return imagesList.size();
        }
    }


    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}

