package com.example.handasy.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.handasy.R;
import com.example.handasy.model.InputText;
import com.example.handasy.controller.GetData;
import com.example.handasy.model.DataBase;
import com.example.handasy.model.UrlData;
import com.example.handasy.view.CustomButton;


/**
 * Created by ahmed on 4/1/2017.
 */

public class Signup extends Fragment {


    CustomButton sign = null;
    public static InputText name, mobile, email, id;
    static String nameText = "", mobileText = "", emailText = "", idText = "";
    LinearLayout input;
    View view = null;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        input = (LinearLayout) view.findViewById(R.id.inputData);
        sign = (CustomButton) view.findViewById(R.id.next);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!InvalidData())
                    return;
                if (!NotExist())
                    return;
                ConfirmData();



            }
        });
//        inputData();
inputData2();
        return view;
    }

    TextInputLayout tilEmail, tilPhone, tilId, tilName;
    EditText etName, etEmail, etPhone, etId;
    TextView tvErrorEmail, tvErrorName, tvErrorId, tvErrorPhone;
    private void inputData2() {

        View view2 = view.findViewById(R.id.sv);
        View view1 = view2.findViewById(R.id.ll1);
        View view3 = view1.findViewById(R.id.inputData);

        tilEmail = (TextInputLayout) view3.findViewById(R.id.tilEmail);
        tilId = (TextInputLayout) view3.findViewById(R.id.tilId);
        tilPhone = (TextInputLayout) view3.findViewById(R.id.tilPhone);
        tilName = (TextInputLayout) view3.findViewById(R.id.tilName);

        etName = tilName.getEditText();
        etName.setCompoundDrawablesWithIntrinsicBounds(null, null,
                resize(getActivity().getResources().getDrawable(R.drawable.account), 40, 40), null);
        etPhone = tilPhone.getEditText();
        //int yourInt = getResources().getIdentifier("call_text", "drawable", getActivity().getPackageName());
        //etPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, resize(getActivity().getResources().getDrawable(R.drawable.call_text)), 0);
        etPhone.setCompoundDrawablesWithIntrinsicBounds(null, null,
                resize(getActivity().getResources().getDrawable(R.drawable.call_text), 40, 40), null);
        etId = tilId.getEditText();
        etId.setCompoundDrawablesWithIntrinsicBounds(null, null,
                resize(getActivity().getResources().getDrawable(R.drawable.id), 40, 40), null);
        etEmail = tilEmail.getEditText();
        etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null,
                resize(getActivity().getResources().getDrawable(R.drawable.email), 40, 40), null);


        tvErrorEmail = (TextView) view3.findViewById(R.id.tvErrorEmail);
        tvErrorName = (TextView) view3.findViewById(R.id.tvErrorName);
        tvErrorId = (TextView) view3.findViewById(R.id.tvErrorId);
        tvErrorPhone = (TextView) view3.findViewById(R.id.tvErrorPhone);

        



    }

    private boolean InvalidData() {
        boolean b = true;
        if (etName.getText().toString().equals("")) {
            tvErrorName.setVisibility(View.VISIBLE);
            tvErrorName.setText("برجاء التأكد من اسم العميل");
//            tilName.setError(" ");
            etName.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.errorRed)));
            etName.setCompoundDrawablesWithIntrinsicBounds(
                    resize(getActivity().getResources().getDrawable(R.drawable.error), 40, 40), null,
                    resize(getActivity().getResources().getDrawable(R.drawable.account), 40, 40), null);
            b = false;
        }else
        {
            etName.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
            tvErrorName.setVisibility(View.INVISIBLE);
            etName.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    resize(getActivity().getResources().getDrawable(R.drawable.account), 40, 40), null);

        }
        if (etEmail.getText().toString().length() == 0 || !isValidEmail(etEmail.getText().toString())) {
            tvErrorEmail.setVisibility(View.VISIBLE);
            tvErrorEmail.setText("برجاء التأكد من البريد الإلكتروني");
//            tilEmail.setError("");
            etEmail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.errorRed)));
            etEmail.setCompoundDrawablesWithIntrinsicBounds(
                    resize(getActivity().getResources().getDrawable(R.drawable.error), 40, 40), null,
                    resize(getActivity().getResources().getDrawable(R.drawable.email), 40, 40), null);
            b = false;
        }else{
            etEmail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
            tvErrorEmail.setVisibility(View.INVISIBLE);
            etEmail.setCompoundDrawablesWithIntrinsicBounds(
                   null, null,
                    resize(getActivity().getResources().getDrawable(R.drawable.email), 40, 40), null);
        }

        if (!(etPhone.getText().length() == 10 || etPhone.getText().toString().startsWith("05"))) {
            tvErrorPhone.setVisibility(View.VISIBLE);
            tvErrorPhone.setText("برجاء التأكد من رقم الجوال");
//            tilPhone.setError("");
            etPhone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.errorRed)));
            etPhone.setCompoundDrawablesWithIntrinsicBounds(
                    resize(getActivity().getResources().getDrawable(R.drawable.error), 40, 40), null,
                    resize(getActivity().getResources().getDrawable(R.drawable.call_text), 40, 40), null);
            b = false;
        }else{
            etPhone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
            tvErrorPhone.setVisibility(View.INVISIBLE);
            etPhone.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    resize(getActivity().getResources().getDrawable(R.drawable.call_text), 40, 40), null);
        }
        if (!(etId.getText().toString().length() == 10)) {
            tvErrorId.setVisibility(View.VISIBLE);
            tvErrorId.setText("برجاء التأكد من رقم الهويه");
//            tilId.setError("");
            etId.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.errorRed)));
            etId.setCompoundDrawablesWithIntrinsicBounds(
                    resize(getActivity().getResources().getDrawable(R.drawable.error), 40, 40), null,
                    resize(getActivity().getResources().getDrawable(R.drawable.id), 40, 40), null);
            b = false;
        }else{
            etId.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
            tvErrorId.setVisibility(View.INVISIBLE);
            etId.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    resize(getActivity().getResources().getDrawable(R.drawable.id), 40, 40), null);
        }
        return b;
    }

    private Drawable resize(Drawable image, int w, int h) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, w, h, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    private void ConfirmData() {

      /*  Random random = new Random();
        String code = String.format("%04d", random.nextInt(10000));

       */
        new GetData(new GetData.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getString("Status").equals("True")) {
                        final Bundle bundle = new Bundle();
                        bundle.putString("kind", "signup");
                        bundle.putString("code", jsonObject.getString("Code"));
                        bundle.putString("name", etName.getText() + "");
                        bundle.putString("mobile", etPhone.getText() + "");
                        bundle.putString("email", etEmail.getText() + "");
                        bundle.putString("ssid",etId.getText() + "");
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
                        Toast.makeText(getActivity(), "خطأ فى ارسال الرساله, برجاء المحاوله مره اخرى", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(),true).execute("http://smusers.i-tecgroup.com/Service1.svc/SendSmsCodeActivation?mobile=966", etPhone.getText().toString().substring(1));
    }

    private boolean NotExist() {

        UrlData data = new UrlData();
        try {
            data.add("Email=" + etEmail.getText().toString());
            data.add("Mobile=" + etPhone.getText().toString());
            data.add("NationalId=" +etId.getText().toString());
            try {
                boolean b = true;
                JSONObject jObject = new JSONObject(new GetData(new GetData.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {

                    }
                }, getActivity(),true).execute(new DataBase().WebService + "CheckExistUsersData", data.get()).get());
                if ((jObject.get("Email").equals("false") && !(etEmail.getText().toString().equals("")))) {
                    tvErrorEmail.setVisibility(View.VISIBLE);
                    tvErrorEmail.setText("البريد الإلكتروني مسجل مسبقا");
                    b = false;
                }else{
                    etEmail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
                    tvErrorEmail.setVisibility(View.INVISIBLE);
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(
                            null, null,
                            resize(getActivity().getResources().getDrawable(R.drawable.email), 40, 40), null);
                }
                if ((jObject.get("Mobile").equals("false"))) {
                    tvErrorPhone.setVisibility(View.VISIBLE);
                    tvErrorPhone.setText("رقم الجوال مسجل مسبقا");
                    b = false;
                }else{
                    etPhone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
                    tvErrorPhone.setVisibility(View.INVISIBLE);
                    etPhone.setCompoundDrawablesWithIntrinsicBounds(
                            null, null,
                            resize(getActivity().getResources().getDrawable(R.drawable.call_text), 40, 40), null);
                }

                if ((jObject.get("NationalId").equals("false"))) {
                    tvErrorId.setVisibility(View.VISIBLE);
                    tvErrorId.setText("رقم الهويه مسجل مسبقا");
                    b = false;
                }else{
                    etId.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
                    tvErrorId.setVisibility(View.INVISIBLE);
                    etId.setCompoundDrawablesWithIntrinsicBounds(
                            null, null,
                            resize(getActivity().getResources().getDrawable(R.drawable.id), 40, 40), null);
                }
                return b;
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean checkData() {

//        clearInputDesign(name);
//        clearInputDesign(mobile);
//        clearInputDesign(email);
//        clearInputDesign(id);

        Boolean bool = true;
        if (!checkNull(etName))
            bool = false;
        if (!checkNull(etPhone))
            bool = false;
        if (!checkNull(etId))
            bool = false;

        return bool;
    }

    private boolean checkNull(EditText view) {
        if (view.getText().toString().equals("")) {
//            view.errorText.setVisibility(View.VISIBLE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                view.inputText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.errorRed)));
//            }
            return false;
        }
        return true;
    }

    private void clearInputDesign(InputText view) {
        view.errorText.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.inputText.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hintColor)));
        }
    }
