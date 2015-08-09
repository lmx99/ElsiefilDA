package com.lifeisle.jekton.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Util class that can be used to post information to servers.<br />
 * Someone who when to use this class can choose to extend it and implements three template method
 * {@link boolean#onTimeout(int)}, {@link Poster#onSuccess(InputStream)},
 * and {@link boolean#onNetworkError(int)}, or, just uses the default behavior.
 *
 * @author Jekton Luo
 * @version 0.01 6/5/2015.
 */
public class Poster {

    private static final String TAG = "Poster";
    public static final int STATE_WRITE = 0;
    public static final int STATE_READ = 1;

    private String url;
    private StringBuilder query;
    private String data;
    private HttpURLConnection connection;

    private int timeoutMillis;


    public Poster(String url) {
        this(url, 1000 * 30);
    }

    public Poster(String url, int timeoutMillis) {
        this.url = url;
        this.timeoutMillis = timeoutMillis;
    }


    public void addParam(String key, String value) throws UnsupportedEncodingException {
        if (query == null) query = new StringBuilder();

        query.append('&');
        query.append(URLEncoder.encode(key, "UTF-8"));
        query.append('=');
        query.append(URLEncoder.encode(value, "UTF-8"));
    }


    public void post() {

        OutputStreamWriter writer = null;
        try {
            URL url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(timeoutMillis);
            connection.setReadTimeout(timeoutMillis);
            // callback of the subclass
            setRequestProperties(connection);
            connection.connect();


            writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            if (data == null) {
                if (query != null)
                    data = query.toString();
                else
                    data = "";
            }
            writer.write(data);
            writer.flush();

        } catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
        } catch (SocketTimeoutException e) {
            if (onTimeout(STATE_WRITE)) post();    // retry
        } catch (IOException e) {
            if (onNetworkError(STATE_WRITE)) post();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                // already posting data, doesn't need to retry, therefore,
                // don't call the onNetworkError()
                Log.e(TAG, e.toString());
            }
        }




        InputStream inputStream = null;
        try {
            onSuccess(connection);
            inputStream = connection.getInputStream();
            onSuccess(inputStream);
        } catch (SocketTimeoutException e) {
            if (onTimeout(STATE_READ)) post();
        } catch (IOException e) {
            if (onNetworkError(STATE_READ)) post();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }

    }



    protected void setRequestProperties(HttpURLConnection connection) {
        // do nothing
    }


    /**
     * Template method that called when timeout happened
     *
     * @param state
     *          used to specify the state when timeout occurred
     * @return <code>true</code> to retry, otherwise, <code>false</code> to quit.<br />
     * The default value returned is <code>false</code>.
     */
    protected boolean onTimeout(int state) {
        return false;
    }


    /**
     * Template method that called when network error occurred
     *
     * @param state
     *          used to specify the state when error occurred
     * @return <code>true</code> to retry, otherwise, <code>false</code> to quit.<br />
     * The default value returned is <code>false</code>.
     */
    protected boolean onNetworkError(int state) {
        return false;
    }


    /**
     * Template method that called after successfully post the data.<br>
     * Using the callback function to process the response data
     *
     * @param inputStream
     *          InputStream that can be read response data from
     */
    @Deprecated
    protected void onSuccess(InputStream inputStream) {
        // do nothing
    }

    protected void onSuccess(HttpURLConnection connection) {
        // do nothing
    }

}
