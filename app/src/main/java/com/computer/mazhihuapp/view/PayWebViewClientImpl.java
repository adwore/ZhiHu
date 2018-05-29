package com.computer.mazhihuapp.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.computer.mazhihuapp.utils.net.GsonArrayRequest;
import com.computer.mazhihuapp.utils.net.GsonObjectRequest;
import com.computer.mazhihuapp.utils.net.RequestListener;
import com.computer.mazhihuapp.utils.net.VolleyManager;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by computer on 2015/10/10.
 */
public class PayWebViewClientImpl extends WebViewClient {


    private static final String TAG = "PayWebViewClientImpl";

    private Context mContext;
    private PayView.PayWebViewListener mListener;

    private String orderid;

    public String getOrderid() {
        return orderid;
    }


    public PayWebViewClientImpl(Context context, PayView.PayWebViewListener listener,String orderid) {
        this.mContext = context;
        this.mListener = listener;
        this.orderid = orderid;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        view.loadUrl(url);
        if (url.indexOf("http://x.xmyunyou.com/api/tenpay_paycomplete") != -1 || url.indexOf("http://x.xmyunyou.com/api/alipay_paycomplete") != -1) {
            view.setVisibility(View.GONE);
            requestOrderid();
        }
        return true;
    }

    private void requestOrderid() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderid", orderid);
        System.out.println("dddddddddorderid" + orderid);
        requestGet("http://x.xmyunyou.com/api/checkorder", params, Order.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                Order order = (Order) result;
                mListener.PayCallJava(order.getData().getPay_status(),order.getData().getOrder_id(),order.getData().getCp_order_id());
                PayView.closeDialog();
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("PayWebViewClientImpl", "launcher onPageFinished url:" + url);
    }

    public void requestGet(String url, Map<String, String> params, Class cla, RequestListener listener) {
        request(addParams(url, params), params, Request.Method.GET, cla, null, listener);
    }

    private static String addParams(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuffer p = new StringBuffer(url);
        p.append(url.contains("?") ? "&" : "?");
        for (String key : params.keySet()) {
            p.append(key).append("=").append(params.get(key)).append("&");
        }
        return p.substring(0, p.length() - 1);
    }


    private void request(final String url, Map<String, String> params, int method, final Class cla, final Type type, final RequestListener listener) {
        Log.d("dddddddrequest",url);
        Request request = null;
        if (cla != null) {
            request = new GsonObjectRequest(method, url, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        String responseBody = response.toString();
                        /*if (SyncStateContract.Constants.RSA.equals(url)) {
                            listener.onSuccess(responseBody);
                        } else*/
                        {
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
}
