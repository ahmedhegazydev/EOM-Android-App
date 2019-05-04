package com.example.handasy.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handasy.model.Drawer;
import com.example.handasy.controller.GetData;
import com.example.handasy.R;
import com.example.handasy.view.CustomButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.ActivityLogin;
import com.example.ActivityMain;
import com.example.ActivityProjectAdd;
import com.example.ActivityProjectEdit;
import com.example.ActivityShowProject;
import com.example.handasy.model.DataBase;
import com.example.handasy.model.Image_Data;
import com.example.handasy.model.ProjectData;


/**
 * Created by ahmed on 3/30/2017.
 */

public class MyProjects extends Fragment {
    public static List<ProjectData> projectList;
    RelativeLayout projectBackground;
    RecyclerView listView;
    boolean optionBool = true;
    LinearLayout bar;
    static Dialog dialog_projects;
    static ProjectAdapter listViewAdapter;
    CustomButton delete, edit, details;
    int projectPostion = 0;
    public static HashMap<String, Integer> projectIdPostion;
    public static Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void getData(final Activity activity) {

        try {
            projectList.clear();
        } catch (Exception e) {

        }

        try {
            projectIdPostion.clear();
            new GetData(new GetData.AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    try {
                        JSONArray jArray = new JSONArray(result);
                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject output = jArray.getJSONObject(i);
                            ProjectData data = new ProjectData();
                            projectIdPostion.put(output.getString("ProjectId"), i);
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
                            projectImages(data.projectId, data, activity);
                        }
                        if (dialog_projects.isShowing())
                            dialog_projects.cancel();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, activity, false).execute(new DataBase().WebService + "GetProjectByCustID", "?custmoerId=" + ActivityLogin.C_ID);
        } catch (Exception e) {
            //Toast.makeText(getActivity(), "حدث خطأ , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void projectImages(String projectId, final ProjectData data, Activity activity) throws JSONException {

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
                    projectList.add(data);
                    listViewAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, activity, false).execute(new DataBase().WebService + "GetImageprojectByProjID", "?projectId=" + projectId);

    }


    @Override
    public void onStop() {
        ActivityMain.option.setVisibility(View.INVISIBLE);
        super.onStop();
    }

    @Override
    public void onResume() {
        ActivityMain.option.setVisibility(View.VISIBLE);
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_projects, container, false);
        dialog_projects = new Dialog(getActivity());
        dialog_projects.setContentView(R.layout.dialog_loading);
        dialog_projects.setCancelable(false);
        dialog_projects.show();

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        activity = getActivity();
        projectList = new ArrayList<>();
        projectIdPostion = new HashMap<>();
        getData(getActivity());
        projectPostion = -1;
        Drawer.title.setText("طلبات مشاريعي");
        Drawer.postionSelected = -1;
        Drawer.drawerAdapter.notifyDataSetChanged();

