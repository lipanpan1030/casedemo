package com.meizu.demo.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by lipan on 16-6-3.
 */
public class NetworkWatcher {

    private Context mContext;
    private ConnectivityManager mConnManager;
    private BroadcastReceiver mNetworkReceiver;


    public NetworkWatcher(Context context, final updateUIListener listener) {
        mContext = context;
        mConnManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo ni = mConnManager.getActiveNetworkInfo();
                if (ni != null) {
                    Log.d("NetworkWatcher", ni.toString() + "\n" + ni.getSubtype());
                }
                if(ni != null && ni.isConnected()) {
                    String info = ni.toString();
                    listener.onNetworkChangeListener(info + "\n" + ni.getSubtype());
                }

            }
        };

    }

    public void start() {
        registerNetWorkChangeListener();
    }

    public void registerNetWorkChangeListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //因为从2g切4g时，现在没有发出CONNECTIVITY_ACTION广播，需要发出监听下面这个广播
        filter.addAction("android.intent.action.ANY_DATA_STATE");
        mContext.registerReceiver(mNetworkReceiver, filter, null, null);
    }

    public interface updateUIListener{
        void onNetworkChangeListener(String text);
    }

    public void stop() {
        mContext.unregisterReceiver(mNetworkReceiver);
    }
}