//011111199047
    private void inputData() {
        name = new InputText(getActivity());
        etName.setHint("اسم العميل");
        etName.setHintTextColor(ContextCompat.getColor(getContext(), R.color.white));
        etName.setInputType(InputType.TYPE_CLASS_TEXT);
        etName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        etName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.account, 0);

        mobile = new InputText(getActivity());
        etPhone.setHint("رقم الجوال");
        etPhone.setHintTextColor(ContextCompat.getColor(getContext(), R.color.white));
        etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        etPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPhone.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        etPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.call_text, 0);

        email = new InputText(getActivity());
        etEmail.setHint("البريد الإلكتروني");
        etEmail.setHintTextColor(ContextCompat.getColor(getContext(), R.color.white));
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmail.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.email, 0);

        id = new InputText(getActivity());
       etId.setHint("رقم الهويه");
       etId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
       etId.setInputType(InputType.TYPE_CLASS_NUMBER);
       etId.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
       etId.setHintTextColor(ContextCompat.getColor(getContext(), R.color.white));
       etId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.id, 0);

        input.addView(name.view);
        input.addView(mobile.view);
        input.addView(email.view);
        input.addView(id.view);







    }

    @Override
    public void onStart() {

        etPhone.setText(mobileText);
        etEmail.setText(emailText);
       etId.setText(idText);
        etName.setText(nameText);

        super.onStart();

    }

    @Override
    public void onPause() {


        nameText = etName.getText().toString();
        mobileText = etPhone.getText().toString();
        idText =etId.getText().toString();
        emailText = etEmail.getText().toString();

        super.onPause();

    }

}
