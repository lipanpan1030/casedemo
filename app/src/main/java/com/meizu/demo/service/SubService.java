package com.meizu.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.meizu.demo.thread.EventCore;

public class SubService extends Service {
    public SubService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SubService", "onStartCommand:" + intent.getAction());
        EventCore.getInstance().start();
        return super.onStartCommand(intent, flags, startId);

    }
}
