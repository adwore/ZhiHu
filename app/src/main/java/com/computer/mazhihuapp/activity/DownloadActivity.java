package com.computer.mazhihuapp.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.computer.mazhihuapp.R;
import com.computer.mazhihuapp.adapter.DownloadListAdapter;
import com.computer.mazhihuapp.application.myApplication;
import com.computer.mazhihuapp.model.MyData;
import com.computer.mazhihuapp.utils.net.NetUtil;
import com.computer.mazhihuapp.utils.net.Token;
import com.computer.mazhihuapp.utils.net.WebViewListener;
import com.computer.mazhihuapp.view.LoginView;
import com.computer.mazhihuapp.view.PayView;
import com.computer.mazhihuapp.view.PayWebViewClientImpl;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by computer on 2015/9/30.
 */
public class DownloadActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView mListView;
    private SwipeRefreshLayout mSr;
    private DownloadListAdapter mAdapter;
    private Button mLoginButton;
    private Button mPayButton;
    private Button mAllButton;
    private Button mClipButton;
    private Button mIntentButton;
    private Button mStaticButton;

    private Button mResultButton;
    private static int GOTO_CODE = 1001;
    private EditText mEditText1;
    private EditText mEditText2;
    private EditText mEditTetxt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mListView = (ListView) findViewById(R.id.down_list);
        mSr = (SwipeRefreshLayout) findViewById(R.id.sr);
        mLoginButton = (Button) findViewById(R.id.login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginView mWebView = new LoginView(DownloadActivity.this, new LoginView.WebViewListener() {

                    @Override
                    public void LoginCallJava(int userid, String name, String token) {
                        int getuserid = userid;
                        String getname = name;
                        String gettoken = token;
                        System.out.println("DDDgetuserid" + getuserid + getname + gettoken);
                    }
                });
                mWebView.loadUrl("http://x.xmyunyou.com/api/login?appid=16294&ts=1444462925&sign=aca32513cb8d40e2387b4b7c55da5a14&cburl=http%3A%2F%2Fx.xmyunyou.com%2Fandroid%2Fback&from=40005androidapp");
                mWebView.showDialog();
            }
        });

        mPayButton = (Button) findViewById(R.id.pay);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayView mPayView = new PayView(DownloadActivity.this, new PayView.PayWebViewListener() {
                    @Override
                    public void PayCallJava(int result, String order_id, String cp_order_id) {
                        int getpay_status = result;
                        String getorder_id = order_id;
                        String getcp_order_id = cp_order_id;
                        System.out.println("DDDgetpay_status" + getpay_status + getorder_id + getcp_order_id);
                    }
                }, "3031201510121129049958953");
                mPayView.initWithUrl("encode2_MiRhadfLpHKrFopu8EqhNQsCKgDPYzm0UTyyhB3VDf1lOmvPDJV_e_2fqXzKZeUa2WJCDsOsMUQiHFKEpGKssrFcFTLndGHXKIK1fLK6ChOWnjjcuB7p1K3qHwChXoEn0n6ael_e_2fJfgUBFRBG_e_2bi7RYPXd7OIRRoddvDzk6BZJpAuQ9SA_e_3d");
                mPayView.showDialog();
            }
        });
        mAllButton = (Button) findViewById(R.id.button_all);
        mAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myApplication myApp = (myApplication) getApplication();
                myApp.setName("jack");
                Intent intent = new Intent(DownloadActivity.this,OtherActivity.class);
                startActivity(intent);
            }
        });
        mClipButton = (Button) findViewById(R.id.button_clip);
        mClipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ClipboardManager mClip =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//                String  name = "jack";
//                mClip.setText(name);

                MyData myData = new MyData("jack",23);
                //将对象转成字符串
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                String base64String = "";
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(myData);
                    base64String = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
                    objectOutputStream.close();


                }catch (Exception e){

                }

                ClipboardManager mClip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                mClip.setText(base64String);
                startActivity(new Intent(DownloadActivity.this, OtherActivity.class));
            }
        });
        mIntentButton = (Button) findViewById(R.id.button_intent);
        mIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DownloadActivity.this,OtherActivity.class);
                intent.putExtra("name", "jack");
                intent.putExtra("age", 23);
//                startActivity(intent);
                startActivityForResult(intent,1000);
            }
        });
        mStaticButton = (Button) findViewById(R.id.button_static);
        mStaticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DownloadActivity.this,OtherActivity.class);
                OtherActivity.Name ="jack";
                OtherActivity.Age = 23;
                startActivity(intent);
            }
        });

        mEditText1 = (EditText) findViewById(R.id.edittext1);
        mEditText2 = (EditText) findViewById(R.id.edittext2);
        mEditTetxt3 = (EditText) findViewById(R.id.edittext3);
        mResultButton = (Button) findViewById(R.id.button_result);
        mResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DownloadActivity.this,OtherActivity.class);
                intent.putExtra("a",Integer.parseInt(mEditText1.getText().toString()));
                intent.putExtra("b",Integer.parseInt(mEditText2.getText().toString()));
                startActivityForResult(intent, GOTO_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==1){
            if (requestCode == GOTO_CODE){
                mEditTetxt3.setText(String.valueOf(data.getIntExtra("three",0)));
            }else if(requestCode == 1000){
                mEditTetxt3.setText(String.valueOf(data.getIntExtra("three",0)));
            }
        }

    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.slide_out_to_left_from_right);
    }


}
