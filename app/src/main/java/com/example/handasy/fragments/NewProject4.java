package com.example.handasy.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.handasy.model.Drawer;
import com.example.handasy.controller.GetData;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.ActivityLogin;
import com.example.ActivityProjectAdd;
import com.example.ActivityShowProject;
import com.example.handasy.model.DataBase;
import com.example.handasy.model.UrlData;
import com.example.handasy.view.CustomTextView;

/**
 * Created by ahmed on 3/26/2017.
 */
public class NewProject4 extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    Dialog dialog;
    ProjectData projectData;
    Dialog info;
    CustomButton locationMap;
    private String projectId = "";
    private boolean enter=true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_project2, container, false);
        dialog = new Dialog(getActivity());
        Drawer.title.setText("");
        Drawer.postionSelected = -1;
        projectData = new ProjectData();
        locationMap = (CustomButton) rootView.findViewById(R.id.maplocation);
        locationMap.setVisibility(View.INVISIBLE);
        CustomButton textButton = (CustomButton) rootView.findViewById(R.id.textButton);
        textButton.setText("موقع المكتب");
        design();

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(enter) {
            enter=false;
            insertProject();
        }
        return rootView;
    }

    private void displayMap(String zoom, String lng, String lat) {
        LatLng location = null;
        Float Zoom = null;
        final Double Lat = Double.parseDouble(lat);
        final Double Lng = Double.parseDouble(lng);
        Zoom = Float.parseFloat(zoom);
        location = new LatLng(Lat, Lng);

        final LatLng finalLocation = location;
        final Float finalZoom = Zoom;
        final LatLng finalLocation1 = location;
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                    return;
                }

                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map

                CameraPosition cameraPosition = new CameraPosition.Builder().target(finalLocation).zoom(finalZoom).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(Lat, Lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getAddressLine(0);
                googleMap.addMarker(new MarkerOptions().position(finalLocation1).title(cityName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
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

    private void dialogInfo(final String projectId, String empImage, String empName, String jobName, final String mobile) {


        info = new Dialog(getContext());
        info.setContentView(R.layout.dialog_new_project);
        info.setCancelable(false);

        final ImageView imageView = (ImageView) info.findViewById(R.id.logo);
        if(empImage.equals(""))
            imageView.setImageResource(R.drawable.logo);
        else
        Picasso.with(getContext()).load(empImage).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                imageView.setImageResource(R.drawable.logo);
            }
        });
        CustomTextView title = (CustomTextView) info.findViewById(R.id.title);
        title.setText(jobName);
        CustomTextView name = (CustomTextView) info.findViewById(R.id.name);
        final CustomTextView call = (CustomTextView) info.findViewById(R.id.call);
        CustomButton show = (CustomButton) info.findViewById(R.id.show);
        name.setText(empName);
        call.setText(mobile);
        final CustomButton callButton = (CustomButton) info.findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + mobile));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                getActivity().startActivity(intent);
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewProject1.project_postion = -1;
                NewProject1.branch_postion = -1;
                NewProject1.current_data = 0;
                info.dismiss();
                startActivity(new Intent(getActivity(), ActivityShowProject.class).putExtra("id", projectId));
                getActivity().finish();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + mobile));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                getActivity().startActivity(intent);
            }
        });

        info.show();
    }

    private void design() {
        ActivityProjectAdd.indicator.setVisibility(View.INVISIBLE);
        ActivityProjectAdd.right.setVisibility(View.INVISIBLE);
        ActivityProjectAdd.left.setVisibility(View.INVISIBLE);
        ActivityProjectAdd.middle.setVisibility(View.INVISIBLE);

    }

    private void insertProject() {
        UrlData urlData = new UrlData();
        urlData.add("CustmoerId=" + ActivityLogin.C_ID);
        urlData.add("UserId=" + ActivityLogin.userId);
        urlData.add("BranchId=" + ActivityProjectAdd.projectData.get("branchId"));
        urlData.add("ProjectTypeId=" + ActivityProjectAdd.projectData.get("ProjectTypeId"));
        urlData.add("GroundId=" + ActivityProjectAdd.projectData.get("kt3a_number"));
        urlData.add("PlanId=" + ActivityProjectAdd.projectData.get("mo5tt_number"));
        urlData.add("Space=" + ActivityProjectAdd.projectData.get("ground_space"));
        urlData.add("LicenceNum=" + ActivityProjectAdd.projectData.get("ro5sa_number"));
        urlData.add("SakNum=" + ActivityProjectAdd.projectData.get("sk_number"));
        urlData.add("DataSake=" + ActivityProjectAdd.projectData.get("sk_date"));
        urlData.add("DateLicence=" + ActivityProjectAdd.projectData.get("ro5sa_date"));
        urlData.add("Notes=" + ActivityProjectAdd.projectData.get("notes"));
        urlData.add("Lat=" + ActivityProjectAdd.projectData.get("Lat"));
        urlData.add("Lng=" + ActivityProjectAdd.projectData.get("Lng"));
        urlData.add("Zoom=" + ActivityProjectAdd.projectData.get("Zoom"));
        try {
            new GetData(new GetData.AsyncResponse() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void processFinish(String result) {
                    try {
                        JSONObject jObject = new JSONObject(result);
                        insertImages(jObject.getString("ProjectId"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            MyProjects.refresh(getActivity());
                        }
                        displayMap(jObject.getString("Zoom"), jObject.getString("BranchLng"), jObject.getString("BranchLat"));
                        dialogInfo(jObject.getString("ProjectId"),jObject.getString("EmpImage"), jObject.getString("EmpName"), jObject.getString("JobName"), jObject.getString("Mobile"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, getActivity(),true).execute(new DataBase().WebService + "ProjectsDataSave", urlData.get());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "حدث خطأ فى حفظ المشروع, برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void insertImages(String projectId) {
        this.projectId = projectId;
        final String path = "http://i-tecgroup.com/Images/Project/";
        for (int i = 0; i < NewProject3.imageList.size(); i++) {
            UrlData urlData = new UrlData();
            urlData.add("projectId=" + projectId);
            urlData.add("projectsImagePath=" + path + NewProject3.imageList.get(i).name);
            urlData.add("projectsImageType=" + NewProject3.imageList.get(i).kind);
            urlData.add("ProjectsImageRotate=" + NewProject3.imageList.get(i).orintation);
            try {
                new GetData(new GetData.AsyncResponse() {
                    @Override
                    public void processFinish(String jObject) {

                    }
                }, getActivity(),false).execute(new DataBase().WebService + "UploadImage", urlData.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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