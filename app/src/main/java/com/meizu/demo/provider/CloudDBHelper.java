package com.meizu.demo.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.meizu.demo.provider.table.TableCommonColumn;
import com.meizu.demo.provider.table.TableFlymeCommunicationColumn;
import com.meizu.demo.provider.table.TablePresenceColumn;
import com.meizu.demo.provider.table.TableSubscribeColumn;

import java.util.ArrayList;

/**
 * Created by liaojinlong on 15-3-20.
 */
public class CloudDBHelper extends SQLiteOpenHelper {

    private final String TAG = CloudDBHelper.class.getSimpleName();
    // 数据库名
    private static final String DB_NAME = "test.db";

    // 数据库版本
    private static final int VERSION = 3;


    public CloudDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableCommonColumn.CREATE_TABLE.create(db);
        TablePresenceColumn.CREATE_TABLE.create(db);
        TableSubscribeColumn.CREATE_TABLE.create(db);
        TableFlymeCommunicationColumn.CREATE_TABLE.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "[onUpgrade] oldVersion + " + oldVersion + ", newVersion = " + newVersion);
        if(db != null && oldVersion == 2 && newVersion == 3){
            try {
                db.execSQL("ALTER TABLE " + TableFlymeCommunicationColumn.TB_NAME + " RENAME TO " + TableFlymeCommunicationColumn.TB_NAME + "_tmp");
                onCreate(db);
                String sql = "INSERT INTO " + TableFlymeCommunicationColumn.TB_NAME + "(" + TableFlymeCommunicationColumn.PHONE_STATUS + ", "
                        + TableFlymeCommunicationColumn.MMS_STATUS + ", " + TableFlymeCommunicationColumn.NUMBER + ", "
                        + TableFlymeCommunicationColumn.PHONE_WIFI_ONLY + ", " + TableFlymeCommunicationColumn.INTERNATIONAL_CODE + ") "
                        + "SELECT status, status, number, wifi_only, \"+86\" FROM " + TableFlymeCommunicationColumn.TB_NAME + "_tmp "
                        + "WHERE bind = 6";
                db.execSQL(sql);
                db.execSQL("DROP TABLE " + TableFlymeCommunicationColumn.TB_NAME + "_tmp");
                Log.i(TAG, "[onUpgrade] sql = " + sql);
            } catch (Exception e){
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "[onDowngrade] oldVersion + " + oldVersion + ", newVersion = " + newVersion);
        if(db != null && oldVersion > newVersion){
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
                ArrayList<String> tableNames = new ArrayList<>();
                if(cursor != null && cursor.moveToFirst()){
                    do{
                        String name = cursor.getString(0);
                        if(!TextUtils.isEmpty(name) && !name.equalsIgnoreCase("android_metadata")){
                            tableNames.add(name);
                        }
                    } while (cursor.moveToNext());
                }
                if(tableNames.size() > 0){
                    for(String name : tableNames){
                        db.execSQL("DROP TABLE IF EXISTS " + name);
                    }
                }
                onCreate(db);
            } catch (Exception e){
                Log.e(TAG, e.getMessage());
            } finally {
                if(cursor != null){
                    cursor.close();
                }
            }
        }
    }
}
