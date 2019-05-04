package com.example.handasy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handasy.model.Drawer;
import com.example.handasy.model.ProjectData;
import com.example.handasy.R;

import com.example.ActivityShowProject;
import com.example.handasy.view.CustomTextView;

/**
 * Created by ahmed on 3/30/2017.
 */

public class ShowProject1 extends Fragment {
    CustomTextView ro5sa_number_data, project_kind_data, mo5tt_number_data, title, sk_number_date_data, branch_data, kt3a_number, sk_number_data, space_data, ro5sa_date_data, notes_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_project1, container, false);
        //Drawer.title.setText("");
        Drawer.postionSelected = -1;

        ProjectData data = ActivityShowProject.data;
        project_kind_data = (CustomTextView) view.findViewById(R.id.project_kind_data);
        ro5sa_number_data = (CustomTextView) view.findViewById(R.id.ro5sa_number_data);
        title = (CustomTextView) view.findViewById(R.id.title);
        mo5tt_number_data = (CustomTextView) view.findViewById(R.id.mo5tt_number_data);
        sk_number_date_data = (CustomTextView) view.findViewById(R.id.sk_number_date_data);
        branch_data = (CustomTextView) view.findViewById(R.id.branch_data);
        sk_number_data = (CustomTextView) view.findViewById(R.id.sk_number_data);
        kt3a_number = (CustomTextView) view.findViewById(R.id.kt3a_number);
        space_data = (CustomTextView) view.findViewById(R.id.space_data);
        ro5sa_date_data = (CustomTextView) view.findViewById(R.id.ro5sa_date_data);
        notes_data = (CustomTextView) view.findViewById(R.id.notes_data);

        title.setText(data.title);
        project_kind_data.setText(data.kind);
        mo5tt_number_data.setText(data.PlanId);
        ro5sa_number_data.setText(data.LicenceNum);
        sk_number_date_data.setText(data.DataSake);
        branch_data.setText(data.BranchName);
        sk_number_data.setText(data.SakNum);
        space_data.setText(data.Space);
        ro5sa_date_data.setText(data.DateLicence);
        notes_data.setText(data.Notes);
        kt3a_number.setText(data.GroundId);

        return view;
    }
}
