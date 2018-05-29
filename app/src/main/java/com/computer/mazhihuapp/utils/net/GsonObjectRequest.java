package com.computer.mazhihuapp.utils.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by sanmee on 2014/12/10.
 */
public class GsonObjectRequest extends Request<String> {

    private Map<String, String> mParams;
    private Response.Listener<String> mListener;

    public GsonObjectRequest(int method, String url, Map<String, String> params,
                             Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mParams = params;
        mListener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
           // String jsonString =
             //       new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            String jsonString =
                    new String(response.data,"utf-8");
            return Response.success(jsonString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if(mParams != null) {
            for (String key : mParams.keySet()){
            }
        }
        return mParams;
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
