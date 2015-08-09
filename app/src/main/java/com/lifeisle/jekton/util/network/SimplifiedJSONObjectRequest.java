package com.lifeisle.jekton.util.network;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.lifeisle.jekton.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * A {@link Request} that works with session and responses a {@link JSONObject}
 *
 * @author Jekton
 * @version 0.01 7/21/2015
 */
public class SimplifiedJSONObjectRequest extends Request<JSONObject>{

    private static final String TAG = "SimplifiedJSONObjectRequest";
    protected static final String PROTOCOL_CHARSET = "utf-8";

    private Response.Listener<JSONObject> listener;

    public SimplifiedJSONObjectRequest(int method, String url, Response.Listener<JSONObject> listener,
                                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseData = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));


            Logger.d(TAG, "statusCode = " + response.statusCode);
            Logger.d(TAG, "responseData = " + responseData);

            Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
            setCacheEntry(entry);
            return Response.success(new JSONObject(responseData), entry);

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
}
