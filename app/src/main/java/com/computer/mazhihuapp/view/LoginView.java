package com.computer.mazhihuapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by computer on 2015/10/8.
 */
public class LoginView extends WebView {

    public interface WebViewListener {
        public void LoginCallJava(int userid, String name, String token);
    }
    boolean layoutChangedOnce = false;
    private static AlertDialog alertDialog;
    private FrameLayout layout;
    private Handler mainThreadHandler;
    private WebViewListener mListener;
    public LoginView(Context context, WebViewListener listener) {
        super(context);
        this.mListener = listener;
        mainThreadHandler = new Handler(context.getMainLooper());
        this.setWebViewClient(new WebViewClientImpl(context, listener));
        this.setWebChromeClient(new WebChromeClientImpl(context, listener));
        getSettings().setDomStorageEnabled(true);
        getSettings().setJavaScriptEnabled(true);
        this.clearCache(true);
        this.clearHistory();
        SetLayout(context);
        setDialog(context);
    }

    private void setDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        /*builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                LoginView.this.onDialogClose();
            }

        });*/

        alertDialog = builder.create();
        alertDialog.setView(layout, 0, 0, 0, 0);
    }




    private void SetLayout(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        int deviceWidth = display.getWidth();
        int deviceHeight = display.getHeight();

        layout = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        params.width = deviceWidth;
        params.height = deviceHeight;
        layout.setLayoutParams(params);
        layout.setBackgroundColor(0);

        FrameLayout.LayoutParams webViewParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            webViewParams.height = (int) (deviceHeight);
        } else {
            webViewParams.height = (int) (deviceHeight);
        }

        this.setLayoutParams(webViewParams);

        layout.addView(this);
    }

//    public void initWithUrl() {
//        this.loadUrl(LoginUrl(url));
//    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!layoutChangedOnce) {
            super.onLayout(changed, l, t, r, b);
            layoutChangedOnce = true;
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(true, direction, previouslyFocusedRect);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }


    @JavascriptInterface
    public void showDialog() {

        mainThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                alertDialog.show();
            }

        });

    }


    public static void closeDialog() {
        alertDialog.dismiss();
    }



    public void close() {
        if (this.alertDialog != null) {
            this.alertDialog.cancel();
            this.alertDialog = null;
        }
    }

    public static String  LoginUrl(String url){
//        String LoginUrl = null;
//        String APPID = "16294";
//        String CBURL = "http://x.xmyunyou.com/android/back";
//        String APPKEY = "E1871E9D3B4945E1A353A3DC5691605E";
//        String time = String.valueOf(WebViewClientImpl.CurrentDate()/1000);
//        String Sign = APPID + CBURL + time + APPKEY;
//        LoginUrl = "http://x.xmyunyou.com/api/login?" + "appid=" + APPID + "&ts=" + time + "&sign=" + WebViewClientImpl.Md5Encode(Sign) + "&cburl=" + WebViewClientImpl.encode(CBURL) + "&from=40005androidapp";
        return url;
    }


    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pKeyEvent) {
        Log.d("LoginView", "onKeyDown");
        if (pKeyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (pKeyCode == KeyEvent.KEYCODE_BACK) {
                Log.d("LoginView", "onKeyDown BACK");
                    closeDialog();
                return true;
            }
        }
        return super.onKeyDown(pKeyCode, pKeyEvent);
    }

}
