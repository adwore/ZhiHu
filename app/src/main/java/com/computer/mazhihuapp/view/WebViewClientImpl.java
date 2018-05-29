package com.computer.mazhihuapp.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.computer.mazhihuapp.utils.net.GsonArrayRequest;
import com.computer.mazhihuapp.utils.net.GsonObjectRequest;
import com.computer.mazhihuapp.utils.net.RequestListener;
import com.computer.mazhihuapp.utils.net.Token;
import com.computer.mazhihuapp.utils.net.VolleyManager;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by computer on 2015/10/9.
 */
public class WebViewClientImpl extends WebViewClient {

    private Context mContext;
    private LoginView.WebViewListener mListener;

    private String token;
    private Token userInfo;
    public WebViewClientImpl(Context context, LoginView.WebViewListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        view.loadUrl(url);
        if (url.indexOf("http://x.xmyunyou.com/android/back") != -1) {
            view.setVisibility(View.GONE);
            token = url.substring(41, url.length() - 21);
            PostUserInfo();
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("WebViewClientImpl", "launcher onPageFinished url:" + url);
    }

    private void PostUserInfo(){
        String Current = String.valueOf(CurrentDate() / 1000);
        String NewSign = "16294" + token + Current + "E1871E9D3B4945E1A353A3DC5691605E";
        String url = "http://x.xmyunyou.com/api/userinfo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", "16294");
        params.put("ts", Current);
        params.put("token", token);
        params.put("sign", Md5Encode(NewSign));
        requestPost(url, params, Token.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                userInfo = (Token) result;
                if (userInfo != null && userInfo.getData().getUserid() > 0) {
                    mListener.LoginCallJava(userInfo.getData().getUserid(), userInfo.getData().getName(), userInfo.getData().getToken());
                    LoginView.closeDialog();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void requestPost(String url, Map<String, String> params, Class cla, RequestListener listener) {
        request(url, params, Request.Method.POST, cla, null, listener);
    }




    private void request(final String url, Map<String, String> params, int method, final Class cla, final Type type, final RequestListener listener) {
        Request request = null;
        if (cla != null) {
            request = new GsonObjectRequest(method, url, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        String responseBody = response.toString();
                        /*if (SyncStateContract.Constants.RSA.equals(url)) {
                            listener.onSuccess(responseBody);
                        } else*/ {
                            Object obj = null;
                            Gson gson = new Gson();
                            if (cla != null) {
                                obj = gson.fromJson(responseBody, cla);
                            }
                            listener.onSuccess(obj);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, errorListener(listener));
        }

        if (type != null) {
            request = new GsonArrayRequest(method, url, params, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        String responseBody = response.toString();
                        Object obj = null;
                        Gson gson = new Gson();
                        if (type != null) {
                            obj = gson.fromJson(responseBody, type);
                        }
                        listener.onSuccess(obj);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, errorListener(listener));
        }

        VolleyManager.getInstance(mContext).addRequest(request, "Android");
    }

    protected Response.ErrorListener errorListener(final RequestListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
    }


    //获取时间搓的方法
    public static long CurrentDate(){
        long timecurrentTimeMillis = System.currentTimeMillis();
        return  timecurrentTimeMillis;
    }

    //MD5加密
    public static String Md5Encode(String key) {
        byte[] hash = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {

        }
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }

    public static final String encode(String args){
        if(TextUtils.isEmpty(args))
            return "";
        String result = "";
        try {
            result = URLEncoder.encode(args, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  result;
    }


}
