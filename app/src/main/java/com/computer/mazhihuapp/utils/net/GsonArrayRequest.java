package com.computer.mazhihuapp.utils.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by sanmee on 2014/12/10.
 */
public class GsonArrayRequest extends JsonRequest<JSONArray> {

    private Map<String, String> mParams;

    public GsonArrayRequest(int method, String url, Map<String, String> params,
                            Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, null, listener, errorListener);
        mParams = params;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (!mParams.isEmpty()) {
            for (String key : mParams.keySet()){
            }
            return mParams;
        }
        return super.getParams();
    }
}
