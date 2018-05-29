package com.computer.mazhihuapp.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by computer on 2015/9/15.
 */
public class HttpUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void get(String url,ResponseHandlerInterface responseHandlerInterface){
        client.get(url,responseHandlerInterface);
        Log.d("ddddddddddd", url);
    }
    public static void getImage(String url, ResponseHandlerInterface responseHandlerInterface){
        client.get(url,responseHandlerInterface);
        Log.d("zhihu",url);
    }
    public static boolean isNetworkConnected(Context context){
        if(context !=null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
