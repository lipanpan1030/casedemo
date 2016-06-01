package com.meizu.demo.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by lipan on 16-5-20.
 */
public class GetPhoneNumberTask extends AsyncTask<String,String,String> {
    private final static String TAG = "GetPhoneNumberTask";
    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground-threadId:" +Thread.currentThread().getId());
        return "+18575606124";
    }

    @Override
    protected void onPostExecute(final String phoneNumber) {
        Log.i(TAG, "auto read phoneNumber " + phoneNumber);
        if(!TextUtils.isEmpty(phoneNumber)){
            Looper looper = Looper.myLooper();
            Log.d(TAG, "onPostExecute-threadId:" +Thread.currentThread().getId());
            new Handler(looper).postDelayed(new Runnable() {
                @Override
                public void run() {
                    int i=0;
                    i++;
                    Log.i(TAG, "i:" + i);
                }
            }, 1000);
        }
    }
}
