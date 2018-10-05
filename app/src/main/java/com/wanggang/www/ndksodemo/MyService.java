package com.wanggang.www.ndksodemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {
    private Service startService;
    private Service bindService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TestService", "onBind...");
        bindService = this;
        if (bindService == startService) {
            Log.i("TestService", "bindService == startService");
        } else {
            Log.i("TestService", "bindService != startService");
        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("TestService", "onUnbind...");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TestService", "onCreate...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TestService", "onStartCommand...");
        startService = this;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TestService", "onDestroy...");
    }
}
