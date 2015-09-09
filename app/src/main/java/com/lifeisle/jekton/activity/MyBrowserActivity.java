package com.lifeisle.jekton.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lifeisle.android.R;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Preferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Note: progressbar does not work
 *
 * @author Jekton
 * @version 0.01 8/1/2015
 */
public class MyBrowserActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MyBrowserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_my_browser);
        findViewById(R.id.close_button).setOnClickListener(this);

        WebView webView = (WebView) findViewById(R.id.web_view);

        setProgressBarVisibility(true);
        setProgressBarIndeterminateVisibility(true);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                setProgress(newProgress * 100);
                if (newProgress == 100) {
                    setProgressBarIndeterminateVisibility(false);
                    setProgressBarVisibility(false);
                }
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        Intent intent = getIntent();
        String dataString = intent.getDataString();
        Logger.d(TAG, "url = " + dataString);

        Map<String, String> extraHeader = new HashMap<>(1);
        extraHeader.put("Cookie", Preferences.getCookie());
        webView.loadUrl(dataString, extraHeader);

//        webView.setWebViewClient(new WebViewClient() {
//
//        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
