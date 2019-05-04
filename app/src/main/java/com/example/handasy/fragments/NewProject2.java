package com.example.handasy.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.handasy.model.Drawer;
import com.example.handasy.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.ActivityProjectAdd;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by ahmed on 3/26/2017.
 */
public class NewProject2 extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    double lat = -1, lang = -1, zoom = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_project2, container, false);
        design();
        Drawer.title.setText("اختيار موقع الأرض");
        Drawer.postionSelected = -1;
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

        displayMap();

        return rootView;
    }

    private void displayMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (getLastKnownLocation() != null) {

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLastKnownLocation().getLatitude(), getLastKnownLocation().getLongitude()), 13));

                    lat = getLastKnownLocation().getLatitude();
                    lang = getLastKnownLocation().getLongitude();
                    zoom = 17;
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(getLastKnownLocation().getLatitude(), getLastKnownLocation().getLongitude()))      // Sets the center of the map to location user
                            .zoom(17)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(getLastKnownLocation().getLatitude(), getLastKnownLocation().getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);

                } else googleMap.setMyLocationEnabled(true);


                // For dropping a marker at a point on the Map

                // CameraPosition cameraPosition = new CameraPosition.Builder().target(finalLocation).zoom(finalZoom).build();
                //                             googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // For zooming automatically to the location of the marker
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {

                        lat = point.latitude;
                        lang = point.longitude;
                        zoom = googleMap.getCameraPosition().zoom;
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(lat, lang, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String cityName = "";

                        try {
                            cityName = addresses.get(0).getAddressLine(0);
                        } catch (Exception e) {

                        }

                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(point).title(cityName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                });

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point) {

                        lat = point.latitude;
                        lang = point.longitude;
                        zoom = googleMap.getCameraPosition().zoom;
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(lat, lang, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String cityName = addresses.get(0).getAddressLine(0);

                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(point).title(cityName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                });
            }
        });

    }

    private void design() {
        ActivityProjectAdd.indicator.setImageResource(R.drawable.indicatorproject2);
        ActivityProjectAdd.right.setVisibility(View.VISIBLE);
        ActivityProjectAdd.right.setText("السابق");
        ActivityProjectAdd.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ActivityProjectAdd.left.setVisibility(View.VISIBLE);
        ActivityProjectAdd.left.setText("التالي");
        ActivityProjectAdd.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zoom == -1 || lang == -1 || lat == -1)
                    Toast.makeText(getContext(), "اضغط على الخريطه و اختر موقع الارض", Toast.LENGTH_SHORT).show();
                else {
                    ActivityProjectAdd.projectData.put("Zoom", zoom + "");
                    ActivityProjectAdd.projectData.put("Lat", lat + "");
                    ActivityProjectAdd.projectData.put("Lng", lang + "");

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_right, R.anim.translat_left,
                            R.anim.translat_left);
                    NewProject3 fragment = new NewProject3();
                    ft.replace(R.id.activity_main_content_fragment3, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            }
        });
        ActivityProjectAdd.middle.setVisibility(View.INVISIBLE);

    }


    private Location getLastKnownLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            Location l = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onStop() {
        DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.setMargins(0, (int) (50 * ActivityProjectAdd.d), 0, (int) (0 * ActivityProjectAdd.d));
        ActivityProjectAdd.frameLayout.setLayoutParams(params1);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.setMargins(0, (int) (50 * ActivityProjectAdd.d), 0, (int) (110 * ActivityProjectAdd.d));
        ActivityProjectAdd.frameLayout.setLayoutParams(params1);
    }
}