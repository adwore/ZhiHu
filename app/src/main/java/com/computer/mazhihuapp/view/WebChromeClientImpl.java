package com.computer.mazhihuapp.view;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by computer on 2015/10/9.
 */
public class WebChromeClientImpl extends WebChromeClient {

    private static final String TAG = "WebViewChromeClientImpl";

    private Context mContext;
    private LoginView.WebViewListener mListener;

    public WebChromeClientImpl(Context context, LoginView.WebViewListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {

        final LoginView childView = new LoginView(mContext,
                mListener);
        final WebSettings settings = childView.getSettings();
        settings.setJavaScriptEnabled(true);
        childView.setWebChromeClient(this);

        WebView.WebViewTransport transport =
                (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(childView);
        resultMsg.sendToTarget();

        childView.showDialog();

        return true;

    }

    @Override
    public void onCloseWindow(WebView window) {
        LoginView currentWebView = (LoginView) window;
        Log.d(TAG, "onCloseWindow");
        currentWebView.close();

    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

        String message = consoleMessage.sourceId() + "\n"
                + consoleMessage.message() + "\n" + consoleMessage.lineNumber();
        Log.d(TAG, message);
        return true;

    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {

        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        result.cancel();
        return true;

    }
}
