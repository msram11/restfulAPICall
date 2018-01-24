package com.ramz.restfulapi;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Asyn Task responsible to fetch data from rest API
 */

public class APIAsyncTask extends AsyncTask<Void, Void, String> {

    //private MainActivity activity;
    private MainPresenter presenter;
    // url to fetch the API from
    private final static String URL = "http://hmkcode.appspot.com/rest/controller/get.json";
    private StringBuilder stringBuilder = new StringBuilder();

    APIAsyncTask(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected String doInBackground(Void...strings) {
        try {
            URL webUrl = new URL(URL);
            HttpURLConnection urlConnection = (HttpURLConnection) webUrl.openConnection();
            urlConnection.setUseCaches(true);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                Log.i("StringBuilder", stringBuilder.toString());
                bufferedReader.close();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Exception in AsyncTask", e.getMessage());
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Log.i("INFO", response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            //JSONArray jsonArray = jsonObject.getJSONArray("errors");
            Log.d("JSONOBJECT ->", jsonObject.toString());
            presenter.updateResponse(jsonObject);
        } catch (JSONException e) {
            Log.e("JSONEXCEPTION", e.getMessage());
        }
    }
}
