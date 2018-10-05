package com.wanggang.www.ndksodemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView ndk_txt;
    private Context context;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("TestService", "onServiceConnected...");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("TestService", "onServiceDisconnected...");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        ndk_txt = findViewById(R.id.ndk_txt);
        ndk_txt.setText(new JNIUtils().test());
        ndk_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startService(new Intent(context, MyService.class));
                //bindService(new Intent(context, MyService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });


        //startService(new Intent(this, MyService.class));
        //bindService(new Intent(this, MyService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(mServiceConnection);
        //stopService(new Intent(this, MyService.class));

    }
}
