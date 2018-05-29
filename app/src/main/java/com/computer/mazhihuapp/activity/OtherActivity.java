package com.computer.mazhihuapp.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.computer.mazhihuapp.R;
import com.computer.mazhihuapp.application.myApplication;
import com.computer.mazhihuapp.model.MyData;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Created by computer on 2015/10/19.
 */
public class OtherActivity extends Activity {

    private TextView mTextView;
    private myApplication myApp;
    private TextView mClipTextView;
    private TextView mIntentTextView;
    public static String Name;
    public static int Age;
    private TextView mStaticTextView;

    private TextView mResultTextView;
    private Button mReturnButton;
    private EditText mInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otheractivity);

        mTextView = (TextView) findViewById(R.id.tv);
        myApp = myApplication.getApplication();
        mTextView.setText(">>>>" + myApp.getName());
        mClipTextView = (TextView) findViewById(R.id.tv_clip);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String name = clipboardManager.getText().toString();
        byte[] base64_byte = Base64.decode(name, Base64.DEFAULT);
        //将字符串转换对象
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(base64_byte);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            MyData myData = (MyData) objectInputStream.readObject();
            mClipTextView.setText(myData.toString());
        } catch (Exception e) {

        }
        mIntentTextView = (TextView) findViewById(R.id.tv_intent);
        final Intent intent = getIntent();
        mIntentTextView.setText("name>>>" + intent.getStringExtra("name") + ">>>age>>>" + intent.getIntExtra("age", 0));
        mStaticTextView = (TextView) findViewById(R.id.tv_static);
        mStaticTextView.setText("name<<<" + Name + "<<<age<<<" + Age);
        mResultTextView = (TextView) findViewById(R.id.result);
        mReturnButton = (Button) findViewById(R.id.return_result);
        mInputEditText = (EditText) findViewById(R.id.input_result);
        Intent intent1 = getIntent();
        mResultTextView.setText(intent1.getIntExtra("a", 0) + "+" + intent1.getIntExtra("b", 0) + "=？");
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.putExtra("three",Integer.parseInt(mInputEditText.getText().toString()));
                setResult(1,intent2);
                finish();
            }
        });
    }
}
