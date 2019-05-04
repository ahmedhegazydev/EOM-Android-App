package com.example.handasy.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import com.example.ActivityShowProject;
import com.example.handasy.view.CustomTextView;

/**
 * Created by ahmed on 3/31/2017.
 */

public class ShowProject3 extends Fragment {
    ProjectData data;
    CustomButton locationMap;
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_project3, container, false);
        data = ActivityShowProject.data;
        final ImageView imageView = (ImageView) view.findViewById(R.id.logo);
        imageView.setImageResource(R.drawable.logo);
        locationMap = (CustomButton) view.findViewById(R.id.maplocation);
        // Drawer.title.setText("");
        Drawer.postionSelected = -1;

        final CustomTextView title = (CustomTextView) view.findViewById(R.id.title);
        CustomTextView name = (CustomTextView) view.findViewById(R.id.name);
        final CustomTextView call = (CustomTextView) view.findViewById(R.id.call);
        CustomButton callButton = (CustomButton) view.findViewById(R.id.callButton);

        try {
        Picasso.with(getContext()).load(data.logoUrl).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                imageView.setImageResource(R.drawable.logo);
            }
        });
        }
        catch (Exception e)
        {

        }
        name.setText(data.eng);
        call.setText(data.mobile);
        title.setText(data.JobName);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + data.mobile));
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                getActivity().startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + data.mobile));
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                getActivity().startActivity(intent);
            }
        });


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
        final Double Lat = Double.parseDouble(data.BranchLat),
                Lng = Double.parseDouble(data.BranchLng);
        Zoom = Float.parseFloat(data.BranchZoom);
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
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                locationMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Lat, Lng, "The Location");
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    }
                });
                //    googleMap.addMarker(new MarkerOptions().title("").position(finalLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
            }
        });
    }


}

