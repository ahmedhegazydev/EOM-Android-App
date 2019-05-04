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

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.ActivityLogin;
import com.example.ActivityProjectEdit;
import com.example.ActivityShowProject;
import com.example.handasy.model.DataBase;
import com.example.handasy.model.UrlData;
import com.example.handasy.view.CustomTextView;

/**
 * Created by ahmed on 3/26/2017.
 */
public class EditProject4 extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    Dialog dialog;
    ProjectData projectData;
    Dialog info;
    CustomButton locationMap;
    private String projectId = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_project2, container, false);
        dialog = new Dialog(getActivity());
        Drawer.title.setText("");
        projectData = new ProjectData();

        locationMap = (CustomButton) rootView.findViewById(R.id.maplocation);

        locationMap.setVisibility(View.INVISIBLE);
        CustomButton textButton = (CustomButton) rootView.findViewById(R.id.textButton);
        textButton.setText("تهانينا تم تعديل مشروعكم بنجاح");
        design();


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        Drawer.postionSelected = -1;
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateProjcet();
        return rootView;
    }

    private void displayMap(String zoom, String lng, String lat) {
        LatLng location = null;
        Float Zoom = null;
        final Double Lat = Double.parseDouble(lat);
        final Double Lng = Double.parseDouble(lng);
        if (zoom == null)
            zoom = "9";
        try {
            Zoom = Float.parseFloat(zoom);
        } catch (Exception e) {
            Zoom = Float.parseFloat("9");

        }
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
                try {
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
                } catch (Exception e) {
                    Toast.makeText(getContext(), "حدث خطا فى الخريطه , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void dialogInfo(final String id, String empImage, String empName, String jobName, final String mobile) {

        MyProjects.refresh(getActivity());

        info = new Dialog(getContext());
        info.setContentView(R.layout.dialog_new_project);
        final ImageView imageView = (ImageView) info.findViewById(R.id.logo);
        if (empImage.equals(""))
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                NewProject1.project_postion = -1;
                NewProject1.branch_postion = -1;
                NewProject1.current_data = 0;
                startActivity(new Intent(getActivity(), ActivityShowProject.class).putExtra("id", id));
                info.dismiss();
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

        info.setCancelable(false);
        try {
            info.show();
        } catch (Exception e) {

        }

        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.setContentView(R.layout.dialog_done);
        ImageView imageView1 = (ImageView) dialog1.findViewById(R.id.close);
        CustomTextView textView = (CustomTextView) dialog1.findViewById(R.id.description);
        textView.setText("لقد تم تعديل مشروعكم بنجاح");
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
        //   dialog1.show();
    }

    private void design() {
        ActivityProjectEdit.indicator.setVisibility(View.INVISIBLE);
        ActivityProjectEdit.right.setVisibility(View.INVISIBLE);
        ActivityProjectEdit.left.setVisibility(View.INVISIBLE);
        ActivityProjectEdit.middle.setVisibility(View.INVISIBLE);

    }

    private void updateProjcet() {

        UrlData urlData = new UrlData();
        urlData.add("CustmoerId=" + ActivityLogin.C_ID);
        urlData.add("UserId=" + ActivityLogin.userId);
        urlData.add("BranchId=" + ActivityProjectEdit.projectData.get("branchId"));
        urlData.add("ProjectTypeId=" + ActivityProjectEdit.projectData.get("ProjectTypeId"));
        urlData.add("GroundId=" + ActivityProjectEdit.projectData.get("kt3a_number"));
        urlData.add("PlanId=" + ActivityProjectEdit.projectData.get("mo5tt_number"));
        urlData.add("Space=" + ActivityProjectEdit.projectData.get("ground_space"));
        urlData.add("LicenceNum=" + ActivityProjectEdit.projectData.get("ro5sa_number"));
        urlData.add("SakNum=" + ActivityProjectEdit.projectData.get("sk_number"));
        urlData.add("DataSake=" + ActivityProjectEdit.projectData.get("sk_date"));
        urlData.add("DateLicence=" + ActivityProjectEdit.projectData.get("ro5sa_date"));
        urlData.add("Notes=" + ActivityProjectEdit.projectData.get("notes"));
        urlData.add("Lat=" + ActivityProjectEdit.projectData.get("Lat"));
        urlData.add("Lng=" + ActivityProjectEdit.projectData.get("Lng"));
        urlData.add("Zoom=" + ActivityProjectEdit.projectData.get("Zoom"));
        urlData.add("ProjectId=" + ActivityProjectEdit.projectID);
        try {
            new GetData(new GetData.AsyncResponse() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void processFinish(String output) {
                    JSONObject jObject = null;
                    try {
                        jObject = new JSONObject(output);
                        insertDeleteImages(jObject.getString("ProjectId"));
                        displayMap(jObject.getString("Zoom"), jObject.getString("BranchLng"), jObject.getString("BranchLat"));
                        dialogInfo(jObject.getString("ProjectId"), jObject.getString("EmpImage"), jObject.getString("EmpName"), jObject.getString("JobName"), jObject.getString("Mobile"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, getActivity(), true).execute(new DataBase().WebService + "ProjectsDataUpdate", urlData.get());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "حدث خطأ فى تسجيل المشروع , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
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
    private void insertDeleteImages(String projectId) {
        this.projectId = projectId;
        final String path = "http://i-tecgroup.com/Images/Project/";
        for (int i = 0; i < EditProject3.imageList.size(); i++) {
            UrlData urlData = new UrlData();
            urlData.add("projectId=" + projectId);
            urlData.add("projectsImagePath=" + path + EditProject3.imageList.get(i).name);
            urlData.add("projectsImageType=" + EditProject3.imageList.get(i).kind);
            urlData.add("ProjectsImageRotate="+ EditProject3.imageList.get(i).orintation);
            try {
                new GetData(new GetData.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        String jObject = (output);
                    }
                }, getActivity(), false).execute(new DataBase().WebService + "UploadImage", urlData.get());
            } catch (Exception e) {
                Toast.makeText(getContext(), "حدث خطأ فى تسجيل الصور , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        for (int i = 0; i < EditProject3.deleteImageList.size(); i++) {
            try {
                new GetData(new GetData.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        String jObject = (output);
                    }
                }, getActivity(), true).execute(new DataBase().WebService + "ImageDelete?", "projectsImageID=" + EditProject3.deleteImageList.get(i).Id);
            } catch (Exception e) {
                Toast.makeText(getContext(), "حدث خطأ فى تسجيل الصور, برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onStop() {
        DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.setMargins(0, (int) (50 * ActivityProjectEdit.d), 0, (int) (0 * ActivityProjectEdit.d));
        ActivityProjectEdit.frameLayout.setLayoutParams(params1);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.setMargins(0, (int) (50 * ActivityProjectEdit.d), 0, (int) (110 * ActivityProjectEdit.d));
        ActivityProjectEdit.frameLayout.setLayoutParams(params1);
    }
}