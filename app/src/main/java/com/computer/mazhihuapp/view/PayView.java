package com.computer.mazhihuapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by computer on 2015/10/10.
 */
public class PayView extends WebView {
    public interface PayWebViewListener {
        public void PayCallJava(int pay_status,String order_id,String cp_order_id);
    }

    private PayWebViewListener mListener;
    private static AlertDialog alertDialog;
    private FrameLayout layout;
    private Handler mainThreadHandler;
    private TextView view;

    public PayView(Context context,PayWebViewListener listener, String orderid) {
        super(context);
        mListener = listener;
        mainThreadHandler = new Handler(context.getMainLooper());
        this.setWebViewClient(new PayWebViewClientImpl(context, listener,orderid));
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


        TextView textView = new TextView(context);
        textView .setText("返回游戏");


        this.setLayoutParams(webViewParams);
        this.setLayoutParams(textView);
        layout.addView(this);


    }

    private void setLayoutParams(TextView textView) {


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


    public void initWithUrl(String Token) {
        String payUrl = "http://x.xmyunyou.com/api/pay_page?order_token="+Token;
        this.loadUrl(payUrl);
    }


    public static void closeDialog() {
        alertDialog.dismiss();
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
