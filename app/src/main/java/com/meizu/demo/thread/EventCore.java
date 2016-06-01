package com.meizu.demo.thread;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lipan on 16-5-18.
 */
public class EventCore {

    private static EventCore sInstance;
    private HandlerThread mThread;
    private Context mContext;

    public void start() {
        mThread = new HandlerThread("EventCore");
        mThread.start();
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int i=0;
                Toast.makeText(mContext,"post",Toast.LENGTH_LONG).show();
                Log.d("EventCore" , "thread" + Thread.currentThread().getId() +"  " +Thread.currentThread());
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("EventCore" , "thread1:" + Thread.currentThread().getId() +"  " +Thread.currentThread());
            }
        }).start();
        Log.d("EventCore" , mThread.getId()+"-"+mThread.getThreadId() +"  " +Thread.currentThread());
    }
    public static EventCore getInstance() {
        if (sInstance == null) {
            sInstance = new EventCore();
        }
        return sInstance;
    }
    public void setContext(Context context) {
        mContext = context;
    }
}
