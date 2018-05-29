package com.computer.mazhihuapp.utils.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by sanmee on 2014/12/10.
 */
public class VolleyManager {

    private VolleyManager() {
    }

    private static VolleyManager ourInstance = new VolleyManager();

    private static RequestQueue mRequestQueue;

    public static VolleyManager getInstance(Context context) {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return ourInstance;
    }

    public void addRequest(Request<?> request, Object tag){
        if(tag != null){
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }


}
