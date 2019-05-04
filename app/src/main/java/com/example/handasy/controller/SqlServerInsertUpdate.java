package com.example.handasy.controller;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by ahmed on 3/25/2017.
 */


public class SqlServerInsertUpdate extends AsyncTask<String, String, Boolean> {
    Connection con;
    String query;
    String un, pass, db, ip;
    String z = "";
    Boolean isSuccess = false;
    public AsyncResponse response = null;

    public interface AsyncResponse {
        void processFinish(boolean output);
    }

    public SqlServerInsertUpdate(AsyncResponse delegate) {
        response = delegate;
    }

    @Override
    protected void onPreExecute() {
        ip = "204.93.193.254";
        db = "itecgroup_Handasy";
        un = "itecgroup_Ahmed";
        pass = "P@ssw0rd12345";
    }

    @Override
    protected void onPostExecute(Boolean resultSet) {
        response.processFinish(resultSet);
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        query = params[0];
        try {
            con = connectionclass(un, pass, db, ip);        // Connect to database
            if (con == null) {
                z = "Check Your Internet Access!";
            } else {
                // Change below query according to your own database.
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                return true;

            }
        } catch (Exception ex) {

            Log.e("database error : ", ex.getMessage());
            isSuccess = false;
            z = ex.getMessage();
        }
        return false;
    }


    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";databaseName=" + database + ";user=" + user + ";password="
                    + password + ";";
            //  ConnectionURL = "jdbc:jtds:sqlserver://" + server + database + ";user=" + user+ ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}