        design(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void design(View view) {

        bar = (LinearLayout) view.findViewById(R.id.bar);
        bar.setVisibility(View.INVISIBLE);


        edit = (CustomButton) view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectList.get(projectPostion).delete.equals("true"))
                    Toast.makeText(getContext(), "لا يمكنك التعديل على مشروع ملغى", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(getActivity(), ActivityProjectEdit.class).putExtra("postion", projectPostion));
            }
        });
        details = (CustomButton) view.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityShowProject.class).putExtra("id", projectList.get(projectPostion).projectId));
            }
        });


        projectBackground = (RelativeLayout) view.findViewById(R.id.background_add_projects);
        projectBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityProjectAdd.class));
            }
        });
        ActivityMain.title.setText("طلبات مشاريعي");
        ActivityMain.title.setVisibility(View.VISIBLE);
        ActivityMain.option.setVisibility(View.VISIBLE);
        ActivityMain.option.setImageResource(R.drawable.add_project);
        ActivityMain.option.setColorFilter(ContextCompat.getColor(getContext(), R.color.darkYellow));
        ActivityMain.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionBool) {
                    startActivity(new Intent(getActivity(), ActivityProjectAdd.class));
                } else {
                    optionBool = true;
                    bar.setVisibility(View.INVISIBLE);
                    ActivityMain.option.setImageResource(R.drawable.add_project);
                    try {
                        listView.findViewHolderForAdapterPosition(projectPostion).itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                    } catch (Exception e) {
                    }
                }
            }
        });
        listView = (RecyclerView) view.findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        listViewAdapter = new ProjectAdapter(projectList);
        listView.setAdapter(listViewAdapter);

        delete = (CustomButton) view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectList.get(projectPostion).delete.equals("true"))
                    Toast.makeText(getContext(), "مشروع ملغي ... غير مسموح بالتعديل", Toast.LENGTH_SHORT).show();
                else {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_confirmation);
                    CustomButton yes = (CustomButton) dialog.findViewById(R.id.yes);
                    CustomButton no = (CustomButton) dialog.findViewById(R.id.no);
                    dialog.show();
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new GetData(new GetData.AsyncResponse() {
                                    @Override
                                    public void processFinish(String jObject) {
                                        if (jObject.contains("Done")) {
                                            projectList.get(projectPostion).delete = "true";
                                            projectList.get(projectPostion).state = "ملغي";
                                            projectList.get(projectPostion).stateColor = "#e74c3c";
                                            listViewAdapter.notifyItemChanged(projectPostion);
                                        } else {
                                            Toast.makeText(getActivity(), "حدث خطأ فى الغاء المشروع , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.cancel();

                                    }
                                }, getActivity(), true).execute(new DataBase().WebService + "ProjectCancel?", "projectId=" + projectList.get(projectPostion).projectId);
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "حدث خطأ فى الغاء المشروع , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                }
            }
        });

    }

    public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {

        private List<ProjectData> projectList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView logo;
            public TextView title, state, eng, mobile, kind;
            public RelativeLayout background;

            public MyViewHolder(View view) {
                super(view);
                logo = (ImageView) view.findViewById(R.id.logo);
                title = (TextView) view.findViewById(R.id.address);
                state = (TextView) view.findViewById(R.id.state);
                kind = (TextView) view.findViewById(R.id.kind);
                mobile = (TextView) view.findViewById(R.id.mob);
                eng = (TextView) view.findViewById(R.id.eng);
                background = (RelativeLayout) view.findViewById(R.id.background);

            }
        }


        public ProjectAdapter(List<ProjectData> projectList) {
            this.projectList = projectList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if (projectPostion != position)
                holder.background.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            holder.background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (projectPostion != -1) {
                        try {
                            listView.findViewHolderForAdapterPosition(projectPostion).itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        } catch (Exception e) {
                            listViewAdapter.notifyItemChanged(projectPostion);
                            e.printStackTrace();
                        }
                    }
                    optionBool = false;
                    projectPostion = position;
                    bar.setVisibility(View.VISIBLE);
                    listView.findViewHolderForAdapterPosition(projectPostion).itemView.setBackgroundResource(R.drawable.boarder);
                    ActivityMain.option.setImageResource(R.drawable.stop);
                }
            });

            try {
                Picasso.with(getContext()).load(projectList.get(position).logoUrl).into(holder.logo, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        holder.logo.setImageResource(R.drawable.logo);
                    }
                });
            } catch (Exception e) {
                holder.logo.setImageResource(R.drawable.logo);
            }

            Drawable circel = ContextCompat.getDrawable(getContext(), R.drawable.circle2);
            circel.setColorFilter(Color.parseColor(projectList.get(position).stateColor), PorterDuff.Mode.MULTIPLY);
            holder.title.setText(projectList.get(position).title);
            holder.state.setCompoundDrawablesWithIntrinsicBounds(null, null, circel, null);
            holder.state.setText("  " + projectList.get(position).state + "  ");
            holder.kind.setText(projectList.get(position).kind);
            holder.mobile.setText("  " + projectList.get(position).mobile + "  ");
            holder.mobile.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.call_text), null);
            holder.eng.setText(projectList.get(position).eng);
        }

        @Override
        public int getItemCount() {
            if (projectList.size() == 0)
                projectBackground.setVisibility(View.VISIBLE);
            else
                projectBackground.setVisibility(View.INVISIBLE);

            return projectList.size();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static void refresh(Activity activity) {
        getData(activity);
        try {
            listViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }
}
