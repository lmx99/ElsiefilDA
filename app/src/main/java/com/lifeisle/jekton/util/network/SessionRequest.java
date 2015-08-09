package com.lifeisle.jekton.util.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Preferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * see <a herf="http://blog.codeint.com/saving-user-session-after-login-using-webservices-in-android-app/">
 *     http://blog.codeint.com/saving-user-session-after-login-using-webservices-in-android-app/</a>
 * to get more information about setting (session) cookie.<br />
 *
 * @author Jekton
 * @version 0.01 7/22/2015
 */
public class SessionRequest extends SimplifiedJSONObjectRequest {

    private static final String TAG = "SessionRequest";


    public SessionRequest(int method, String url,
                          Response.Listener<JSONObject> listener,
                          Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        setParams(params);
        return params;
    }



    /**
     * Client code can override this method to manipulate the posting parameters.
     *
     * @param params A {@link Map} that contains parameters to be send to the server
     */
    protected void setParams(Map<String, String> params) {
        // The default behavior does nothing
    }




    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Map<String, String> headers = response.headers;
        String cookie = headers.get("Set-Cookie");
        Logger.d(TAG, "parseNetworkResponse() cookie :" + cookie);
        Preferences.saveCookie(cookie);

        return super.parseNetworkResponse(response);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        super.getHeaders();
        Map<String, String> headers = new HashMap<>();

        String cookie = Preferences.getCookie();
        if(!cookie.equals("")) {
            Logger.d(TAG, "getHeaders() cookie :" + cookie);
//            headers.put("Cookie", "PHPSESSID=123456789454368565675609658668586; path=/");
            headers.put("Cookie", cookie);
        }

        return headers;
    }


}
