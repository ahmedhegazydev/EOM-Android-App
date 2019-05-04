package com.example.handasy.fragments;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.handasy.model.Drawer;
import com.example.handasy.controller.GetData;
import com.example.handasy.model.InputText;
import com.example.handasy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.ActivityProjectEdit;
import com.example.handasy.model.DataBase;

import static com.example.handasy.R.id.inputData;

/**
 * Created by ahmed on 3/26/2017.
 */

public class EditProject1 extends Fragment {
    Spinner project, branch;
    LinearLayout input;
    static int project_postion = -1, branch_postion = -1, current_data = 0;
    private InputText sk_number, mo5tt_number, kt3a_number, ro5sa_number, ground_space, ro5sa_date, sk_date, notes;
    HashMap<String, String> project_id;
    HashMap<String, String> branch_id;
    Calendar myCalendar = Calendar.getInstance();


    DatePickerDialog.OnDateSetListener date_ro5sa = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(ro5sa_date.inputText);
        }

    };

    DatePickerDialog.OnDateSetListener date_sk = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(sk_date.inputText);
        }

    };

    private void updateLabel(EditText editText) {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_project1, container, false);

        project = (Spinner) view.findViewById(R.id.project);
        branch = (Spinner) view.findViewById(R.id.branch);
        input = (LinearLayout) view.findViewById(inputData);
        Drawer.title.setText("بيانات المشروع");
        Drawer.postionSelected = -1;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        project_id = new HashMap<>();
        branch_id = new HashMap<>();

        BranchData();
        ProjectData();
        design();
        inputData();
        ActivityProjectEdit.middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (branch_id.size() == 0) {
                    Toast.makeText(getContext(), "اختر الفرع اولا", Toast.LENGTH_SHORT).show();
                    BranchData();
                } else if (project_id.size() == 0) {
                    Toast.makeText(getContext(), "اختر المشروع اولا", Toast.LENGTH_SHORT).show();
                    ProjectData();
                } else if (checkData()) {
                    ActivityProjectEdit.projectData.put("branchName", branch.getSelectedItem().toString());
                    ActivityProjectEdit.projectData.put("projectName", project.getSelectedItem().toString());
                    ActivityProjectEdit.projectData.put("branchId", branch_id.get(branch.getSelectedItem().toString()).toString());
                    ActivityProjectEdit.projectData.put("ProjectTypeId", project_id.get(project.getSelectedItem().toString()).toString());
                    ActivityProjectEdit.projectData.put("sk_number", sk_number.inputText.getText().toString());
                    ActivityProjectEdit.projectData.put("mo5tt_number", mo5tt_number.inputText.getText().toString());
                    ActivityProjectEdit.projectData.put("kt3a_number", kt3a_number.inputText.getText().toString());
                    ActivityProjectEdit.projectData.put("ground_space", ground_space.inputText.getText().toString());
                    ActivityProjectEdit.projectData.put("ro5sa_number", ro5sa_number.inputText.getText().toString());
                    ActivityProjectEdit.projectData.put("ro5sa_date", ro5sa_date.inputText.getText().toString());
                    ActivityProjectEdit.projectData.put("sk_date", sk_date.inputText.getText().toString());
                    ActivityProjectEdit.projectData.put("notes", notes.inputText.getText().toString());
                    project_postion = project.getSelectedItemPosition();
                    branch_postion = branch.getSelectedItemPosition();
                    current_data++;

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_right, R.anim.translat_left,
                            R.anim.translat_left);
                    EditProject2 fragment = new EditProject2();
                    ft.replace(R.id.activity_main_content_fragment3, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else
                    Toast.makeText(getContext(), "ادخل جميع البيانات المطلوبه", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }


    @Override
    public void onStop() {
        DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.setMargins(0, (int) (50 * ActivityProjectEdit.d), 0, (int) (110 * ActivityProjectEdit.d));
        ActivityProjectEdit.frameLayout.setLayoutParams(params1);
        super.onStop();
    }

    @Override
    public void onStart() {

        super.onStart();
        if (current_data % 2 != 0) {
            current_data++;
            try {
                sk_number.inputText.setText(ActivityProjectEdit.projectData.get("sk_number").toString());
                mo5tt_number.inputText.setText(ActivityProjectEdit.projectData.get("mo5tt_number").toString());
                kt3a_number.inputText.setText(ActivityProjectEdit.projectData.get("kt3a_number").toString());
                ro5sa_number.inputText.setText(ActivityProjectEdit.projectData.get("ro5sa_number").toString());
                ground_space.inputText.setText(ActivityProjectEdit.projectData.get("ground_space").toString());
                sk_date.inputText.setText(ActivityProjectEdit.projectData.get("sk_date").toString());
                ro5sa_date.inputText.setText(ActivityProjectEdit.projectData.get("ro5sa_date").toString());
                notes.inputText.setText(ActivityProjectEdit.projectData.get("notes").toString());
            } catch (Exception e) {

            }
        }

        DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.setMargins(0, (int) (50 * ActivityProjectEdit.d), 0, (int) (110 * ActivityProjectEdit.d));
        ActivityProjectEdit.frameLayout.setLayoutParams(params1);

        // Apply any required UI change now that the Fragment is visible.
    }


    private boolean checkData() {

        clearInputDesign(sk_number);

        Boolean bool = true;
        if (!checkNull(sk_number))
            bool = false;

        return bool;
    }

    private boolean checkNull(InputText view) {
        if (view.inputText.getText().toString().equals("")) {
            view.errorText.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.inputText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.errorRed)));
            }
            return false;
        }
        return true;
    }

    private void clearInputDesign(InputText view) {
        view.errorText.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.inputText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
        }
    }

    private void ProjectData() {

        try {
            new GetData(new GetData.AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    try {
                        JSONArray jArray = new JSONArray(result);
                        {
                            List<String> list_branches = new ArrayList<String>();
                            int postions = 0;
                            int counter = 0;

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject output = jArray.getJSONObject(i);
                                if (output.getString("PrjTypeName").equals(ActivityProjectEdit.projectDataBase.kind))
                                    postions = counter;
                                counter++;
                                list_branches.add(output.getString("PrjTypeName"));
                                project_id.put(output.getString("PrjTypeName"), output.getString("PrjTypeID"));
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, list_branches);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_layout_text);
                            project.setAdapter(dataAdapter);
                            getActivity().getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                            );

                            project.setSelection(postions);

                            if (project_postion != -1)
                                project.setSelection(project_postion);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, getActivity(),true).execute(new DataBase().WebService + "GetProjecttypes", "");

        } catch (Exception e) {
            Toast.makeText(getContext(), "حدث خطا فى الحصول على الانواع المشاريع, برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void BranchData() {
        try {
            new GetData(new GetData.AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    try {
                        JSONArray jArray = new JSONArray(result);
                        {
                            List<String> list_branches = new ArrayList<String>();
                            int postions = 0;
                            int counter = 0;
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject output = jArray.getJSONObject(i);
                                if (output.getString("BranchName").equals(ActivityProjectEdit.projectDataBase.BranchName))
                                    postions = counter;
                                list_branches.add(output.getString("BranchName"));
                                counter++;
                                branch_id.put(output.getString("BranchName"), output.getString("BranchID"));
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, list_branches);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_layout_text);
                            branch.setAdapter(dataAdapter);
                            getActivity().getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                            );

                            branch.setSelection(postions);
                            if (branch_postion != -1)
                                branch.setSelection(branch_postion);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, getActivity(),true).execute(new DataBase().WebService + "GetBranches", "");

        } catch (Exception e) {
            Toast.makeText(getContext(), "حدث خطا فى الحصول على الفروع , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void inputData() {
        sk_number = new InputText(getActivity());
        sk_number.inputText.setHint("رقم الصك");
        sk_number.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        sk_number.inputText.setText(ActivityProjectEdit.projectDataBase.SakNum);
        sk_number.inputText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
        sk_number.errorText.setVisibility(View.GONE);

        mo5tt_number = new InputText(getActivity());
        mo5tt_number.inputText.setHint("رقم المخطط");
        mo5tt_number.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        mo5tt_number.inputText.setText(ActivityProjectEdit.projectDataBase.PlanId);
        mo5tt_number.errorText.setVisibility(View.GONE);

        kt3a_number = new InputText(getActivity());
        kt3a_number.inputText.setHint("رقم القطعه");
        kt3a_number.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        kt3a_number.inputText.setText(ActivityProjectEdit.projectDataBase.GroundId);
        kt3a_number.errorText.setVisibility(View.GONE);

        ro5sa_number = new InputText(getActivity());
        ro5sa_number.inputText.setHint("رقم الرخصه");
        ro5sa_number.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        ro5sa_number.inputText.setText(ActivityProjectEdit.projectDataBase.LicenceNum);
        ro5sa_number.errorText.setVisibility(View.GONE);

        ground_space = new InputText(getActivity());
        ground_space.inputText.setHint("مساحة الارض");
        ground_space.inputText.setText(ActivityProjectEdit.projectDataBase.Space);
        ground_space.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        ground_space.errorText.setVisibility(View.GONE);


        ro5sa_date = new InputText(getActivity());
        ro5sa_date.inputText.setHint("تاريخ الرخصه");
        ro5sa_date.inputText.setInputType(InputType.TYPE_CLASS_DATETIME);
        ro5sa_date.inputText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.date_icon, 0, 0, 0);
        //ro5sa_date.inputText.setFocusable(false);
        ro5sa_date.inputText.setText(ActivityProjectEdit.projectDataBase.DateLicence);
        ro5sa_date.errorText.setVisibility(View.GONE);
        ro5sa_date.inputText.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void afterTextChanged(Editable s) {
                String str = ro5sa_date.inputText.getText().toString();
                if ((str.length() == 2 || str.length() == 5) && len < str.length()) {//len check for backspace
                    ro5sa_date.inputText.append("/");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                String str = ro5sa_date.inputText.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


        });
        /*ro5sa_date.inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date_ro5sa, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/

        sk_date = new InputText(getActivity());
        sk_date.inputText.setHint("تاريخ الصك");
        sk_date.inputText.setInputType(InputType.TYPE_CLASS_DATETIME);
        sk_date.inputText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.date_icon, 0, 0, 0);
        sk_date.errorText.setVisibility(View.GONE);
        //  sk_date.inputText.setFocusable(false);
        sk_date.inputText.setText(ActivityProjectEdit.projectDataBase.DataSake);
        sk_date.inputText.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void afterTextChanged(Editable s) {
                String str = sk_date.inputText.getText().toString();
                if ((str.length() == 2 || str.length() == 5) && len < str.length()) {//len check for backspace
                    sk_date.inputText.append("/");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                String str = sk_date.inputText.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


        });
        /*sk_date.inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date_sk, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/
        notes = new InputText(getActivity());
        notes.inputText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        notes.inputText.setSingleLine(false);
        notes.inputText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        notes.inputText.setHint("ملاحظات");
        notes.inputText.setText(ActivityProjectEdit.projectDataBase.Notes);
        notes.inputText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.note_icon, 0, 0, 0);
        notes.errorText.setVisibility(View.GONE);


        input.addView(sk_number.view);
        input.addView(mo5tt_number.view);
        input.addView(kt3a_number.view);
        input.addView(ro5sa_number.view);
        input.addView(ground_space.view);
        input.addView(sk_date.view);
        input.addView(ro5sa_date.view);
        input.addView(notes.view);


    }

    private void design() {
        ActivityProjectEdit.indicator.setImageResource(R.drawable.indicatorproject1);
        ActivityProjectEdit.right.setVisibility(View.INVISIBLE);
        ActivityProjectEdit.left.setVisibility(View.INVISIBLE);
        ActivityProjectEdit.middle.setVisibility(View.VISIBLE);
        ActivityProjectEdit.left.setText("التالي");

    }


}
