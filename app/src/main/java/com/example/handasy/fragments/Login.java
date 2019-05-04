package com.example.handasy.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.handasy.controller.GetData;
import com.example.handasy.helpers.Helper;
import com.example.handasy.model.InputText;
import com.example.handasy.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.handasy.model.DataBase;
import com.example.handasy.view.CustomButton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ahmed on 4/1/2017.
 */

public class Login extends Fragment implements CustomButton.OnClickListener{
    CustomButton login;
    InputText mobile;
    LinearLayout linearLayout;
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mPrefs = getActivity().getPreferences(MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        init(view);

        login.setOnClickListener(this);

        return view;
    }


    private void init(View view) {

        login = (CustomButton) view.findViewById(R.id.login);

        linearLayout = (LinearLayout) view.findViewById(R.id.inputData);

        mobile = new InputText(getActivity());
        mobile.errorText.setVisibility(View.GONE);
        mobile.inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mobile.inputText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        mobile.inputText.setHint("ادخل رقم الجوال");
        mobile.inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        mobile.inputText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.call_text, 0);

        linearLayout.addView(mobile.view);
    }

    @Override
    public void onClick(View view) {

        //new Helper().createToast(getActivity().getBaseContext(), "login 2", Toast.LENGTH_SHORT);

        //if the mobile phone empty do nothing
        if (mobile.inputText.getText().toString().length() == 0){

            new Helper().createToast(getContext(), getActivity().getResources().getString(R.string.please_mobile), Toast.LENGTH_SHORT);

            return;
        }

        //otherwise

        JSONObject output = null;
        try {
            new GetData(new GetData.AsyncResponse() {
                @Override
                public void processFinish(String result) {
                    try {
                        final JSONObject output = new JSONObject(result);
                        if (!(output.getString("CustmoerId").contains("null"))) {

                            new GetData(new GetData.AsyncResponse() {
                                @Override
                                public void processFinish(String outputs) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(outputs);
                                        if (jsonObject.getString("Status").equals("True")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("kind", "login");
                                            bundle.putString("code", jsonObject.getString("Code") + "");
                                            bundle.putString("userId", output.getString("UserId"));
                                            bundle.putString("id", output.getString("CustmoerId"));
                                            bundle.putString("logo", output.getString("CustomerPhoto"));
                                            bundle.putString("mobile", output.getString("Mobile"));
                                            bundle.putString("name", output.getString("CustmoerName"));
                                            FragmentManager fm = getActivity().getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();
                                            Confirmation fragment = new Confirmation();
                                            fragment.setArguments(bundle);
                                            ft.setCustomAnimations(R.anim.slide_in_right,
                                                    R.anim.slide_out_right, R.anim.translat_left,
                                                    R.anim.translat_left);
                                            ft.addToBackStack(null);
                                            ft.replace(R.id.activity_main_content_fragment3, fragment);
                                            ft.commit();


                                        } else {

                                            //zyadat
                                            Bundle bundle = new Bundle();
                                            bundle.putString("kind", "login");
                                            bundle.putString("code", 1111 + "");
                                            bundle.putString("userId", output.getString("UserId"));
                                            bundle.putString("id", output.getString("CustmoerId"));
                                            bundle.putString("logo", output.getString("CustomerPhoto"));
                                            bundle.putString("mobile", output.getString("Mobile"));
                                            bundle.putString("name", output.getString("CustmoerName"));
                                            FragmentManager fm = getActivity().getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();
                                            Confirmation fragment = new Confirmation();
                                            fragment.setArguments(bundle);
                                            ft.setCustomAnimations(R.anim.slide_in_right,
                                                    R.anim.slide_out_right, R.anim.translat_left,
                                                    R.anim.translat_left);
                                            ft.addToBackStack(null);
                                            ft.replace(R.id.activity_main_content_fragment3, fragment);
                                            ft.commit();


//    Toast.makeText(getActivity(), "خطأ , برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //966 59 548 9308
                            }, getActivity(),true).execute("http://smusers.i-tecgroup.com/Service1.svc/SendSmsCodeActivation?mobile=966", mobile.inputText.getText().toString().substring(1));

                        } else {
                            Toast.makeText(getActivity(), "هذا الرقم غير مسجل , برجاء التسجيل اولا", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, getActivity(),true).execute(new DataBase().WebService + "GetEmptByMobileNum", "?mobileNum=" + mobile.inputText.getText().toString());
        } catch (Exception e) {

        }

    }
}
