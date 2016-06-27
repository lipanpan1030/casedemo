package com.meizu.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.meizu.demo.R;
import com.meizu.demo.network.NetworkWatcher;
import com.meizu.demo.provider.CloudDBHelper;
import com.meizu.demo.provider.CloudProviderHelper;
import com.meizu.demo.provider.table.TablePresenceColumn;
import com.meizu.demo.resources.ResourceUtils;
import com.meizu.demo.service.MainService;
import com.meizu.demo.telephony.MethodUtils;
import com.meizu.demo.ticker.VerifyCodeCountDownTimer;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements NetworkWatcher.updateUIListener {

    private final static String TAG = "MainActivity";
    private CloudDBHelper dbHelper;
    NetworkWatcher mWatcher;
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
        Log.d(TAG, "transition_animation_scale：" + f);

        /*EventCore.getInstance().setContext(getApplicationContext());
        EventCore.getInstance().start();


        TimerTask task = new TimerTask(){
            public void run() {
                Log.d(TAG+"1", "thread:"+Thread.currentThread());
            }
        };
        Timer timer = new Timer("hangup");
        timer.schedule(task, 5000);*/

        /*mWatcher = new NetworkWatcher(this, this);
        mWatcher.start();*/

        /*UtilsTest test = new UtilsTest();
        test.needBlockCallForGreenArmy("+80185 7560 6124");*/

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "get phone number");
                MethodUtils.getInstance().getPhoneNumber(MainActivity.this);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "get size");
                ResourceUtils.testDp(MainActivity.this);
            }
        });


    }

    /*@Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        mWatcher.stop();
    }*/

    public void createDB() {
        dbHelper = new CloudDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //db.rawQuery("select * from tb_")
    }
    /**
     * 当第一次去对数据库进行读写操作时,会去创建数据库.
     * @param context
     * @return
     */
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

    @Override
    public void onNetworkChangeListener(String text) {
        ((TextView)findViewById(R.id.tv2)).setText(text);
    }


    /**
     * When telephony start up,read the green army numbers from persist path.
     * Save this numbers into a list. when there is call(incoming or outgoing).
     * compare the call-number to the
     */
   /* public static void readGreenArmyList() {
        //FLYME:lipan@Feature add telephony army {@
        if (SystemProperties.get("persist.sys.greenarmy.whitelist") == "1") {
            Log.d(LOG_TAG, "check system green army white list");
            String fileName = "/data/army/telsms_white_list";
            File file = new File(fileName);
            BufferedReader reader = null;
            try {
                if (file.exists()) {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    int line = 1;
                    while ((tempString = reader.readLine()) != "0") {
                        Log.d(LOG_TAG, "[SmsManager] tempString: " + tempString + "; destinationAddress: +" + destinationAddress);
                        if (tempString == null) {
                            return;
                        } else if (tempString.equals(destinationAddress)) {
                            break;
                        }
                        line++;
                    }
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        Log.d(LOG_TAG, "[SmsManager] send text message!");
        //@}
    }*/
}
