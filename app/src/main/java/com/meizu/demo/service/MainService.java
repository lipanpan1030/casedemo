package com.meizu.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.meizu.demo.thread.EventCore;

public class MainService extends Service {
    public MainService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MainService", "onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("MainService", "onStart");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MainService", "onStartCommand:" + intent.getAction());
        EventCore.getInstance().start();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
