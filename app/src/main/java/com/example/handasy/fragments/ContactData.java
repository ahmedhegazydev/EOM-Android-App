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

import java.util.Locale;

import com.example.handasy.view.CustomTextView;

/**
 * Created by ahmed on 4/13/2017.
 */

public class ContactData extends Fragment {
    MapView mMapView;
    CustomButton locationMap;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_data, container, false);
        final CustomTextView mobile = (CustomTextView) view.findViewById(R.id.mobile);
        CustomTextView fax = (CustomTextView) view.findViewById(R.id.fax);
        CustomTextView address = (CustomTextView) view.findViewById(R.id.address);
        CustomTextView email = (CustomTextView) view.findViewById(R.id.email);
        CustomTextView facebook = (CustomTextView) view.findViewById(R.id.facebook_);
        CustomTextView branchName = (CustomTextView) view.findViewById(R.id.branchName);
        final Bundle bundle = getArguments();

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + bundle.getString("mobile")));
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                        return;
                    }
                    getActivity().startActivity(intent);
                }

        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",bundle.getString("email"), null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bundle.getString("facebook")));
                startActivity(browserIntent);
            }
        });

        locationMap = (CustomButton) view.findViewById(R.id.maplocation);

        mobile.setText(bundle.getString("mobile"));
        fax.setText(bundle.getString("fax"));
        address.setText(bundle.getString("address"));
        branchName.setText(bundle.getString("branchName"));
        email.setText(bundle.getString("email"));
        facebook.setText(bundle.getString("facebook"));

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        LatLng location = null;
        Float Zoom = null;
        final Double Lat = Double.parseDouble(bundle.getString("Lat")),
                Lng = Double.parseDouble(bundle.getString("Lng"));
        Zoom = Float.parseFloat(bundle.getString("Zoom"));
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

        return view;
    }

}
