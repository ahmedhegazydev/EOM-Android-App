package com.example.handasy.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputFilter;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.ActivityProjectAdd;
import com.example.handasy.model.DataBase;
import com.example.handasy.view.CustomButton;
import com.example.handasy.view.CustomTextView;

import static com.example.handasy.R.id.inputData;

public class NewProject1 extends Fragment {
    Spinner project, branch;
    int mCurCheckPosition;
    LinearLayout input;
    static int project_postion = -1, branch_postion = -1, current_data = 0;
    static private InputText sk_number, mo5tt_number, kt3a_number, ro5sa_number, ground_space, ro5sa_date, sk_date, notes;
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
        int GPSoff = 0;
        try {
            GPSoff = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (GPSoff == 0) {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_confirmation);
            CustomTextView textView = (CustomTextView) dialog.findViewById(R.id.text);
            textView.setText("برجاء تفعيل الوصول الى الموقع");
            CustomButton no = (CustomButton) dialog.findViewById(R.id.no);
            CustomButton yes = (CustomButton) dialog.findViewById(R.id.yes);
            no.setText("الغاء");
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            yes.setText("تفعيل");
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(onGPS);
                    dialog.cancel();
                }
            });
            dialog.show();
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        Drawer.title.setText("بيانات المشروع");

        project_id = new HashMap<>();
        branch_id = new HashMap<>();

        Drawer.postionSelected = -1;
        BranchData();
        ProjectData();
        design();
        inputData();
        ActivityProjectAdd.middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                project_postion = project.getSelectedItemPosition();
                branch_postion = branch.getSelectedItemPosition();
                if (branch_id.size() == 0 || branch_postion == 0) {
                    Toast.makeText(getContext(), "اختر الفرع اولا", Toast.LENGTH_SHORT).show();
                    if (branch_postion != 0)
                        BranchData();
                } else if (project_id.size() == 0 || project_postion == 0) {
                    Toast.makeText(getContext(), "اختر المشروع اولا", Toast.LENGTH_SHORT).show();
                    if (project_postion != 0)
                        ProjectData();
                } else if (checkData()) {
                    ActivityProjectAdd.projectData.put("branchName", branch.getSelectedItem().toString());
                    ActivityProjectAdd.projectData.put("projectName", project.getSelectedItem().toString());
                    ActivityProjectAdd.projectData.put("branchId", branch_id.get(branch.getSelectedItem().toString()).toString());
                    ActivityProjectAdd.projectData.put("ProjectTypeId", project_id.get(project.getSelectedItem().toString()).toString());
                    ActivityProjectAdd.projectData.put("sk_number", sk_number.inputText.getText().toString());
                    ActivityProjectAdd.projectData.put("mo5tt_number", mo5tt_number.inputText.getText().toString());
                    ActivityProjectAdd.projectData.put("kt3a_number", kt3a_number.inputText.getText().toString());
                    ActivityProjectAdd.projectData.put("ground_space", ground_space.inputText.getText().toString());
                    ActivityProjectAdd.projectData.put("ro5sa_number", ro5sa_number.inputText.getText().toString());
                    ActivityProjectAdd.projectData.put("ro5sa_date", ro5sa_date.inputText.getText().toString());
                    ActivityProjectAdd.projectData.put("sk_date", sk_date.inputText.getText().toString());
                    ActivityProjectAdd.projectData.put("notes", notes.inputText.getText().toString());
                    current_data++;

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_right, R.anim.translat_left,
                            R.anim.translat_left);
                    NewProject2 fragment = new NewProject2();
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
        params1.setMargins(0, (int) (50 * ActivityProjectAdd.d), 0, (int) (110 * ActivityProjectAdd.d));
        ActivityProjectAdd.frameLayout.setLayoutParams(params1);
        super.onStop();
    }


    @Override
    public void onStart() {

        super.onStart();
        if (current_data % 2 != 0) {
            current_data++;
            sk_number.inputText.setText(ActivityProjectAdd.projectData.get("sk_number").toString());
            mo5tt_number.inputText.setText(ActivityProjectAdd.projectData.get("mo5tt_number").toString());
            kt3a_number.inputText.setText(ActivityProjectAdd.projectData.get("kt3a_number").toString());
            ro5sa_number.inputText.setText(ActivityProjectAdd.projectData.get("ro5sa_number").toString());
            ground_space.inputText.setText(ActivityProjectAdd.projectData.get("ground_space").toString());
            sk_date.inputText.setText(ActivityProjectAdd.projectData.get("sk_date").toString());
            ro5sa_date.inputText.setText(ActivityProjectAdd.projectData.get("ro5sa_date").toString());
            notes.inputText.setText(ActivityProjectAdd.projectData.get("notes").toString());
            DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params1.setMargins(0, (int) (50 * ActivityProjectAdd.d), 0, (int) (110 * ActivityProjectAdd.d));
            ActivityProjectAdd.frameLayout.setLayoutParams(params1);

        }


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
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        // dialog.show();
        try {
            new GetData(new GetData.AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    try {
                        JSONArray jArray = new JSONArray(result);
                        List<String> list_branches = new ArrayList<String>();
                        list_branches.add("اختر المشروع");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject output = jArray.getJSONObject(i);
                            list_branches.add(output.getString("PrjTypeName"));
                            project_id.put(output.getString("PrjTypeName"), output.getString("PrjTypeID"));
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, list_branches);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_layout_text);
                        project.setAdapter(dataAdapter);
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                        project_postion = 0;
                        project.setSelection(project_postion);
                        if (project_postion != -1)
                            project.setSelection(project_postion);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, getActivity(),true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new DataBase().WebService + "GetProjecttypes", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BranchData() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        // dialog.show();
        try {
            new GetData(new GetData.AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    try {
                        JSONArray jArray = new JSONArray(result);
                        List<String> list_branches = new ArrayList<String>();
                        list_branches.add("اختر الفرع");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject output = jArray.getJSONObject(i);
                            list_branches.add(output.getString("BranchName"));
                            branch_id.put(output.getString("BranchName"), output.getString("BranchID"));
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, list_branches);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_layout_text);
                        branch.setAdapter(dataAdapter);
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );

                        branch_postion = 0;
                        branch.setSelection(branch_postion);
                        if (branch_postion != -1)
                            branch.setSelection(branch_postion);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, getActivity(),true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new DataBase().WebService + "GetBranches", "");

        } catch (Exception e) {
            Toast.makeText(getActivity(), "حدث خطأ فى الحصول على الفروع, برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void inputData() {

        sk_number = new InputText(getActivity());
        sk_number.inputText.setHint("رقم الصك");
        sk_number.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        sk_number.inputText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
        sk_number.errorText.setVisibility(View.GONE);

        mo5tt_number = new InputText(getActivity());
        mo5tt_number.inputText.setHint("رقم المخطط");
        mo5tt_number.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        mo5tt_number.errorText.setVisibility(View.GONE);

        kt3a_number = new InputText(getActivity());
        kt3a_number.inputText.setHint("رقم القطعه");
        kt3a_number.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        kt3a_number.errorText.setVisibility(View.GONE);

        ro5sa_number = new InputText(getActivity());
        ro5sa_number.inputText.setHint("رقم الرخصه");
        ro5sa_number.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        ro5sa_number.errorText.setVisibility(View.GONE);

        ground_space = new InputText(getActivity());
        ground_space.inputText.setHint("مساحة الارض");
        ground_space.inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        ground_space.errorText.setVisibility(View.GONE);


        ro5sa_date = new InputText(getActivity());
        ro5sa_date.inputText.setHint("تاريخ الرخصه");
        ro5sa_date.inputText.setInputType(InputType.TYPE_CLASS_DATETIME);
        ro5sa_date.inputText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.date_icon, 0, 0, 0);
        ro5sa_date.inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
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
       /* ro5sa_date.inputText.setFocusable(false);
        ro5sa_date.inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date_ro5sa, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
*/
        sk_date = new InputText(getActivity());
        sk_date.inputText.setHint("تاريخ الصك");
        sk_date.inputText.setInputType(InputType.TYPE_CLASS_DATETIME);
        sk_date.inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        sk_date.inputText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.date_icon, 0, 0, 0);
        sk_date.errorText.setVisibility(View.GONE);
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
        /*sk_date.inputText.setFocusable(false);
        sk_date.inputText.setOnClickListener(new View.OnClickListener() {
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
        ActivityProjectAdd.indicator.setImageResource(R.drawable.indicatorproject1);
        ActivityProjectAdd.right.setVisibility(View.INVISIBLE);
        ActivityProjectAdd.left.setVisibility(View.INVISIBLE);
        ActivityProjectAdd.middle.setVisibility(View.VISIBLE);
        ActivityProjectAdd.left.setText("التالي");

    }


}
