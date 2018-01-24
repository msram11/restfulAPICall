package com.ramz.restfulapi;

import android.util.Log;

import com.ramz.restfulapi.listener.CallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Presenter class responsible to perform network calls
 */

class MainPresenter {

    private CallbackListener listener;
    private static MainPresenter instance = null;

    private MainPresenter() {
        // do nothing
    }

    static MainPresenter getInstance() {
        if (instance == null)
            instance = new MainPresenter();
        return instance;
    }

    void setListener(CallbackListener listener) {
        this.listener = listener;
    }

    void retrieveResponseFromAPI() {
        APIAsyncTask asyncTask = new APIAsyncTask(this);
        asyncTask.execute();
    }

    void updateResponse(JSONObject object) {
        JSONArray jsonArray;
        StringBuilder builder = new StringBuilder();
        try {
            jsonArray = object.getJSONArray("articleList");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dataObject = jsonArray.getJSONObject(i);
                String title = dataObject.getString("title");
                builder.append(title);
                builder.append("\n");
                String url = dataObject.getString("url");
                builder.append(url);
                builder.append("\n");
            }
        } catch (JSONException e) {
            Log.e("MainActivity", e.getMessage());
        }
        // update the call back listener with result
        if (listener != null) {
            listener.onUpdate(builder.toString());
        }
    }
}