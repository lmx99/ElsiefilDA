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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Preferences;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.LoginRequest;

import org.json.JSONObject;

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

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_my_browser);
        findViewById(R.id.close_button).setOnClickListener(this);

        mWebView = (WebView) findViewById(R.id.web_view);

        setProgressBarVisibility(true);
        setProgressBarIndeterminateVisibility(true);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                setProgress(newProgress * 100);
                if (newProgress == 100) {
                    setProgressBarIndeterminateVisibility(false);
                    setProgressBarVisibility(false);
                }
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        MyApplication.addToRequestQueue(
                new LoginRequest(StringUtils.getServerPath(),
                                 Preferences.getUserName(),
                                 Preferences.getPassword(),
                                 new Response.Listener<JSONObject>() {
                                     @Override
                                     public void onResponse(JSONObject jsonObject) {
                                         loadWebPage();
                                     }
                                 },
                                 new Response.ErrorListener() {
                                     @Override
                                     public void onErrorResponse(VolleyError volleyError) {
                                         Toaster.showShort(MyBrowserActivity.this,
                                                           R.string.error_network_fail);
                                     }
                                 }));



//        webView.setWebViewClient(new WebViewClient() {
//
//        });
    }

    private void loadWebPage() {
        Intent intent = getIntent();
        String dataString = intent.getDataString();
        Logger.d(TAG, "url = " + dataString);

        Map<String, String> extraHeader = new HashMap<>(1);
        extraHeader.put("Cookie", Preferences.getCookie());
        mWebView.loadUrl(dataString, extraHeader);
    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
