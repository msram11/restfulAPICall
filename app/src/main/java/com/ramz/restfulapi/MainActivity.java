package com.ramz.restfulapi;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ramz.restfulapi.listener.CallbackListener;

import java.io.File;
import java.io.IOException;

/*
 Activity for the rest API call
 */

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView response_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button api_button = findViewById(R.id.button_api);
        response_text = findViewById(R.id.text_response);
        progressBar = findViewById(R.id.progress_bar);
        // enable caching the response
        try {
            File httpCacheDir = new File(getApplicationContext().getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i("MainActivity", "HTTP response cache installation failed:" + e);
        }
        // button click listener
        api_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                // invoke the API call from the presenter
                MainPresenter.getInstance().retrieveResponseFromAPI();
            }
        });
        // listen for the update from API call
        MainPresenter.getInstance().setListener(new CallbackListener() {
            @Override
            public void onUpdate(String result) {
                progressBar.setVisibility(View.GONE);
                response_text.setText(result);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // reset the listener anytime the activity is destroyed - prevents memory leak
        MainPresenter.getInstance().setListener(null);
    }
}