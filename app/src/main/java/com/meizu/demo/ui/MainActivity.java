package com.meizu.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.meizu.demo.R;
import com.meizu.demo.provider.CloudProviderHelper;
import com.meizu.demo.provider.table.TablePresenceColumn;
import com.meizu.demo.service.MainService;
import com.meizu.demo.thread.EventCore;
import com.meizu.demo.ticker.VerifyCodeCountDownTimer;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start main service");
                /*String action = "com.meizu.servicedemo.mainservice";
                Intent intent = new Intent(action);
                intent.setPackage("com.meizu.servicedemo");
                startService(intent);*/
                Intent startIntent = new Intent(getApplicationContext(), MainService.class);
                startService(startIntent);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start sub service");
                String action = "com.meizu.servicedemo.subservice";
                Intent intent = new Intent(action);
                intent.setPackage("com.meizu.servicedemo");
                startService(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "create database");
                getLocalContactNumbers(getApplicationContext());
            }
        });

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "get verify code");
                new VerifyCodeCountDownTimer(60 * 1000, 1000, ((TextView) findViewById(R.id.tv1))).start();
                ((TextView) findViewById(R.id.tv1)).setEnabled(false);
            }
        });

        Float f = Settings.Global.getFloat(getContentResolver(), Settings.Global.TRANSITION_ANIMATION_SCALE , 1.5f);
        Log.d(TAG, "transition_animation_scale："+f);

        EventCore.getInstance().setContext(getApplicationContext());
        EventCore.getInstance().start();


        TimerTask task = new TimerTask(){
            public void run() {
                Log.d(TAG+"1", "thread:"+Thread.currentThread());
            }
        };
        Timer timer = new Timer("hangup");
        timer.schedule(task, 5000);


    }

    public static HashSet<String> getLocalContactNumbers(Context context) {
        HashSet<String> localContactNumbers = new HashSet<>();
        final Cursor result = context.getContentResolver().query(CloudProviderHelper.PRESENCE_CONTENT_URI, null, null, null, null);// 获取所有的同步记录
        if(result != null) {
            final int NumberColumnIndex = result.getColumnIndexOrThrow(TablePresenceColumn.CONTACT_NUMBER);
            try {
                if (result.getCount() > 0 && result.moveToFirst()) {
                    do {
                        localContactNumbers.add(result.getString(NumberColumnIndex));
                    } while (result.moveToNext());
                }
            } catch (Exception e) {
                //ignore
            } finally {
                result.close();
            }
        }
        return localContactNumbers;
    }

}
