package com.example.handasy.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ActivityLogin;
import com.example.ActivityMain;
import com.example.handasy.controller.GetData;
import com.example.handasy.R;

import com.example.handasy.model.UrlData;

import org.json.JSONObject;

import com.example.handasy.model.DataBase;
import com.example.handasy.view.CustomButton;
import com.example.handasy.view.CustomTextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ahmed on 4/1/2017.
 */

public class Confirmation extends Fragment {
    String code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirmation, container, false);
        final Bundle bundle = getArguments();
        final String kind = bundle.getString("kind");
        code = bundle.getString("code");
        code = convertToArabic(code);
        Toast.makeText(getContext(), convertToArabic(code) + "", Toast.LENGTH_SHORT).show();
        CustomTextView sendAgain = (CustomTextView) view.findViewById(R.id.send);
        CustomButton check = (CustomButton) view.findViewById(R.id.confirm);
//        final CustomEditText code_confirm = (CustomEditText) view.findViewById(R.id.code);
        final EditText code_confirm = (EditText) view.findViewById(R.id.txtInContainer).findViewById(R.id.code);
        code_confirm.setInputType(InputType.TYPE_CLASS_NUMBER);
        sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetData(new GetData.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        try {
                            JSONObject jsonObject = new JSONObject(output);
                            if (jsonObject.getString("Status").equals("True")) {
                                code = convertToArabic(jsonObject.getString("Code"));
                            } else {
                                Toast.makeText(getActivity(), "خطأ فى ارسال الرساله , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, getActivity(),true).execute("http://smusers.i-tecgroup.com/Service1.svc/SendSmsCodeActivation?mobile=966", bundle.getString("mobile").substring(1));
            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code_confirm.getText().toString().equals(code)) {
                    if (kind.equals("login")) {
                        SharedPreferences mPrefs = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        prefsEditor.putString("mobile", bundle.getString("mobile"));
                        prefsEditor.commit();
                        ActivityLogin.C_ID = bundle.getString("id");
                        ActivityLogin.name = bundle.getString("name");
                        ActivityLogin.logo = bundle.getString("logo");
                        ActivityLogin.mobile = bundle.getString("mobile");
                        ActivityLogin.userId = bundle.getString("userId");
                        startActivity(new Intent(getActivity(), ActivityMain.class).putExtra("kind", kind));
                        getActivity().finish();
                    } else if (kind.equals("signup")) {

                        SharedPreferences mPrefs = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        prefsEditor.putString("id", bundle.getString("id"));
                        prefsEditor.putString("name", bundle.getString("name"));
                        prefsEditor.putString("mobile", bundle.getString("mobile"));
                        prefsEditor.commit();

                        ActivityLogin.name = bundle.getString("name");
                        ActivityLogin.mobile = bundle.getString("mobile");

                        UrlData data = new UrlData();
                        data.add("CustmoerName=" + bundle.getString("name"));
                        data.add("Mobile=" + bundle.getString("mobile"));
                        data.add("Email=" + bundle.getString("email"));
                        data.add("NationalId=" + bundle.getString("ssid"));
                        try {
                            new GetData(new GetData.AsyncResponse() {
                                @Override
                                public void processFinish(String result) {
                                    if (result.contains("Done")) {
                                        JSONObject output = null;
                                        try {
                                            new GetData(new GetData.AsyncResponse() {
                                                @Override
                                                public void processFinish(String result) {
                                                    JSONObject output = null;
                                                    try {
                                                        new GetData(new GetData.AsyncResponse() {
                                                            @Override
                                                            public void processFinish(String output) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(output);
                                                                    if (jsonObject.getString("Status").equals("True"))
                                                                        ;
                                                                    else {
                                                                        Toast.makeText(getActivity(), "خطأ فى ارسال الرساله , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, getActivity(),true).execute("http://smusers.i-tecgroup.com/Service1.svc/SendSmsLogInfo?mobile=966", bundle.getString("mobile").substring(1));

                                                        output = new JSONObject(result);
                                                        ActivityLogin.C_ID = output.getString("CustmoerId");
                                                        ActivityLogin.logo = output.getString("CustomerPhoto");
                                                        ActivityLogin.userId = output.getString("UserId");

                                                        Signup.nameText = "";
                                                        Signup.mobileText = "";
                                                        Signup.emailText = "";
                                                        Signup.idText = "";

                                                        startActivity(new Intent(getActivity(), ActivityMain.class).putExtra("kind", "login"));
                                                        getActivity().finish();

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, getActivity(),true).execute(new DataBase().WebService + "GetEmptByMobileNum", "?mobileNum=" + bundle.getString("mobile"));
                                        } catch (Exception e) {

                                        }
                                    } else
                                        Toast.makeText(getContext(), "عفوا خطأ فى تخزين البيانات ,حاول مره اخره", Toast.LENGTH_SHORT).show();

                                }
                            }, getActivity(),true).execute(new DataBase().WebService + "RegCustomers", data.get());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "الكود الذى ادخلته غير صحيح", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public String convertToArabic(String value) {
        String newValue = (((((((((((value + "").
                replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).
                replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).
                replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }
}
