package com.wanggang.www.ndksodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView ndk_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ndk_txt = findViewById(R.id.ndk_txt);
        ndk_txt.setText(new JNIUtils().test());
        Log.i("wanggang", "MainActivity-onCreate");
    }
}
