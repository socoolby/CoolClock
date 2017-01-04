package clock.socoolby.com.clock.utils;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import clock.socoolby.com.clock.ClockApplication;
import clock.socoolby.com.clock.protocol.RequestBase;

public final class NetworkService {
    private static NetworkService mInstance;
    private RequestQueue mRequestQueue;

    private DefaultRetryPolicy mRetryPolicy;


    private NetworkService() {
        mRequestQueue = getRequestQueue();
    }

    private final static String TAG = "NetworkService";

    public static synchronized NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue =
                    Volley.newRequestQueue(ClockApplication.getContext());
        }
        return mRequestQueue;
    }


    private void setRequestTimeout(int ms) {
        int timeout = (ms == 0) ? DefaultRetryPolicy.DEFAULT_TIMEOUT_MS : ms;

        mRetryPolicy = new DefaultRetryPolicy(timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }


    public boolean sendRequest(RequestBase request, final RequestListener listener) {
        String url = request.getUrl();
        JSONObject requestObject = request.createRequest();

        if (url == null || url.length() == 0 || requestObject == null) {
            return false;
        }
        Log.d(TAG, String.format("request URL:%s \nrequest Params:%s", request.getUrl(), requestObject.toString()));
        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "http response JsonString:" + response.toString());
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "http request fail");
                        listener.onRequestFailed(Constants.FAIL_CODE, error.toString());
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };
        setRequestTimeout(30000);
        jsonRequest.setRetryPolicy(mRetryPolicy);
        mRequestQueue.add(jsonRequest);
        return true;
    }

    public interface RequestListener {
        void onResponse(JSONObject response);

        void onRequestFailed(int error, String errorMessage);
    }
}
