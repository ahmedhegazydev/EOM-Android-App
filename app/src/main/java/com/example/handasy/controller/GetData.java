package com.example.handasy.controller;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.handasy.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ahmed on 4/23/2017.
 */

public class GetData extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;
    Dialog dialog;
    private boolean dialogShown;
    Activity activity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);

        try {
            if (!dialog.isShowing() && dialogShown) {
                dialog.show();
            }
        } catch (Exception e) {

        }

    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public GetData(AsyncResponse delegate, Activity activity, boolean dialogShown) {
        this.dialogShown = dialogShown;
        this.delegate = delegate;
        this.activity = activity;
    }


    @Override
    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0] + params[1].replaceAll(" ", "%20").replaceAll("\n", "%0A"));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            return buffer.toString();


        } catch (Exception e) {
            dialog.cancel();
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                dialog.cancel();
                e.printStackTrace();
            }
        }
        return "null";
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (result.equals("null"))
                Toast.makeText(activity, "حدث خطأ, برجاء المحاوله مرة اخرى", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            delegate.processFinish(result);
            Log.e("TAG-Result: ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        } catch (Exception e) {
        }
        super.onPostExecute(result);
    }
}